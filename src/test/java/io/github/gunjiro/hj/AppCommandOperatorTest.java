package io.github.gunjiro.hj;

import org.junit.Test;

import io.github.gunjiro.hj.command.UnknownCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.Reader;

public class AppCommandOperatorTest {
    @Test
    public void outputsMessageWhenInputIsUnknownCommand() throws ExitException {
        // 入力が不明なコマンドの場合、メッセージを出力する。
        // このテストでは「コマンド処理」の結果が「不明なコマンドを処理するアクション」の結果と同等になることを確認する。
        final UnknownCommand input = new UnknownCommand("☆☆☆☆☆");
        final StringBuilder outputByOperator = new StringBuilder();
        final StringBuilder outputByAction = new StringBuilder();

        final CommandOperator operator = AppCommandOperator.create(new ResourceProvider() {

            @Override
            public Reader open(String name) throws FailedException {
                throw new UnsupportedOperationException("Unimplemented method 'open'");
            }
        }, new MessagePrinter() {

            @Override
            public void printMessage(String message) {
                outputByOperator.append(message);
            }
            
        });

        final UnknownCommandAction action = new UnknownCommandAction(new UnknownCommandAction.Implementor() {

            @Override
            public void showMessage(String message) {
                outputByAction.append(message);
            }
            
        });

        operator.operate(new DefaultEnvironment(), input);
        action.take(input);

        assertThat(outputByOperator.toString(), is(outputByAction.toString()));
    }
}
