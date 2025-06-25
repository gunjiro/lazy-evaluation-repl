package io.github.gunjiro.hj.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

import io.github.gunjiro.hj.ApplicationException;
import io.github.gunjiro.hj.Thunk;
import io.github.gunjiro.hj.processor.FileLoader;
import io.github.gunjiro.hj.repl.GeneralOperator;
import io.github.gunjiro.hj.repl.REPL;
import io.github.gunjiro.hj.state.State;

public class AppImplementorOfREPL implements REPL.Implementor {
    private final AppUnitContainer container = new AppUnitContainer();
    private final GeneralOperator operator = createGeneralOperator();

    @Override
    public void operate(String input) {
        operator.operate(input);
    }

    @Override
    public void output(String text) {
        container.getTextOutputUnit().output(text);
    }

    @Override
    public void newline() {
        container.getTextOutputUnit().newline();
    }

    @Override
    public State getState() {
        return container.getManagingStateUnit().getState();
    }

    @Override
    public String getInput() throws IOException {
        return container.getInputUnit().getInput();
    }

    private FileLoader createFileLoader() {
        final FileLoader loader = new FileLoader(new FileLoader.Implementor() {
            @Override
            public Reader open(String filename) throws FileNotFoundException {
                return container.getFileOpenUnit().open(filename);
            }

            @Override
            public void storeFunctions(Reader reader) {
                try {
                    container.getThunkTableUnit().addFunctions(reader);
                } catch (ApplicationException e) {
                    container.getTextOutputUnit().output(e.getMessage());
                    container.getTextOutputUnit().newline();
                }
            }

        });

        loader.addObserver(message -> {
            container.getTextOutputUnit().output(message);
            container.getTextOutputUnit().newline();
        });

        return loader;
    }

    private GeneralOperator createGeneralOperator() {
        return new GeneralOperator(new GeneralOperator.Implementor() {
            private final FileLoader loader = createFileLoader();

            @Override
            public Thunk createThunk(Reader reader) throws ApplicationException {
                return container.getThunkTableUnit().createThunk(reader);
            }

            @Override
            public void output(String text) {
                container.getTextOutputUnit().output(text);
            }

            @Override
            public void newline() {
                container.getTextOutputUnit().newline();
            }

            @Override
            public void stopApplication() {
                container.getManagingStateUnit().stopApplication();
            }

            @Override
            public void load(String name) {
                loader.load(name);
            }
            
        });
    }
}
