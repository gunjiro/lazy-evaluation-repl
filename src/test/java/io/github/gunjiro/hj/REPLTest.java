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

    @Test
    public void printMessageWhenExecuteUnknownCommand() {
        // 存在しないコマンドを入力するとメッセージを出力する
        final Deque<String> output = new LinkedList<>();

        final Deque<String> inputs = new LinkedList<>(List.of( ":nothing", ":q"));
        final REPL repl = REPL.create(new REPL.Factory() {

            @Override
            public Environment createEnvironment() {
                return new DefaultEnvironment();
            }

            @Override
            public TextOutputUnit createTextOutputUnit() {
                return new TextOutputUnit() {

                    @Override
                    public void output(String text) {
                        output.add(text);
                    }

                    @Override
                    public void newline() {
                        output.add("↵");
                    }

                };
            }

            @Override
            public ManagingStateUnit createManagingStateUnit() {
                return new AppManagingStateUnit();
            }

            @Override
            public InputUnit createInputUnit() {
                return new InputUnit() {

                    @Override
                    public String getInput() throws IOException {
                        assert !inputs.isEmpty() : "..... already received all inputs .....";
                        return inputs.pop();
                    }
                    
                };
            }
            
        });
        repl.run();

        assertThat(output, hasItem("unknown command ':nothing'"));
    }

}
