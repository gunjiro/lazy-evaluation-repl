public class AppFactory {
    public App create() {
        return new App(createIOLoop(), new MessagePrinter());
    }

    private IOLoop createIOLoop() {
        return new IOLoop(SystemInInputReceiver.create(), AppExecuter.create());
    }
}
