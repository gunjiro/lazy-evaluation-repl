package io.github.gunjiro.hj;

import java.io.Reader;

import io.github.gunjiro.hj.unit.ThunkTableUnit;

public class EnvironmentThunkTableUnit extends ThunkTableUnit {
    private final Environment environment;

    public EnvironmentThunkTableUnit(Environment environment) {
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