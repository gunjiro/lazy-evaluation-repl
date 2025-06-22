package io.github.gunjiro.hj;

import org.junit.Test;

import io.github.gunjiro.hj.app.AppManagingStateUnit;
import io.github.gunjiro.hj.state.State;
import io.github.gunjiro.hj.unit.InputUnit;
import io.github.gunjiro.hj.unit.ManagingStateUnit;
import io.github.gunjiro.hj.unit.TextOutputUnit;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class REPLTest {
    private static class StubImplementor implements REPL.Implementor {
        private final ManagingStateUnit managingStateUnit = new AppManagingStateUnit();
        private final List<String> outputs = new LinkedList<>();
        private final Deque<String> inputs = new LinkedList<>();

        @Override
        public void operate(String input) {
            if (":q".equals(input)) {
                managingStateUnit.stopApplication();
            }
        }

        @Override
        public void output(String text) {
            outputs.add(text);
        }

        @Override
        public void newline() {
            outputs.add("↵");
        }

        @Override
        public State getState() {
            return managingStateUnit.getState();
        }

        @Override
        public String getInput() throws IOException {
            return inputs.removeFirst();
        }

        private void addInputs(String ... newInputs) {
            inputs.addAll(List.of(newInputs));
        }

        private List<String> getOutputs() {
            return Collections.unmodifiableList(outputs);
        }
    }

    @Test
    public void thisRepeatsTheProcessUntilItInputsQuitCommand() {
        final StubImplementor implementor = new StubImplementor();
        final REPL repl = new REPL(implementor);

        implementor.addInputs("", "", "", "", ":q");
        repl.run();

        assertThat(String.join("|", implementor.getOutputs()), endsWith("Bye.|↵"));
    }

}
