package io.github.gunjiro.hj;

import java.io.Reader;

public interface ThunkTableUnit {
    public void addFunctions(Reader reader) throws ApplicationException;

    public Thunk createThunk(Reader reader) throws ApplicationException;
}