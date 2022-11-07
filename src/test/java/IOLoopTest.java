import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class IOLoopTest {
    // :qコマンドがくるまでループ
    @Test
    public void loopShouldloopUntilQuitCommandIsInputed() {
        final LinkedList<String> inputs = new LinkedList<String>(List.of("", "", ":q"));
        final IOLoop ioLoop = new IOLoop(new InputReceiver() {
            @Override
            public String receive() {
                return inputs.pop();
            }
        }, AppExecuter.create());

        ioLoop.loop();

        assertThat(inputs, is(empty()));
    }
}
