package io.github.gunjiro.hj;

import org.junit.Test;

import io.github.gunjiro.hj.command.UnknownCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UnknownCommandActionTest {
    @Test
    public void testTake() {
        // 不明なコマンドが入力されたときのメッセージをチェック
        final StringBuilder output = new StringBuilder();

        final UnknownCommandAction action = new UnknownCommandAction(new UnknownCommandAction.Implementor() {
            @Override
            public void showMessage(String message) {
                output.append(message);
            }
        });

        action.take(new UnknownCommand(":-)"));

        assertThat(output.toString(), is("unknown command ':-)'"));
    }
}
