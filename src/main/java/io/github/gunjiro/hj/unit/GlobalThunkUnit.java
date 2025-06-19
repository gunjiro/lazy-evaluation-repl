package io.github.gunjiro.hj.unit;

import io.github.gunjiro.hj.environment.GlobalThunks;

public abstract class GlobalThunkUnit {
    public abstract void update(GlobalThunks thunks);
    public abstract GlobalThunks getGlobalThunks();
}
