package io.github.gunjiro.hj;
import org.junit.Test;

public class AppRequestOperatorTest {
    // :qで終了
    @Test(expected = ExitException.class)
    public void operateShouldExitByQuitCommand() throws ExitException {
        final Request request = new CommandRequest(":q");
        final RequestOperator operator = AppRequestOperator.create(null, null, null);
        operator.operate(new DefaultEnvironment(), request);
    }
    
}
