import org.junit.Test;

public class AppExecutorTest {
    // :qで終了
    @Test(expected = ExitException.class)
    public void executeShouldExitByQuitCommand() throws ExitException {
        final Request request = new CommandRequest(new DefaultEnvironment(), ":q");
        final Executor executor = new AppExecutor();
        executor.execute(request);
    }
}
