package io.github.gunjiro.hj;

import org.junit.Test;

import io.github.gunjiro.hj.REPL.Result;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.LinkedList;
import java.util.List;

public class REPLTest {
    @Test
    public void testRun() {
        // REPLは入力と実行の繰り返しを制御する。
        // このテストでは４回、空の入力をしたあと、５回目の終了コマンドで終了することを検証する。
        final CountREPLImplementor implementor = CountREPLImplementor.create("", "", "", "", ":q");
        final REPL repl = new REPL(implementor);

        repl.run();

        assertThat(implementor.getCount(), is(5));
    }

    private static class CountREPLImplementor implements REPL.Implementor {
        private final LinkedList<String> inputStack;
        private int count;

        private CountREPLImplementor(LinkedList<String> inputStack, int count) {
            this.inputStack = inputStack;
            this.count = count;
        }

        private static CountREPLImplementor create(String... inputs) {
            return new CountREPLImplementor(new LinkedList<String>(List.of(inputs)), 0);
        }

        public int getCount() {
            return count;
        }

        @Override
        public String waitForInput() {
            count++;
            return inputStack.pop();
        }

        @Override
        public Result execute(String input) {
            return ":q".equals(input) ? REPL.Result.Quit : REPL.Result.Continue;
        }

        @Override
        public void showQuitMessage() {
        }

    }

}
