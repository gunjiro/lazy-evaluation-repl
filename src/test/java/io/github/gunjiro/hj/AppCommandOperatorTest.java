package io.github.gunjiro.hj;

import org.junit.Test;

import io.github.gunjiro.hj.command.QuitCommand;
import io.github.gunjiro.hj.command.UnknownCommand;
import io.github.gunjiro.hj.command.action.QuitCommandAction;

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
        }, new AppCommandOperator.Implementor() {

            @Override
            public void showMessage(String message) {
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

    @Test
    public void quitWhenInputIsQuitCommand() throws ExitException {
        // 入力が終了コマンドの場合、終了指示を出す。
        // このテストでは「コマンド処理」の結果が「終了コマンドを処理するアクション」の結果と同等になることを確認する。
        String resultByOperator ="☆☆☆☆☆ continue";
        String resultByAction = "☆☆☆☆☆ continue";

        try {
            final CommandOperator operator = AppCommandOperator.create(
                new ResourceProvider() {

                    @Override
                    public Reader open(String name) throws FailedException {
                        throw new UnsupportedOperationException("Unimplemented method 'open'");
                    }
                    
                },
                new AppCommandOperator.Implementor() {

                    @Override
                    public void showMessage(String message) {
                        throw new UnsupportedOperationException("Unimplemented method 'showMessage'");
                    }
                    
                }
            );
            operator.operate(new DefaultEnvironment(), new QuitCommand());
        } catch (ExitException e) {
            resultByOperator = "☆☆☆☆☆ quit";
        }

        try {
            final QuitCommandAction action = new QuitCommandAction();
            action.take(new QuitCommand());
        } catch (ExitException e) {
            resultByAction = "☆☆☆☆☆ quit";
        }

        assertThat(resultByOperator , is(resultByAction));
    }
}
