import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class CommandAnalyzerTest {
    // １文字目が:じゃないならIllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void analyzeShouldThrowIllegalArgumentExceptionWhenInvalidStringIsReceived() {
        final String input = "not start :";
        final Environment environment = new DefaultEnvironment();
        final CommandAnalyzer analyzer = new CommandAnalyzer();

        analyzer.analyze(environment, input);
    }

    // 空文字ならIllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void analyzeShouldThrowIllegalArgumentExceptionWhenEmptyStringIsReceived() {
        final String input = "";
        final Environment environment = new DefaultEnvironment();
        final CommandAnalyzer analyzer = new CommandAnalyzer();

        analyzer.analyze(environment, input);
    }

    // :ならEmptyCommand
    @Test()
    public void analyzeShouldReturnEmptyCommandWhenEmptyCommandIsReceived() {
        final String input = ":";
        final Environment environment = new DefaultEnvironment();
        final CommandAnalyzer analyzer = new CommandAnalyzer();

        assertThat(analyzer.analyze(environment, input), is(instanceOf(EmptyCommand.class)));
    }

    // :quitならQuitCommand
    @Test()
    public void analyzeShouldReturnQuitCommandWhenEmptyCommandIsReceived() {
        final String input = ":quit";
        final Environment environment = new DefaultEnvironment();
        final CommandAnalyzer analyzer = new CommandAnalyzer();

        assertThat(analyzer.analyze(environment, input), is(instanceOf(QuitCommand.class)));
    }

    // :loadならLoadCommand
    @Test()
    public void analyzeShouldReturnLoadCommandWhenLoadCommandIsReceived() {
        final String input = ":load";
        final Environment environment = new DefaultEnvironment();
        final CommandAnalyzer analyzer = new CommandAnalyzer();

        assertThat(analyzer.analyze(environment, input), is(instanceOf(LoadCommand.class)));
    }

    // 該当するコマンドが無い場合UnknownCommand
    @Test()
    public void analyzeShouldReturnUnknownCommandWhenUnknownCommandIsReceived() {
        final String input = ":unknown";
        final Environment environment = new DefaultEnvironment();
        final CommandAnalyzer analyzer = new CommandAnalyzer();

        assertThat(analyzer.analyze(environment, input), is(instanceOf(UnknownCommand.class)));
    }
}
