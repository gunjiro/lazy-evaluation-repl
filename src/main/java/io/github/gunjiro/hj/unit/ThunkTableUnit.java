package io.github.gunjiro.hj.unit;

import java.io.Reader;

import io.github.gunjiro.hj.ApplicationException;
import io.github.gunjiro.hj.Thunk;

public interface ThunkTableUnit {
    public void addFunctions(Reader reader) throws ApplicationException;

    public Thunk createThunk(Reader reader) throws ApplicationException;
}