public class AppExecutor implements Executor {
    @Override
    public void execute(Environment environment, Request request) throws ExitException {
        RequestOperator.create(AppCommandOperator.create(new FileResourceProvider(), new SystemOutMessagePrinter()), new SystemOutStringPrinter(), new SystemOutMessagePrinter()).operate(environment, request);
    }
}
