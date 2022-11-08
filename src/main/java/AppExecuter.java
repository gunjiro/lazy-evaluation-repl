public class AppExecuter implements Executer {
    @Override
    public void execute(Request request) throws ExitException {
        request.send();
    }
}
