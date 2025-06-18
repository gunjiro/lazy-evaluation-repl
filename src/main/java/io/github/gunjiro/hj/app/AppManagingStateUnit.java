package io.github.gunjiro.hj.app;

import io.github.gunjiro.hj.state.State;
import io.github.gunjiro.hj.unit.ManagingStateUnit;

public class AppManagingStateUnit extends ManagingStateUnit {
    private State state = State.RUNNING;

    @Override
    public void stopApplication() {
        state = State.STOPPING;
    }

    @Override
    public State getState() {
        return state;
    }

}
