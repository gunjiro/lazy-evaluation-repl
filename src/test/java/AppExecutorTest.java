import org.junit.Test;

public class AppExecutorTest {
    // :qで終了
    @Test(expected = ExitException.class)
    public void executeShouldExitByQuitCommand() throws ExitException {
        final Environment environment = new DefaultEnvironment();
        final Request request = new CommandRequest(":q");
        final Executor executor = new AppExecutor();
        executor.execute(environment, request);
    }
}
