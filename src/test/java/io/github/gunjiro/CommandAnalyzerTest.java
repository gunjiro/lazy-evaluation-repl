package io.github.gunjiro;
import static org.hamcrest.Matchers.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class CommandAnalyzerTest {
    // １文字目が:じゃないならIllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void analyzeShouldThrowIllegalArgumentExceptionWhenInvalidStringIsReceived() {
        final String input = "not start :";
        final CommandAnalyzer analyzer = new CommandAnalyzer();

        analyzer.analyze(input);
    }

    // 空文字ならIllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void analyzeShouldThrowIllegalArgumentExceptionWhenEmptyStringIsReceived() {
        final String input = "";
        final CommandAnalyzer analyzer = new CommandAnalyzer();

        analyzer.analyze(input);
    }

    // :ならEmptyCommand
    @Test()
    public void analyzeShouldReturnEmptyCommandWhenEmptyCommandIsReceived() {
        final String input = ":";
        final CommandAnalyzer analyzer = new CommandAnalyzer();

        assertThat(analyzer.analyze(input), is(instanceOf(EmptyCommand.class)));
    }

    // :quitならQuitCommand
    @Test()
    public void analyzeShouldReturnQuitCommandWhenEmptyCommandIsReceived() {
        final String input = ":quit";
        final CommandAnalyzer analyzer = new CommandAnalyzer();

        assertThat(analyzer.analyze(input), is(instanceOf(QuitCommand.class)));
    }

    // :loadならLoadCommand
    @Test()
    public void analyzeShouldReturnLoadCommandWhenLoadCommandIsReceived() {
        final String input = ":load sample1 sample2";
        final CommandAnalyzer analyzer = new CommandAnalyzer();

        final LoadCommand command = (LoadCommand)analyzer.analyze(input);

        assertThat(command.getResourceNames(), is(List.of("sample1", "sample2")));
    }

    // 該当するコマンドが無い場合UnknownCommand
    @Test()
    public void analyzeShouldReturnUnknownCommandWhenUnknownCommandIsReceived() {
        final String input = ":unknown";
        final CommandAnalyzer analyzer = new CommandAnalyzer();

        assertThat(analyzer.analyze(input), is(instanceOf(UnknownCommand.class)));
    }
}
