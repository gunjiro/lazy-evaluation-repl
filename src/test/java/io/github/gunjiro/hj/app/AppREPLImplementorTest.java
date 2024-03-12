package io.github.gunjiro.hj.app;

import org.junit.Test;

import io.github.gunjiro.hj.REPL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AppREPLImplementorTest {
    @Test
    public void testExecute() {
        // 入力が:qなら終了
        final AppREPLImplementor implementor = AppREPLImplementor.create();
        assertThat(implementor.execute(":q"), is(REPL.Result.Quit));
    }
}
