package io.github.gunjiro.hj.app;

import java.io.Console;
import java.io.IOException;

public class ConsoleHelper {
    public String getInput() throws IOException {
        return readLine();
    }

    private String readLine() throws IOException {
        final String line = getConsole().readLine("> ");

        if (line == null) {
            throw new IOException("An end of stream has been reached.");
        }

        return line;
    }

    private Console getConsole() throws IOException {
        final Console console = System.console();

        if (console == null) {
            throw new IOException("No console device is available.");
        }

        return console;
    }
}
