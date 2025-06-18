package io.github.gunjiro.hj.unit;

import io.github.gunjiro.hj.state.State;

public abstract class ManagingStateUnit {
    public abstract void stopApplication();
    public abstract State getState();
}
