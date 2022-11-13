public class AppExecutor implements Executor {
    @Override
    public void execute(Environment environment, Request request) throws ExitException {
        RequestOperator.create(new FileResourceProvider(), new SystemOutStringPrinter(), new SystemOutMessagePrinter()).operate(environment, request);
    }
}
