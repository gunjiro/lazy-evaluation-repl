import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EvalActionTest {
    // 空文字の場合は何もしない
    @Test
    public void applyNotDoAnythingWhenCodeIsEmptyString() {
        final EvalAction action = new EvalAction(null, null);

        action.apply(null, "");
    }

    // 式の場合は評価して出力する
    @Test
    public void applyShouldOutputValueWhenCodeIsExpressionString() throws ApplicationException {
        final Reader reader = new StringReader("two = 1 + 1");
        final String code = "two";
        final Environment environment = new DefaultEnvironment();

        environment.addFunctions(reader);

        final StringBuilder builder = new StringBuilder();
        final EvalAction action = new EvalAction(new ValuePrinter(new StringPrinter() {
            @Override
            public void print(String s) {
                builder.append(s);
            }
        }), new MessagePrinter() {
            @Override
            public void printMessage(String message) {
            }
        });

        action.apply(environment, code);

        assertThat(builder.toString(), is("2"));
    }

    // 評価に失敗した場合はエラーメッセージを出力する
    @Test
    public void applyShouldOutputErrorMessageWhenEvaluationIsFailed() throws ApplicationException {
        final String code = "1 + (1:[])";
        final Environment environment = new DefaultEnvironment();

        final StringBuilder builder = new StringBuilder();
        final EvalAction action = new EvalAction(new ValuePrinter(new StringPrinter() {
            @Override
            public void print(String s) {
            }
        }), new MessagePrinter() {
            @Override
            public void printMessage(String message) {
                builder.append(message);
            }
        });

        action.apply(environment, code);

        assertThat(builder.toString(), is(not("")));
    }

    // 構文間違いの場合はエラーメッセージを出力する
    @Test
    public void applyShouldOutputErrorMessageWhenCodeIsInvalid() throws ApplicationException {
        final String code = "#$%";
        final Environment environment = new DefaultEnvironment();

        final StringBuilder builder = new StringBuilder();
        final EvalAction action = new EvalAction(new ValuePrinter(new StringPrinter() {
            @Override
            public void print(String s) {
            }
        }), new MessagePrinter() {
            @Override
            public void printMessage(String message) {
                builder.append(message);
            }
        });

        action.apply(environment, code);

        assertThat(builder.toString(), is(not("")));
    }
}
