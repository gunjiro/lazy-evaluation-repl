package io.github.gunjiro;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EvaluationRequestActionTest {
    // 空文字の場合は何もしない
    @Test
    public void takeNotDoAnythingWhenCodeIsEmptyString() {
        final String code = "";
        final EvaluationRequestAction action = new EvaluationRequestAction(null, null);

        action.take(null, new EvaluationRequest(code));
    }

    // 式の場合は評価して出力する
    @Test
    public void takeShouldOutputValueWhenCodeIsExpressionString() throws ApplicationException {
        final Reader reader = new StringReader("two = 1 + 1");
        final String code = "two";
        final Environment environment = new DefaultEnvironment();

        environment.addFunctions(reader);

        final StringBuilder builder = new StringBuilder();
        final EvaluationRequestAction action = new EvaluationRequestAction(new ValuePrinter(new StringPrinter() {
            @Override
            public void print(String s) {
                builder.append(s);
            }
        }), new MessagePrinter() {
            @Override
            public void printMessage(String message) {
            }
        });

        action.take(environment, new EvaluationRequest(code));

        assertThat(builder.toString(), is("2"));
    }

    // 評価に失敗した場合はエラーメッセージを出力する
    @Test
    public void takeShouldOutputErrorMessageWhenEvaluationIsFailed() throws ApplicationException {
        final String code = "1 + (1:[])";
        final Environment environment = new DefaultEnvironment();

        final StringBuilder builder = new StringBuilder();
        final EvaluationRequestAction action = new EvaluationRequestAction(new ValuePrinter(new StringPrinter() {
            @Override
            public void print(String s) {
            }
        }), new MessagePrinter() {
            @Override
            public void printMessage(String message) {
                builder.append(message);
            }
        });

        action.take(environment, new EvaluationRequest(code));

        assertThat(builder.toString(), is(not("")));
    }

    // 構文間違いの場合はエラーメッセージを出力する
    @Test
    public void takeShouldOutputErrorMessageWhenCodeIsInvalid() throws ApplicationException {
        final String code = "#$%";
        final Environment environment = new DefaultEnvironment();

        final StringBuilder builder = new StringBuilder();
        final EvaluationRequestAction action = new EvaluationRequestAction(new ValuePrinter(new StringPrinter() {
            @Override
            public void print(String s) {
            }
        }), new MessagePrinter() {
            @Override
            public void printMessage(String message) {
                builder.append(message);
            }
        });

        action.take(environment, new EvaluationRequest(code));

        assertThat(builder.toString(), is(not("")));
    }
}
