package io.github.gunjiro.hj.app;

import java.io.Console;
import java.io.IOError;
import java.io.IOException;

public class ConsoleHelper {
    private final ConsoleEmulator emulator;

    public ConsoleHelper(ConsoleEmulator emulator) {
        this.emulator = emulator;
    }

    public static ConsoleHelper create(Console console) {
        if (console == null) {
            throw new IOError(new IOException("No console device is available."));
        }

        return new ConsoleHelper(new ConsoleEmulator() {

            @Override
            public String readLine() {
                return console.readLine("> ");
            }

            @Override
            public void print(String s) {
                console.writer().print(s);
            }

            @Override
            public void println() {
                console.writer().println();
            }
            
        });
    }

    public static ConsoleHelper create() {
        return create(System.console());
    }

    public static interface ConsoleEmulator {
        public String readLine();
        public void print(String s);
        public void println();
    }

    public void output(String text) {
        emulator.print(text);
    }

    public void newline() {
        emulator.println();
    }

    public String getInput() throws IOException {
        return readLineOrThrowIOException();
    }

    private String readLineOrThrowIOException() throws IOException {
        final String line = emulator.readLine();

        if (line == null) {
            throw new IOException("An end of stream has been reached.");
        }

        return line;
    }

}
