package io.github.gunjiro.hj.command.operator;

import org.junit.Test;

import io.github.gunjiro.hj.ExitException;
import io.github.gunjiro.hj.UnknownCommandAction;
import io.github.gunjiro.hj.command.EmptyCommand;
import io.github.gunjiro.hj.command.LoadCommand;
import io.github.gunjiro.hj.command.QuitCommand;
import io.github.gunjiro.hj.command.UnknownCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.LinkedList;
import java.util.List;

public class AppCommandOperatorTest {
    @Test
    public void outputsMessageWhenInputIsUnknownCommand() throws ExitException {
        // 入力が不明なコマンドの場合、メッセージを出力する。
        // このテストでは「コマンド処理」の結果が「不明なコマンドを処理するアクション」の結果と同等になることを確認する。
        final UnknownCommand input = new UnknownCommand("☆☆☆☆☆");
        final StringBuilder outputByOperator = new StringBuilder();
        final StringBuilder outputByAction = new StringBuilder();

        final CommandOperator operator = new AppCommandOperator(new AppCommandOperator.Implementor() {

            @Override
            public void showMessage(String message) {
                outputByOperator.append(message);
            }

            @Override
            public void load(String name) {
                throw new UnsupportedOperationException("Unimplemented method 'load'");
            }
            
        });

        final UnknownCommandAction action = new UnknownCommandAction(new UnknownCommandAction.Implementor() {

            @Override
            public void showMessage(String message) {
                outputByAction.append(message);
            }
            
        });

        operator.operate(input);
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
            final CommandOperator operator = new AppCommandOperator(new AppCommandOperator.Implementor() {

                    @Override
                    public void showMessage(String message) {
                        throw new UnsupportedOperationException("Unimplemented method 'showMessage'");
                    }

                    @Override
                    public void load(String name) {
                        throw new UnsupportedOperationException("Unimplemented method 'load'");
                    }
                    
                }
            );
            operator.operate(new QuitCommand());
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

    @Test
    public void doNothingWhenInputIsEmptyCommand() {
        // 入力が空のコマンドの場合、何もしない。
        // このテストでは単に「コマンド処理」の呼び出しが通ることを確認する。
        String result = "☆☆☆☆☆ do nothing";
        final CommandOperator operator = new AppCommandOperator(new AppCommandOperator.Implementor() {

                @Override
                public void showMessage(String message) {
                    throw new UnsupportedOperationException("Unimplemented method 'showMessage'");
                }

                @Override
                public void load(String name) {
                    throw new UnsupportedOperationException("Unimplemented method 'load'");
                }

            });

        try {
            operator.operate(new EmptyCommand());
        } catch (ExitException e) {
            result = "☆☆☆☆☆ quit";
        }

        assertThat(result, is("☆☆☆☆☆ do nothing"));
    }

    @Test
    public void outputsMessagesAfterOperatingLoadCommand() throws ExitException {
        // 入力が読み込みコマンドの場合、リソースから関数定義等を読み込む。
        // このテストでは「コマンド処理」の結果が「読み込みコマンドを処理するアクション」の結果と同等になることを確認する。
        final LoadCommand input = new LoadCommand(List.of("resource1", "resource2"));
        final LinkedList<String> outputsByOperator = new LinkedList<String>();
        final LinkedList<String> outputsByAction = new LinkedList<String>();
        final CommandOperator operator = new AppCommandOperator(new AppCommandOperator.Implementor() {

            @Override
            public void showMessage(String message) {
                throw new UnsupportedOperationException("Unimplemented method 'showMessage'");
            }

            @Override
            public void load(String name) {
                outputsByOperator.add("loaded: " + name);
            }

        });
            
        final LoadCommandAction action = new LoadCommandAction(new LoadCommandAction.Implementor() {

            @Override
            public void load(String name) {
                outputsByAction.add("loaded: " + name);
            }
        });

        operator.operate(input);
        action.take(input);

        assertThat(outputsByOperator, is(outputsByAction));
    }
}