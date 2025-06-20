package io.github.gunjiro.hj;

import java.io.IOError;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import io.github.gunjiro.hj.processor.FileLoader;
import io.github.gunjiro.hj.state.State;
import io.github.gunjiro.hj.unit.InputUnit;
import io.github.gunjiro.hj.unit.ManagingStateUnit;
import io.github.gunjiro.hj.unit.TextOutputUnit;

public class REPL {
    private final Implementor implementor;

    public static interface Implementor {
        public String waitForInput() throws IOException;
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

    public static interface OldInputUnit {
        public InputUnit getNewInputUnit();
    }

    public static interface ThunkTableUnit {
        public void addFunctions(Reader reader) throws ApplicationException;

        public Thunk createThunk(Reader reader) throws ApplicationException;
    }

    private static interface OperationUnit {
        public void operate(String input);
    }

    public static interface UnitFactory {
        public OldInputUnit createInputUnit();

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
        final UnitFactory unitFactory = new DefaultUnitFactory(factory);
        final ThunkTableUnit thunkTableUnit = unitFactory.createThunkTableUnit();
        final TextOutputUnit textOutputUnit = unitFactory.createTextOutputUnit();
        final InputUnit inputUnit = unitFactory.createInputUnit().getNewInputUnit();
        final ManagingStateUnit managingStateUnit = unitFactory.createManagingStateUnit();

        return new REPL(new Implementor() {

            @Override
            public String waitForInput() throws IOException {
                textOutputUnit.output("> ");
                return inputUnit.getInput();
            }

            @Override
            public void execute(String input) {
                final OperationUnit operationUnit = new DefaultOperationUnit(thunkTableUnit, textOutputUnit,
                        managingStateUnit);
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

        });
    }

    public void run() {
        do {
            final String input = waitForInput();
            execute(input);
        } while (isRunning());

        showMessage("Bye.");
    }

    private String waitForInput() {
        try {
            return implementor.waitForInput();
        } catch (IOException e) {
            throw new IOError(e);
        }
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
        private final ThunkTableUnit thunkTableUnit;
        private final TextOutputUnit textOutputUnit;
        private final ManagingStateUnit managingStateUnit;
        private final InputUnit inputUnit;

        private DefaultImplementor(ThunkTableUnit thunkTableUnit, TextOutputUnit textOutputUnit, ManagingStateUnit managingStateUnit, InputUnit inputUnit) {
            this.thunkTableUnit = thunkTableUnit;
            this.textOutputUnit = textOutputUnit;
            this.managingStateUnit = managingStateUnit;
            this.inputUnit = inputUnit;
        }

        @Override
        public String waitForInput() throws IOException {
            textOutputUnit.output("> ");
            return inputUnit.getInput();
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

    private static class InputReceiverInputUnit implements OldInputUnit {
        private final InputReceiver inputReceiver;

        private InputReceiverInputUnit(InputReceiver inputReceiver) {
            this.inputReceiver = inputReceiver;
        }

        @Override
        public InputUnit getNewInputUnit() {
            return new InputUnit() {

                @Override
                public String getInput() throws IOException {
                    return inputReceiver.receive();
                }
                
            };
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
        public OldInputUnit createInputUnit() {
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

}
