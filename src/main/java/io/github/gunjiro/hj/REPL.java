package io.github.gunjiro.hj;

import java.io.IOError;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import io.github.gunjiro.hj.app.AppInformation;
import io.github.gunjiro.hj.processor.FileLoader;
import io.github.gunjiro.hj.unit.TextOutputUnit;

public class REPL {
    private final Implementor implementor;

    public static interface Implementor {
        public String waitForInput();
        public void showMessage(String message);
        public void execute(String input);
        public boolean isRunning();
    }

    public static interface DisplayUnit {
        public void printMessage(String message);

        public void printText(String text);

        public void startANewLine();
    }

    public static interface InputUnit {
        public String receive();
    }

    public static interface ControlUnit {
        public void changeStopping();

        public boolean isStateRunning();
    }

    public static interface ThunkTableUnit {
        public void addFunctions(Reader reader) throws ApplicationException;

        public Thunk createThunk(Reader reader) throws ApplicationException;
    }

    private static interface OperationUnit {
        public void operate(String input);
    }

    public static interface UnitFactory {
        public DisplayUnit createDisplayUnit();

        public InputUnit createInputUnit();

        public ControlUnit createControlUnit();

        public ThunkTableUnit createThunkTableUnit();

        public TextOutputUnit createTextOutputUnit();
    }

    public REPL(Implementor implementor) {
        this.implementor = implementor;
    }

    public static interface Factory {
        public Environment createEnvironment();
        public InputReceiver createInputReceiver();
        public AppInformation createAppInformation();
        public TextOutputUnit createTextOutputUnit();
    }

    public static REPL create(Factory factory) {
        return new REPL(createImplementor(factory));
    }

    public void run() {
        do {
            final String input = implementor.waitForInput();
            implementor.execute(input);
        } while (implementor.isRunning());

        implementor.showMessage("Bye.");
    }

    private static class DefaultOperationUnit implements OperationUnit {
        private final ControlUnit controlUnit;
        private final ThunkTableUnit thunkTableUnit;
        private final TextOutputUnit textOutputUnit;

        private DefaultOperationUnit(ControlUnit controlUnit, ThunkTableUnit thunkTableUnit, TextOutputUnit textOutputUnit) {
            this.controlUnit = controlUnit;
            this.thunkTableUnit = thunkTableUnit;
            this.textOutputUnit = textOutputUnit;
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
                    controlUnit.changeStopping();
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
        private final ControlUnit controlUnit;
        private final ThunkTableUnit thunkTableUnit;
        private final TextOutputUnit textOutputUnit;

        private DefaultImplementor(InputUnit inputUnit, ControlUnit controlUnit,
                ThunkTableUnit thunkTableUnit, TextOutputUnit textOutputUnit) {
            this.inputUnit = inputUnit;
            this.controlUnit = controlUnit;
            this.thunkTableUnit = thunkTableUnit;
            this.textOutputUnit = textOutputUnit;
        }

        @Override
        public String waitForInput() {
            textOutputUnit.output("> ");
            return inputUnit.receive();
        }

        @Override
        public void showMessage(String message) {
            textOutputUnit.output(message);
            textOutputUnit.newline();
        }

        @Override
        public void execute(String input) {
            final OperationUnit operationUnit = new DefaultOperationUnit(controlUnit, thunkTableUnit, textOutputUnit);
            operationUnit.operate(input);
        }

        @Override
        public boolean isRunning() {
            return controlUnit.isStateRunning();
        }
    }

    private static Implementor createImplementor(UnitFactory factory) {
        return new DefaultImplementor(factory.createInputUnit(), factory.createControlUnit(), factory.createThunkTableUnit(), factory.createTextOutputUnit());
    }

    private static class TextOutputUnitDisplayUnit implements DisplayUnit {
        private final TextOutputUnit unit;

        public TextOutputUnitDisplayUnit(TextOutputUnit unit) {
            this.unit = unit;
        }

        @Override
        public void printMessage(String message) {
            unit.output(message);
            unit.newline();
        }

        @Override
        public void printText(String text) {
            unit.output(text);
        }

        @Override
        public void startANewLine() {
            unit.newline();
        }
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

    private static class AppInformationControlUnit implements ControlUnit {
        private final AppInformation appInformation;

        private AppInformationControlUnit(AppInformation appInformation) {
            this.appInformation = appInformation;
        }

        @Override
        public void changeStopping() {
            appInformation.changeStopping();
        }

        @Override
        public boolean isStateRunning() {
            return appInformation.isStateRunning();
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
        public DisplayUnit createDisplayUnit() {
            return new TextOutputUnitDisplayUnit(factory.createTextOutputUnit());
        }

        @Override
        public InputUnit createInputUnit() {
            return new InputReceiverInputUnit(factory.createInputReceiver());
        }

        @Override
        public ControlUnit createControlUnit() {
            return new AppInformationControlUnit(factory.createAppInformation());
        }

        @Override
        public ThunkTableUnit createThunkTableUnit() {
            return new EnvironmentThunkTableUnit(factory.createEnvironment());
        }

        @Override
        public TextOutputUnit createTextOutputUnit() {
            return factory.createTextOutputUnit();
        }

    }

    private static Implementor createImplementor(Factory factory) {
        return createImplementor(new DefaultUnitFactory(factory));
    }
}
