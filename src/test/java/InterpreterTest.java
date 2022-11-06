import org.junit.Test;

public class InterpreterTest {
    // :qで終了
    @Test
    public void executeShouldExitByQuitCommand() {
        final InputReceiver receiver = new InputReceiver() {
            @Override
            public String receive() {
                return ":q";
            }
        };
        final Interpreter interpreter = new Interpreter(receiver);

        interpreter.execute();
    }
}
