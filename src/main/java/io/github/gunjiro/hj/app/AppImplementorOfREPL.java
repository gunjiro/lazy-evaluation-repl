package io.github.gunjiro.hj.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

import io.github.gunjiro.hj.ApplicationException;
import io.github.gunjiro.hj.InputConverter;
import io.github.gunjiro.hj.Request;
import io.github.gunjiro.hj.Thunk;
import io.github.gunjiro.hj.processor.FileLoader;
import io.github.gunjiro.hj.repl.GeneralOperator;
import io.github.gunjiro.hj.repl.REPL;
import io.github.gunjiro.hj.state.State;
import io.github.gunjiro.hj.unit.FileOpenUnit;
import io.github.gunjiro.hj.unit.InputUnit;
import io.github.gunjiro.hj.unit.ManagingStateUnit;
import io.github.gunjiro.hj.unit.TextOutputUnit;
import io.github.gunjiro.hj.unit.ThunkTableUnit;

public class AppImplementorOfREPL implements REPL.Implementor {
    private final TextOutputUnit textOutputUnit = new AppTextOutputUnit();
    private final ManagingStateUnit managingStateUnit = new AppManagingStateUnit();
    private final InputUnit inputUnit = new AppInputUnit();
    private final ThunkTableUnit thunkTableUnit = new AppThunkTableUnit();
    private final FileOpenUnit fileOpenUnit = new AppFileOpenUnit();

    @Override
    public void operate(String input) {
        createGeneralOperator().operate(input);
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

    private FileLoader createFileLoader() {
        final FileLoader loader = new FileLoader(new FileLoader.Implementor() {
            @Override
            public Reader open(String filename) throws FileNotFoundException {
                return fileOpenUnit.open(filename);
            }

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

        loader.addObserver(message -> {
            textOutputUnit.output(message);
            textOutputUnit.newline();
        });

        return loader;
    }

    private GeneralOperator createGeneralOperator() {
        return new GeneralOperator(new GeneralOperator.Implementor() {
            private final FileLoader loader = createFileLoader();

            @Override
            public Thunk createThunk(Reader reader) throws ApplicationException {
                return thunkTableUnit.createThunk(reader);
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
            public void stopApplication() {
                managingStateUnit.stopApplication();
            }

            @Override
            public Request convertToRequest(String input) {
                final InputConverter converter = new InputConverter();
                return converter.convertToRequest(input);
            }

            @Override
            public void load(String name) {
                loader.load(name);
            }
            
        });
    }
}
