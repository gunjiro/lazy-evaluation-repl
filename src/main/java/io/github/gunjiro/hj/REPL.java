package io.github.gunjiro.hj;

import java.io.IOError;
import java.io.IOException;
import io.github.gunjiro.hj.state.State;
import io.github.gunjiro.hj.unit.InputUnit;
import io.github.gunjiro.hj.unit.ManagingStateUnit;
import io.github.gunjiro.hj.unit.TextOutputUnit;

public class REPL {
    private final Implementor implementor;

    public static interface Implementor {
        public void operate(String input);
        public void output(String text);
        public void newline();
        public State getState();
        public String getInput() throws IOException;
    }

    public REPL(Implementor implementor) {
        this.implementor = implementor;
    }

    public static interface Factory {
        public Environment createEnvironment();
        public TextOutputUnit createTextOutputUnit();
        public ManagingStateUnit createManagingStateUnit();
        public InputUnit createInputUnit();
    }

    public void run() {
        do {
            final String input = waitForInput();
            operate(input);
        } while (isRunning());

        showMessage("Bye.");
    }

    private String waitForInput() {
        try {
            return implementor.getInput();
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

    private void operate(String input) {
        implementor.operate(input);
    }

    private boolean isRunning() {
        return State.RUNNING.equals(implementor.getState());
    }

    private void showMessage(String message) {
        implementor.output(message);
        implementor.newline();
    }

}
