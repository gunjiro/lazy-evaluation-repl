package io.github.gunjiro.hj.app;

import java.io.Reader;

import io.github.gunjiro.hj.ApplicationException;
import io.github.gunjiro.hj.DefaultEnvironment;
import io.github.gunjiro.hj.Environment;
import io.github.gunjiro.hj.Thunk;
import io.github.gunjiro.hj.unit.ThunkTableUnit;

public class AppThunkTableUnit extends ThunkTableUnit {
    private final Environment environment = new DefaultEnvironment();

    @Override
    public void addFunctions(Reader reader) throws ApplicationException {
        environment.addFunctions(reader);
    }

    @Override
    public Thunk createThunk(Reader reader) throws ApplicationException {
        return environment.createThunk(reader);
    }

}