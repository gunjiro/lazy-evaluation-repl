public class AppExecutor implements Executor {
    @Override
    public void execute(Environment environment, Request request) throws ExitException {
        AppRequestOperator.create(new FileResourceProvider(), new SystemOutStringPrinter(), new SystemOutMessagePrinter()).operate(environment, request);
    }
}
