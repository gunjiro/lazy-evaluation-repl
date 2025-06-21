package io.github.gunjiro.hj.unit;

import java.io.Reader;

import io.github.gunjiro.hj.ApplicationException;
import io.github.gunjiro.hj.Thunk;

public abstract class ThunkTableUnit {
    public abstract void addFunctions(Reader reader) throws ApplicationException;

    public abstract Thunk createThunk(Reader reader) throws ApplicationException;
}