package io.github.gunjiro.hj.app;

import java.io.Console;
import java.io.IOError;
import java.io.IOException;

public class ConsoleHelper {
    private final ConsoleEmulator emulator;

    public ConsoleHelper(ConsoleEmulator emulator) {
        this.emulator = emulator;
    }

    public static interface ConsoleEmulator {
        public String readLine();
        public void print(String s);
        public void println();
    }

    private static class SystemConsoleEmulator implements ConsoleEmulator {
        private final Console consoleOrNull;

        public SystemConsoleEmulator(Console consoleOrNull) {
            this.consoleOrNull = consoleOrNull;
        }

        @Override
        public String readLine() {
            return getConsole().readLine("> ");
        }

        @Override
        public void print(String s) {
            getConsole().writer().print(s);
        }

        @Override
        public void println() {
            getConsole().writer().println();
        }

        private Console getConsole() {
            if (consoleOrNull == null) {
                throw new IOError(new IOException("No console device is available."));
            }

            return consoleOrNull;
        }

    }

    public static ConsoleHelper create(Console console) {
        return new ConsoleHelper(new SystemConsoleEmulator(console));
    }

    public static ConsoleHelper create() {
        return create(System.console());
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
