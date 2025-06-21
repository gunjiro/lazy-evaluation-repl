package io.github.gunjiro.hj.app;

import java.io.IOException;
import java.io.Reader;

import io.github.gunjiro.hj.ApplicationException;
import io.github.gunjiro.hj.REPL;
import io.github.gunjiro.hj.Thunk;
import io.github.gunjiro.hj.repl.GeneralOperator;
import io.github.gunjiro.hj.state.State;
import io.github.gunjiro.hj.unit.InputUnit;
import io.github.gunjiro.hj.unit.ManagingStateUnit;
import io.github.gunjiro.hj.unit.TextOutputUnit;
import io.github.gunjiro.hj.unit.ThunkTableUnit;

public class AppImplementorOfREPL implements REPL.Implementor {
    private final TextOutputUnit textOutputUnit = new AppTextOutputUnit();
    private final ManagingStateUnit managingStateUnit = new AppManagingStateUnit();
    private final InputUnit inputUnit = new AppInputUnit();
    private final ThunkTableUnit thunkTableUnit = new AppThunkTableUnit();

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

    private GeneralOperator createGeneralOperator() {
        return new GeneralOperator(new GeneralOperator.Implementor() {

            @Override
            public void addFunctions(Reader reader) throws ApplicationException {
                thunkTableUnit.addFunctions(reader);
            }

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
            
        });
    }
}
