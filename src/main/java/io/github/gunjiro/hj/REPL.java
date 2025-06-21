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
import io.github.gunjiro.hj.unit.ThunkTableUnit;

public class REPL {
    private final Implementor implementor;

    public static interface Implementor {
        public void execute(String input);
        public void output(String text);
        public void newline();
        public State getState();
        public String getInput() throws IOException;
    }

    private static interface OperationUnit {
        public void operate(String input);
    }

    public REPL(Implementor implementor) {
        this.implementor = implementor;
    }

    public static interface Factory {
        public Environment createEnvironment();
        public InputReceiver createInputReceiver();
        public TextOutputUnit createTextOutputUnit();
        public ManagingStateUnit createManagingStateUnit();
        public InputUnit createInputUnit();
    }

    public static REPL create(Factory factory) {
        final TextOutputUnit textOutputUnit = factory.createTextOutputUnit();
        final ManagingStateUnit managingStateUnit = factory.createManagingStateUnit();
        final InputUnit inputUnit = factory.createInputUnit();
        final ThunkTableUnit thunkTableUnit = new EnvironmentThunkTableUnit(factory.createEnvironment());
        final OperationUnit operationUnit = new DefaultOperationUnit(thunkTableUnit, textOutputUnit, managingStateUnit);

        return new REPL(new Implementor() {

            @Override
            public void execute(String input) {
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

            @Override
            public String getInput() throws IOException {
                return inputUnit.getInput();
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
            implementor.output("> ");
            return implementor.getInput();
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

}
