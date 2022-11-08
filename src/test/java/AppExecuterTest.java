import org.junit.Test;

public class AppExecuterTest {
    // :qで終了
    @Test(expected = ExitException.class)
    public void executeShouldExitByQuitCommand() throws ExitException {
        final Request request = new CommandRequest(new DefaultEnvironment(), ":q");
        final Executer executer = AppExecuter.create();
        executer.execute(request);
    }
}
