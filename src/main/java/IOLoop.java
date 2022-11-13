public class IOLoop {
    private final InputReceiver receiver;
    private final RequestFactory factory;
    private final RequestOperator operator;

    public IOLoop(InputReceiver receiver, RequestFactory factory, RequestOperator operator) {
        this.receiver = receiver;
        this.factory = factory;
        this.operator = operator;
    }

    public void loop() {
        try {
            final Environment environment = new DefaultEnvironment();
            while (true) {
                String input = receiver.receive();
                Request request = factory.createRequest(input);
                operator.operate(environment, request);
            }
        } catch (ExitException e) {
        }
    }
}
