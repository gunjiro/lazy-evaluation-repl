public class IOLoop {
    private final InputReceiver receiver;
    private final RequestFactory factory;
    private final Executor executor;

    public IOLoop(InputReceiver receiver, RequestFactory factory, Executor executor) {
        this.receiver = receiver;
        this.factory = factory;
        this.executor = executor;
    }

    public void loop() {
        try {
            final Environment environment = new DefaultEnvironment();
            while (true) {
                String input = receiver.receive();
                Request request = factory.createRequest(input);
                executor.execute(environment, request);
            }
        } catch (ExitException e) {
        }
    }
}
