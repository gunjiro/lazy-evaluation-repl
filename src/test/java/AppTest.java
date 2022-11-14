import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

public class AppTest {
    // :qでメッセージを出力して終了
    @Test
    public void runShouldExitWhenInputIsQuitCommand() {
        final String input = ":q";
        final StringBuilder builder = new StringBuilder();
        final AppFactory factory = new AppFactory();
        final App app = factory.create(new InputReceiver() {
            @Override
            public String receive() {
                return input;
            }
        }, null, null, new MessagePrinter() {
            @Override
            public void printMessage(String message) {
                builder.append(message);
            }
        });

        app.run();

        assertThat(builder.toString(), is("Bye."));
    }

    // 宣言の読み込みと式の評価
    @Test
    public void runCanLoadAndEvaluate() {
        final LinkedList<String> inputs = new LinkedList<String>(List.of(":l sample1 sample2", "one + two", ":q"));
        final LinkedList<String> decls = new LinkedList<String>(List.of("one = 1", "two = one + one"));
        final StringBuilder builder = new StringBuilder();
        final AppFactory factory = new AppFactory();
        final App app = factory.create(new InputReceiver() {
            @Override
            public String receive() {
                return inputs.poll();
            }
        }, new ResourceProvider() {
            @Override
            public Reader open(String name) throws ResourceProvider.FailedException {
                return new StringReader(decls.poll());
            }
        }, new StringPrinter() {
            @Override
            public void print(String s) {
                builder.append(s);
            }
        }, new MessagePrinter() {
            @Override
            public void printMessage(String message) {
            }
        });

        app.run();

        assertThat(builder.toString(), is("3"));
    }

    // 入力が空なら何もせず、入力待ちに戻る
    @Test
    public void runNotDoAnythingWhenInputIsEmptyString() {
        final LinkedList<String> inputs = new LinkedList<String>(List.of("", ":q"));
        final AppFactory factory = new AppFactory();
        final App app = factory.create(new InputReceiver() {
            @Override
            public String receive() {
                return inputs.poll();
            }
        }, new ResourceProvider() {
            @Override
            public Reader open(String name) throws ResourceProvider.FailedException {
                return null;
            }
        }, new StringPrinter() {
            @Override
            public void print(String s) {
            }
        }, new MessagePrinter() {
            @Override
            public void printMessage(String message) {
            }
        });

        app.run();
    }

    // :を入力すると何もせず、入力待ちに戻る
    @Test
    public void runNotDoAnythingWhenInputIsEmptyCommand() {
        final LinkedList<String> inputs = new LinkedList<String>(List.of(":", ":q"));
        final AppFactory factory = new AppFactory();
        final App app = factory.create(new InputReceiver() {
            @Override
            public String receive() {
                return inputs.poll();
            }
        }, new ResourceProvider() {
            @Override
            public Reader open(String name) throws ResourceProvider.FailedException {
                return null;
            }
        }, new StringPrinter() {
            @Override
            public void print(String s) {
            }
        }, new MessagePrinter() {
            @Override
            public void printMessage(String message) {
            }
        });

        app.run();
    }

    // 定義されていないコマンドが入力された場合、エラーメッセージを出力して入力待ちに戻る
    @Test
    public void runOutputErrorMessageWhenInputIsUnknownCommand() {
        final LinkedList<String> inputs = new LinkedList<String>(List.of(":sample", ":q"));
        final LinkedList<String> messages = new LinkedList<String>();
        final AppFactory factory = new AppFactory();
        final App app = factory.create(new InputReceiver() {
            @Override
            public String receive() {
                return inputs.poll();
            }
        }, new ResourceProvider() {
            @Override
            public Reader open(String name) throws ResourceProvider.FailedException {
                return null;
            }
        }, new StringPrinter() {
            @Override
            public void print(String s) {
            }
        }, new MessagePrinter() {
            @Override
            public void printMessage(String message) {
                messages.add(message);
            }
        });

        app.run();

        assertThat(messages.getFirst(), is("unknown command ':sample'"));
    }
}
