package io.github.gunjiro.hj.app;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOError;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

public class ConsoleHelperTest {

    @Test
    public void throwsIOErrorIfConsoleIsNull() {
        final Deque<String> messages = new LinkedList<>();

        try {
            ConsoleHelper.create(null).output("");
        } catch (IOError error) {
            messages.add(error.getMessage());
        }

        assertThat(messages, contains("java.io.IOException: No console device is available."));
    }

    @Test
    public void throwsIOExceptionIfReadLineReturnsNull() {
        final Deque<String> messages = new LinkedList<>();
        final ConsoleHelper nullReadLineHelper = new ConsoleHelper(new ConsoleHelper.ConsoleEmulator() {

            @Override
            public String readLine() {
                return null;
            }

            @Override
            public void print(String s) {
                throw new UnsupportedOperationException("Unimplemented method 'print'");
            }

            @Override
            public void println() {
                throw new UnsupportedOperationException("Unimplemented method 'println'");
            }

        });

        try {
            nullReadLineHelper.getInput();
        } catch (IOException e) {
            messages.add(e.getMessage());
        }

        assertThat(messages, contains("An end of stream has been reached."));
    }

}
