package io.github.gunjiro.hj;

import org.junit.Test;

import io.github.gunjiro.hj.command.Command;
import io.github.gunjiro.hj.command.UnknownCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.Reader;

public class AppCommandOperatorTest {
    @Test
    public void operateOutputsMessageWhenInputIsUnknownCommand() throws ExitException {
        // 入力が不明なコマンドの場合、メッセージを出力する。
        final Command input = new UnknownCommand("☆☆☆☆☆");
        final StringBuilder output = new StringBuilder();

        final CommandOperator operator = AppCommandOperator.create(new ResourceProvider() {

            @Override
            public Reader open(String name) throws FailedException {
                throw new UnsupportedOperationException("Unimplemented method 'open'");
            }
        }, new MessagePrinter() {

            @Override
            public void printMessage(String message) {
                output.append(message);
            }
            
        });

        operator.operate(new DefaultEnvironment(), input);

        assertThat(output.toString(), is("unknown command '☆☆☆☆☆'"));
    }
}
