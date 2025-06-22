package io.github.gunjiro.hj.tester;

import java.io.Console;
import java.io.IOError;
import java.io.IOException;

import io.github.gunjiro.hj.app.ConsoleHelper;

public class ConsoleHelperTester {
    private final ConsoleHelper helper = new ConsoleHelper();

    private void throwsIOErrorIfConsoleIsNull() {
        final ConsoleHelper noConsoleHelper = new ConsoleHelper() {
            @Override
            protected Console getConsoleOrNull() {
                return null;
            }
        };

        helper.output("throws IOError if Console is null : ");

        try {
            noConsoleHelper.getConsole();
            helper.output("!!!!! Not Throw IOError !!!!!");
            helper.newline();
        } catch (IOError error) {
            helper.output("OK");
            helper.newline();
        }
    }

    private void throwsIOExceptionIfReadLineReturnsNull() {
        final ConsoleHelper nullReadLineHelper = new ConsoleHelper() {
            @Override
            protected String readLine() {
                return null;
            }
        };

        helper.output("throws IOException if readLine returns null : ");

        try {
            nullReadLineHelper.getInput();
            helper.output("!!!!! Not Throw IOException !!!!!");
            helper.newline();
        } catch (IOException e) {
            helper.output("OK");
            helper.newline();
        }
    }

    private static void test() {
        final ConsoleHelperTester tester = new ConsoleHelperTester();
        tester.outputStartMessage();
        tester.throwsIOErrorIfConsoleIsNull();
        tester.throwsIOExceptionIfReadLineReturnsNull();
    }

    private void outputStartMessage() {
        helper.output("----- Test of ConsoleHelper -----");
        helper.newline();
    }

    public static void main(String[] args) {
        test();
    }
}
