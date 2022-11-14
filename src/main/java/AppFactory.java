public class AppFactory {
    public App create(InputReceiver receiver, ResourceProvider provider, StringPrinter stringPrinter, MessagePrinter messagePrinter) {
        return new App(createIOLoop(receiver, provider, stringPrinter, messagePrinter), messagePrinter);
    }

    private IOLoop createIOLoop(InputReceiver receiver, ResourceProvider provider, StringPrinter stringPrinter, MessagePrinter messagePrinter) {
        return IOLoop.create(receiver, AppRequestOperator.create(provider, stringPrinter, messagePrinter));
    }
}
