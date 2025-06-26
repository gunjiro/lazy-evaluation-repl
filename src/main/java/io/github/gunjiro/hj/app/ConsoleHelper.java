package io.github.gunjiro.hj.app;

import java.io.Console;
import java.io.IOError;
import java.io.IOException;

public class ConsoleHelper {
    private final Implementor implementor;

    public ConsoleHelper(Implementor implementor) {
        this.implementor = implementor;
    }

    public ConsoleHelper() {
        this(() -> (System.console() == null) ? null : new ConsoleEmulator() {

            @Override
            public String readLine() {
                return System.console().readLine("> ");
            }

            @Override
            public void print(String s) {
                System.console().writer().print(s);
            }

            @Override
            public void println() {
                System.console().writer().println();
            }
            
        });
    }

    public static interface Implementor {
        public ConsoleEmulator console();
    }

    public static interface ConsoleEmulator {
        public String readLine();
        public void print(String s);
        public void println();
    }

    public void output(String text) {
        getConsoleEmulator().print(text);
    }

    public void newline() {
        getConsoleEmulator().println();
    }

    public String getInput() throws IOException {
        return readLineOrThrowIOException();
    }

    private ConsoleEmulator getConsoleEmulator() {
        try {
            return getConsoleEmulatorOrThrowIOException();
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

    protected String readLine() {
        return getConsoleEmulator().readLine();
    }

    protected Console getConsoleOrNull() {
        return System.console();
    }

    private ConsoleEmulator getConsoleEmulatorOrNull() {
        return implementor.console();
    }

    private ConsoleEmulator getConsoleEmulatorOrThrowIOException() throws IOException {
        final ConsoleEmulator emulator = getConsoleEmulatorOrNull();

        if (emulator == null) {
            throw new IOException("No console device is available.");
        }

        return emulator;
    }

    private String readLineOrThrowIOException() throws IOException {
        final String line = readLine();

        if (line == null) {
            throw new IOException("An end of stream has been reached.");
        }

        return line;
    }

}
