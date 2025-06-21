package io.github.gunjiro.hj;

import java.io.IOError;
import java.io.IOException;
import java.io.Reader;
import io.github.gunjiro.hj.repl.GeneralOperator;
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
        public GeneralOperator getGeneralOperator();
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
        final GeneralOperator generalOperator = new GeneralOperator(new GeneralOperator.Implementor() {

            @Override
            public TextOutputUnit createTextOutputUnit() {
                return textOutputUnit;
            }

            @Override
            public ManagingStateUnit createManagingStateUnit() {
                return managingStateUnit;
            }

            @Override
            public ThunkTableUnit createThunkTableUnit() {
                return new EnvironmentThunkTableUnit(factory.createEnvironment());
            }
            
        });

        return new REPL(new Implementor() {

            @Override
            public void execute(String input) {
                generalOperator.operate(input);
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
        private final GeneralOperator operator;

        private DefaultOperationUnit(ThunkTableUnit thunkTableUnit, TextOutputUnit textOutputUnit, ManagingStateUnit managingStateUnit) {
            this.operator = new GeneralOperator(new GeneralOperator.Implementor() {

                @Override
                public TextOutputUnit createTextOutputUnit() {
                    return textOutputUnit;
                }

                @Override
                public ManagingStateUnit createManagingStateUnit() {
                    return managingStateUnit;
                }

                @Override
                public ThunkTableUnit createThunkTableUnit() {
                    return thunkTableUnit;
                }
                
            });
        }

        @Override
        public GeneralOperator getGeneralOperator() {
            return operator;
        }

    }

    private static class EnvironmentThunkTableUnit extends ThunkTableUnit {
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
