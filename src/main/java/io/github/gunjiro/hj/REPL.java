package io.github.gunjiro.hj;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import io.github.gunjiro.hj.app.AppInformation;
import io.github.gunjiro.hj.processor.FileLoader;
import io.github.gunjiro.hj.ui.OutputOperation;

public class REPL {
    private final Implementor implementor;

    public static interface Implementor {
        public void showPrompt();
        public String waitForInput() throws IOException;
        public void showMessage(String message);
        public void execute(String input);
        public boolean isRunning();
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
        try {
            do {
                implementor.showPrompt();
                final String input = implementor.waitForInput();
                implementor.execute(input);
            } while (implementor.isRunning());

            implementor.showMessage("Bye.");
        } catch (IOException e) {
            implementor.showMessage(e.getMessage());
        }
    }

    private static Implementor createImplementor(Factory factory) {
        final Environment environment = factory.createEnvironment();
        final OutputOperation outOperation = factory.createOutputOperation();
        final InputReceiver receiver = factory.createInputReceiver();
        final AppInformation information = factory.createAppInformation();

        return new Implementor() {

            @Override
            public void showPrompt() {
                outOperation.printText("> ");
            }

            @Override
            public String waitForInput() throws IOException {
                return receiver.receive();
            }

            @Override
            public void execute(String input) {
                operate(input);
            }

            @Override
            public boolean isRunning() {
                return information.isStateRunning();
            }

            @Override
            public void showMessage(String message) {
                outOperation.printMessage(message);
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
                        information.changeStopping();
                    }

                    @Override
                    public void sendText(String text) {
                        outOperation.printText(text);
                    }

                    @Override
                    public void sendMessage(String message) {
                        outOperation.printMessage(message);
                    }

                    @Override
                    public void load(String name) {
                        final FileLoader loader = new FileLoader(new FileLoader.DefaultImplementor() {

                            @Override
                            public void storeFunctions(Reader reader) {
                                try {
                                    environment.addFunctions(reader);
                                } catch (ApplicationException e) {
                                    outOperation.printMessage(e.getMessage());
                                }
                            }

                        });
                        loader.addObserver(new FileLoader.Observer() {

                            @Override
                            public void receiveMessage(String message) {
                                outOperation.printMessage(message);
                            }

                        });
                        loader.load(name);
                    }

                    @Override
                    public void sendBreak() {
                        outOperation.startANewLine();
                    }

                }, new AppRequestOperator.Factory() {

                    @Override
                    public Thunk createThunk(String code) throws ApplicationException {
                        return environment.createThunk(new StringReader(code));
                    }

                });
            }

        };
    }
}
