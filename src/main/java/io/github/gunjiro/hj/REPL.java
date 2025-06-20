package io.github.gunjiro.hj;

import java.io.IOError;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import io.github.gunjiro.hj.processor.FileLoader;
import io.github.gunjiro.hj.state.State;
import io.github.gunjiro.hj.unit.ManagingStateUnit;
import io.github.gunjiro.hj.unit.TextOutputUnit;

public class REPL {
    private final Implementor implementor;

    public static interface Implementor {
        public String waitForInput();
        public void execute(String input);
        public void output(String text);
        public void newline();
        public State getState();
    }

    public static interface DisplayUnit {
        public void printMessage(String message);

        public void printText(String text);

        public void startANewLine();
    }

    public static interface InputUnit {
        public String receive();
    }

    public static interface ThunkTableUnit {
        public void addFunctions(Reader reader) throws ApplicationException;

        public Thunk createThunk(Reader reader) throws ApplicationException;
    }

    private static interface OperationUnit {
        public void operate(String input);
    }

    public static interface UnitFactory {
        public InputUnit createInputUnit();

        public ThunkTableUnit createThunkTableUnit();

        public TextOutputUnit createTextOutputUnit();

        public ManagingStateUnit createManagingStateUnit();
    }

    public REPL(Implementor implementor) {
        this.implementor = implementor;
    }

    public static interface Factory {
        public Environment createEnvironment();
        public InputReceiver createInputReceiver();
        public TextOutputUnit createTextOutputUnit();
        public ManagingStateUnit createManagingStateUnit();
    }

    public static REPL create(Factory factory) {
        return new REPL(createImplementor(factory));
    }

    public void run() {
        do {
            final String input = waitForInput();
            execute(input);
        } while (isRunning());

        showMessage("Bye.");
    }

    private String waitForInput() {
        return implementor.waitForInput();
    }

    private void execute(String input) {
        implementor.execute(input);
    }

    private boolean isRunning() {
        return State.RUNNING.equals(implementor.getState());
    }

    private void showMessage(String message) {
        implementor.output(message);
        implementor.newline();
    }

    private static class DefaultOperationUnit implements OperationUnit {
        private final ThunkTableUnit thunkTableUnit;
        private final TextOutputUnit textOutputUnit;
        private final ManagingStateUnit managingStateUnit;

        private DefaultOperationUnit(ThunkTableUnit thunkTableUnit, TextOutputUnit textOutputUnit, ManagingStateUnit managingStateUnit) {
            this.thunkTableUnit = thunkTableUnit;
            this.textOutputUnit = textOutputUnit;
            this.managingStateUnit = managingStateUnit;
        }

        @Override
        public void operate(String input) {
            final Request request = createRequest(input);
            createOperator().operate(request);
        }

        private Request createRequest(String input) {
            final RequestFactory factory = new RequestFactory();
            return factory.createRequest(input);
        }

        private FileLoader createFileLoader() {
            return new FileLoader(new FileLoader.DefaultImplementor() {

                @Override
                public void storeFunctions(Reader reader) {
                    try {
                        thunkTableUnit.addFunctions(reader);
                    } catch (ApplicationException e) {
                        textOutputUnit.output(e.getMessage());
                        textOutputUnit.newline();
                    }
                }

            });
        }

        private AppRequestOperator createOperator() {
            final AppRequestOperator.Implementor implementor = new AppRequestOperator.Implementor() {

                @Override
                public void quit() {
                    managingStateUnit.stopApplication();
                }

                @Override
                public void sendText(String text) {
                    textOutputUnit.output(text);
                }

                @Override
                public void sendMessage(String message) {
                    textOutputUnit.output(message);
                    textOutputUnit.newline();
                }

                @Override
                public void load(String name) {
                    final FileLoader loader = createFileLoader();
                    loader.addObserver(message -> {
                        textOutputUnit.output(message);
                        textOutputUnit.newline();
                    });
                    loader.load(name);
                }

                @Override
                public void sendBreak() {
                    textOutputUnit.newline();
                }

            };
            final AppRequestOperator.Factory factory = new AppRequestOperator.Factory() {

                @Override
                public Thunk createThunk(String code) throws ApplicationException {
                    return thunkTableUnit.createThunk(new StringReader(code));
                }

            };
            return new AppRequestOperator(implementor, factory);
        }

    }

    private static class DefaultImplementor implements Implementor {
        private final InputUnit inputUnit;
        private final ThunkTableUnit thunkTableUnit;
        private final TextOutputUnit textOutputUnit;
        private final ManagingStateUnit managingStateUnit;

        private DefaultImplementor(InputUnit inputUnit, ThunkTableUnit thunkTableUnit, TextOutputUnit textOutputUnit, ManagingStateUnit managingStateUnit) {
            this.inputUnit = inputUnit;
            this.thunkTableUnit = thunkTableUnit;
            this.textOutputUnit = textOutputUnit;
            this.managingStateUnit = managingStateUnit;
        }

        @Override
        public String waitForInput() {
            textOutputUnit.output("> ");
            return inputUnit.receive();
        }

        @Override
        public void execute(String input) {
            final OperationUnit operationUnit = new DefaultOperationUnit(thunkTableUnit, textOutputUnit, managingStateUnit);
            operationUnit.operate(input);
        }

        @Override
        public void output(String text) {
            textOutputUnit.output(text);
        }

        @Override
        public void newline() {
            textOutputUnit.newline();
        }

        @Override
        public State getState() {
            return managingStateUnit.getState();
        }
    }

    private static Implementor createImplementor(UnitFactory factory) {
        return new DefaultImplementor(factory.createInputUnit(), factory.createThunkTableUnit(), factory.createTextOutputUnit(), factory.createManagingStateUnit());
    }

    private static class InputReceiverInputUnit implements InputUnit {
        private final InputReceiver inputReceiver;

        private InputReceiverInputUnit(InputReceiver inputReceiver) {
            this.inputReceiver = inputReceiver;
        }

        @Override
        public String receive() {
            try {
                return inputReceiver.receive();
            } catch (IOException e) {
                throw new IOError(e);
            }
        }

    }

    private static class EnvironmentThunkTableUnit implements ThunkTableUnit {
        private final Environment environment;

        private EnvironmentThunkTableUnit(Environment environment) {
            this.environment = environment;
        }

        @Override
        public void addFunctions(Reader reader) throws ApplicationException {
            environment.addFunctions(reader);
        }

        @Override
        public Thunk createThunk(Reader reader) throws ApplicationException {
            return environment.createThunk(reader);
        }

    }

    private static class DefaultUnitFactory implements UnitFactory {
        private final Factory factory;

        private DefaultUnitFactory(Factory factory) {
            this.factory = factory;
        }

        @Override
        public InputUnit createInputUnit() {
            return new InputReceiverInputUnit(factory.createInputReceiver());
        }

        @Override
        public ThunkTableUnit createThunkTableUnit() {
            return new EnvironmentThunkTableUnit(factory.createEnvironment());
        }

        @Override
        public TextOutputUnit createTextOutputUnit() {
            return factory.createTextOutputUnit();
        }

        @Override
        public ManagingStateUnit createManagingStateUnit() {
            return factory.createManagingStateUnit();
        }

    }

    private static Implementor createImplementor(Factory factory) {
        return createImplementor(new DefaultUnitFactory(factory));
    }
}
