package io.github.gunjiro;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class CommandNameTest {
    // 空文字ならIllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void matchesShouldThrowIllegalArgumentExceptionWhenInputIsEmptyString() {
        final String input = "";
        final CommandName name = new CommandName(":sample");

        name.matches(input);
    }

    // 完全一致でtrue
    @Test
    public void matchesShouldReturnTrueWhenInputEqualsCommandName() {
        final String input = ":sample";
        final CommandName name = new CommandName(":sample");

        assertThat(name.matches(input), is(true));
    }

    // 前方一致でtrue
    @Test
    public void matchesShouldReturnTrueWhenInputIsAForepartOfCommandName() {
        final String input = ":sa";
        final CommandName name = new CommandName(":sample");

        assertThat(name.matches(input), is(true));
    }

    // 不一致でfalse
    @Test
    public void matchesShouldReturnFalseWhenInputNotMatchCommandName() {
        final String input = ":test";
        final CommandName name = new CommandName(":sample");

        assertThat(name.matches(input), is(false));
    }
}
