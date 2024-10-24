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

    private static Implementor createImplementor(UnitFactory factory) {
        final DisplayUnit display = factory.createDisplayUnit();
        final InputUnit input = factory.createInputUnit();
        final ControlUnit control = factory.createControlUnit();
        final ThunkTableUnit table = factory.createThunkTableUnit();

        return new Implementor() {

            @Override
            public String waitForInput() {
                display.printText("> ");
                return input.receive();
            }

            @Override
            public void execute(String input) {
                operate(input);
            }

            @Override
            public boolean isRunning() {
                return control.isStateRunning();
            }

            @Override
            public void showMessage(String message) {
                display.printMessage(message);
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
                        control.changeStopping();
                    }

                    @Override
                    public void sendText(String text) {
                        display.printText(text);
                    }

                    @Override
                    public void sendMessage(String message) {
                        display.printMessage(message);
                    }

                    @Override
                    public void load(String name) {
                        final FileLoader loader = new FileLoader(new FileLoader.DefaultImplementor() {

                            @Override
                            public void storeFunctions(Reader reader) {
                                try {
                                    table.addFunctions(reader);
                                } catch (ApplicationException e) {
                                    display.printMessage(e.getMessage());
                                }
                            }

                        });
                        loader.addObserver(display::printMessage);
                        loader.load(name);
                    }

                    @Override
                    public void sendBreak() {
                        display.startANewLine();
                    }

                }, new AppRequestOperator.Factory() {

                    @Override
                    public Thunk createThunk(String code) throws ApplicationException {
                        return table.createThunk(new StringReader(code));
                    }

                });
            }

        };
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

    private static UnitFactory createUnitFactory(Factory factory) {

        return new UnitFactory() {

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
            
        };
    }

    private static Implementor createImplementor(Factory factory) {
        return createImplementor(createUnitFactory(factory));
    }
}
