package io.github.gunjiro.hj;

import org.junit.Test;

import io.github.gunjiro.hj.app.AppInformation;
import io.github.gunjiro.hj.ui.OutputOperation;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class REPLTest {

    @Test
    public void verifyLoop() {
        // REPLは入力と実行の繰り返しを制御する。
        // このテストでは４回、空の入力をしたあと、５回目の終了コマンドで終了することを検証する。
        final Deque<String> messages = new LinkedList<>();

        final Deque<String> inputs = new LinkedList<>(List.of("", "", "", "", ":q"));
        final REPL repl = REPL.create(new REPL.Factory() {

            @Override
            public Environment createEnvironment() {
                return new DefaultEnvironment();
            }

            @Override
            public OutputOperation createOutputOperation() {
                return new OutputOperation();
            }

            @Override
            public InputReceiver createInputReceiver() {
                return new InputReceiver() {

                    @Override
                    public String receive() {
                        assert !inputs.isEmpty() : "..... already received all inputs .....";
                        messages.add("..... received .....");
                        return inputs.pop();
                    }

                };
            }

            @Override
            public AppInformation createAppInformation() {
                return new AppInformation();
            }
            
        });
        repl.run();

        assertThat(messages, hasSize(5));
    }

    @Test
    public void printMessageWhenExecuteQuitCommand() {
        // 終了コマンド実行時にメッセージ出力
        final StringBuilder output = new StringBuilder();

        final Deque<String> inputs = new LinkedList<>(List.of( ":q"));
        final REPL repl = REPL.create(new REPL.Factory() {

            @Override
            public Environment createEnvironment() {
                return new DefaultEnvironment();
            }

            @Override
            public OutputOperation createOutputOperation() {
                return new OutputOperation() {

                    @Override
                    public void printMessage(String message) {
                        output.append(message);
                    }

                };
            }

            @Override
            public InputReceiver createInputReceiver() {
                return new InputReceiver() {

                    @Override
                    public String receive() {
                        assert !inputs.isEmpty() : "..... already received all inputs .....";
                        return inputs.pop();
                    }
                    
                };
            }

            @Override
            public AppInformation createAppInformation() {
                return new AppInformation();
            }
            
        });
        repl.run();

        assertThat(output, hasToString("Bye."));
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
            public OutputOperation createOutputOperation() {
                return new OutputOperation() {

                    @Override
                    public void printMessage(String message) {
                        output.add(message);
                    }

                };
            }

            @Override
            public InputReceiver createInputReceiver() {
                return new InputReceiver() {

                    @Override
                    public String receive() {
                        assert !inputs.isEmpty() : "..... already received all inputs .....";
                        return inputs.pop();
                    }

                };
            }

            @Override
            public AppInformation createAppInformation() {
                return new AppInformation();
            }
            
        });
        repl.run();

        assertThat(output, hasItem("unknown command ':nothing'"));
    }

}
