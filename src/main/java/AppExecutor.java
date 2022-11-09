public class AppExecutor implements Executor {
    @Override
    public void execute(Request request) throws ExitException {
        request.send();
    }
}
