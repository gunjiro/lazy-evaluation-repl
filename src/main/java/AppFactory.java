public class AppFactory {
    public App create() {
        return new App(createIOLoop(), new SystemOutMessagePrinter());
    }

    private IOLoop createIOLoop() {
        return new IOLoop(SystemInInputReceiver.create(), new RequestFactory(), AppRequestOperator.create(new FileResourceProvider(), new SystemOutStringPrinter(), new SystemOutMessagePrinter()));
    }
}
