package io.github.gunjiro.hj.tester;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOError;
import java.io.IOException;

import io.github.gunjiro.hj.app.ConsoleHelper;

public class ConsoleHelperTester {
    private final ConsoleHelper helper = ConsoleHelper.create();

    private void throwsIOErrorIfConsoleIsNull() {
        helper.output("throws IOError if Console is null : ");

        try {
            ConsoleHelper.create(null);
            helper.output("!!!!! Not Throw IOError !!!!!");
            helper.newline();
        } catch (IOError error) {
            helper.output("OK");
            helper.newline();
        }
    }

    private void throwsIOExceptionIfReadLineReturnsNull() {
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
