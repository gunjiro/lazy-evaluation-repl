public class AppExecutor implements Executor {
    @Override
    public void execute(Request request) throws ExitException {
        factory().create().operate(request);
    }

    private RequestOperatorFactory factory() {
        return new RequestOperatorFactory();
    }
}
