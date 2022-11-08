import org.junit.Test;

public class AppExecuterTest {
    // :qで終了
    @Test(expected = ExitException.class)
    public void executeShouldExitByQuitCommand() throws ExitException {
        final Environment environment = new DefaultEnvironment();
        final Executer executer = AppExecuter.create();
        executer.execute(environment, ":q");
    }
}
