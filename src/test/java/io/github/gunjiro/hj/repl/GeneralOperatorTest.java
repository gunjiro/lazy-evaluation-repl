package io.github.gunjiro.hj.repl;

import org.junit.Test;

import io.github.gunjiro.hj.ApplicationException;
import io.github.gunjiro.hj.Thunk;
import io.github.gunjiro.hj.app.AppManagingStateUnit;
import io.github.gunjiro.hj.state.State;
import io.github.gunjiro.hj.unit.ManagingStateUnit;

import java.io.Reader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class GeneralOperatorTest {
    private static class StubImplementor implements GeneralOperator.Implementor {
        private final List<String> outputs = new LinkedList<>();
        private final ManagingStateUnit managingStateUnit = new AppManagingStateUnit();

        @Override
        public void addFunctions(Reader reader) throws ApplicationException {
            throw new UnsupportedOperationException("Unimplemented method 'addFunctions'");
        }

        @Override
        public Thunk createThunk(Reader reader) throws ApplicationException {
            throw new UnsupportedOperationException("Unimplemented method 'createThunk'");
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
        public void stopApplication() {
            managingStateUnit.stopApplication();
        }

        private State getState() {
            return managingStateUnit.getState();
        }

        private List<String> getOutputs() {
            return Collections.unmodifiableList(outputs);
        }
    }

    @Test
    public void quitCommandStopsApplication() {
        final StubImplementor implementor = new StubImplementor();
        final GeneralOperator operator = new GeneralOperator(implementor);

        operator.operate(":q");

        assertThat(implementor.getState(), is(State.STOPPING));
    }

    @Test
    public void unknownCommandOutputsMessage() {
        final StubImplementor implementor = new StubImplementor();
        final GeneralOperator operator = new GeneralOperator(implementor);

        operator.operate(":nothing");

        assertThat(implementor.getOutputs(), contains("unknown command ':nothing'", "↵"));
    }
}
