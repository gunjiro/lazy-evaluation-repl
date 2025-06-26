package io.github.gunjiro.hj.app;

import java.io.Console;
import java.io.IOError;
import java.io.IOException;

public class ConsoleHelper {
    public static interface Implementor {
        public ConsoleEmulator console();
    }

    public static interface ConsoleEmulator {
        public String readLine();
    }

    public void output(String text) {
        getConsole().writer().print(text);
    }

    public void newline() {
        getConsole().writer().println();
    }

    public String getInput() throws IOException {
        return readLineOrThrowIOException();
    }

    public Console getConsole() {
        try {
            return getConsoleOrThrowIOException();
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

    protected String readLine() {
        return getConsole().readLine("> ");
    }

    protected Console getConsoleOrNull() {
        return System.console();
    }

    private Console getConsoleOrThrowIOException() throws IOException {
        final Console console = getConsoleOrNull();

        if (console == null) {
            throw new IOException("No console device is available.");
        }

        return console;
    }

    private String readLineOrThrowIOException() throws IOException {
        final String line = readLine();

        if (line == null) {
            throw new IOException("An end of stream has been reached.");
        }

        return line;
    }

}
