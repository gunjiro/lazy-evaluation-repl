public class AppExecutor implements Executor {
    @Override
    public void execute(Request request) throws ExitException {
        RequestOperator.create().operate(request);
    }
}
