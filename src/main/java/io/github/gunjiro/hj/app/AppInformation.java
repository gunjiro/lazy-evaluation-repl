package io.github.gunjiro.hj.app;

public class AppInformation {
    private State state = State.RUNNING;

    public enum State {
        RUNNING,
        STOPPING
    }

    public void changeStopping() {
        state = State.STOPPING;
    }

    public boolean isStateRunning() {
        return State.RUNNING.equals(state);
    }

}
