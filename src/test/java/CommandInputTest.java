import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

public class CommandInputTest {
    // 空文字ならIllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void createShouldThrowIllegalArgumentExceptionWhenInputIsEmptyString() {
        final String input = "";

        CommandInput.create(input);
    }

    // 1文字目が:でなければIllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void createShouldThrowIllegalArgumentExceptionWhenInputIsNotCommandString() {
        final String input = "sample";

        CommandInput.create(input);
    }

    // スペース区切りで入力文字列を分割
    @Test
    public void createShouldSplitInputStringWithSpace() {
        final String input = ":load sample1 sample2";

        CommandInput.create(input).extract(new CommandInput.Operation<Void>() {
            @Override
            public Void apply(String command, List<String> arguments) {
                assertThat(command, is(":load"));
                assertThat(arguments, is(List.of("sample1", "sample2")));
                return null;
            }
        });
    }
}
