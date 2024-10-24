package io.github.gunjiro.hj;

import java.io.IOError;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import io.github.gunjiro.hj.app.AppInformation;
import io.github.gunjiro.hj.processor.FileLoader;
import io.github.gunjiro.hj.ui.OutputOperation;

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
    }

    public REPL(Implementor implementor) {
        this.implementor = implementor;
    }

    public static interface Factory {
        public Environment createEnvironment();
        public OutputOperation createOutputOperation();
        public InputReceiver createInputReceiver();
        public AppInformation createAppInformation();
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

    private static class DefaultImplementor implements Implementor {
        private final DisplayUnit displayUnit;
        private final InputUnit inputUnit;
        private final ControlUnit controlUnit;
        private final ThunkTableUnit thunkTableUnit;

        private DefaultImplementor(DisplayUnit displayUnit, InputUnit inputUnit, ControlUnit controlUnit,
                ThunkTableUnit thunkTableUnit) {
            this.displayUnit = displayUnit;
            this.inputUnit = inputUnit;
            this.controlUnit = controlUnit;
            this.thunkTableUnit = thunkTableUnit;
        }

        @Override
        public String waitForInput() {
            displayUnit.printText("> ");
            return inputUnit.receive();
        }

        @Override
        public void showMessage(String message) {
            displayUnit.printMessage(message);
        }

        @Override
        public void execute(String input) {
            operate(input);
        }

        @Override
        public boolean isRunning() {
            return controlUnit.isStateRunning();
        }

        private void operate(String input) {
            final Request request = createRequest(input);
            createOperator().operate(request);
        }

        private Request createRequest(String input) {
            final RequestFactory factory = new RequestFactory();
            return factory.createRequest(input);
        }

        private AppRequestOperator createOperator() {
            return new AppRequestOperator(new AppRequestOperator.Implementor() {

                @Override
                public void quit() {
                    controlUnit.changeStopping();
                }

                @Override
                public void sendText(String text) {
                    displayUnit.printText(text);
                }

                @Override
                public void sendMessage(String message) {
                    displayUnit.printMessage(message);
                }

                @Override
                public void load(String name) {
                    final FileLoader loader = new FileLoader(new FileLoader.DefaultImplementor() {

                        @Override
                        public void storeFunctions(Reader reader) {
                            try {
                                thunkTableUnit.addFunctions(reader);
                            } catch (ApplicationException e) {
                                displayUnit.printMessage(e.getMessage());
                            }
                        }

                    });
                    loader.addObserver(displayUnit::printMessage);
                    loader.load(name);
                }

                @Override
                public void sendBreak() {
                    displayUnit.startANewLine();
                }

            }, new AppRequestOperator.Factory() {

                @Override
                public Thunk createThunk(String code) throws ApplicationException {
                    return thunkTableUnit.createThunk(new StringReader(code));
                }

            });
        }
    }

    private static Implementor createImplementor(UnitFactory factory) {
        return new DefaultImplementor(factory.createDisplayUnit(), factory.createInputUnit(), factory.createControlUnit(), factory.createThunkTableUnit());
    }

    private static class OutputOperationDisplayUnit implements DisplayUnit {
        private final OutputOperation outputOperation;

        private OutputOperationDisplayUnit(OutputOperation outputOperation) {
            this.outputOperation = outputOperation;
        }

        @Override
        public void printMessage(String message) {
            outputOperation.printMessage(message);
        }

        @Override
        public void printText(String text) {
            outputOperation.printText(text);
        }

        @Override
        public void startANewLine() {
            outputOperation.startANewLine();
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
            return new OutputOperationDisplayUnit(factory.createOutputOperation());
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

    }

    private static Implementor createImplementor(Factory factory) {
        return createImplementor(new DefaultUnitFactory(factory));
    }
}
