package io.github.gunjiro.hj;
public class IOLoop {
    private final RequestFactory factory;
    private final InputReceiver receiver;
    private final RequestOperator operator;

    private IOLoop(RequestFactory factory ,InputReceiver receiver, RequestOperator operator) {
        this.factory = factory;
        this.receiver = receiver;
        this.operator = operator;
    }

    public static IOLoop create(InputReceiver receiver, RequestOperator operator) {
        return new IOLoop(new RequestFactory(), receiver, operator);
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
