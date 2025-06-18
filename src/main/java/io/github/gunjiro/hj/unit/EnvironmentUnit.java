package io.github.gunjiro.hj.unit;

import io.github.gunjiro.hj.Thunk;

public abstract class EnvironmentUnit {
    public abstract void addThunk(Thunk t);
    public abstract Thunk getThunk(int level, int index);
}
