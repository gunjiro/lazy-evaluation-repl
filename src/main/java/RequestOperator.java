public class RequestOperator {
    private final RequestActionFactory factory;
    private final ResourceProvider provider;
    private final StringPrinter strinngPrinter;
    private final MessagePrinter messagePrinter;

    private RequestOperator(RequestActionFactory factory, ResourceProvider provider, StringPrinter strinngPrinter,
            MessagePrinter messagePrinter) {
        this.factory = factory;
        this.provider = provider;
        this.strinngPrinter = strinngPrinter;
        this.messagePrinter = messagePrinter;
    }

    public static RequestOperator create() {
        return new RequestOperator(new RequestActionFactory(), new FileResourceProvider(), new SystemOutStringPrinter(), new SystemOutMessagePrinter());
    }

    public void operate(Environment environment, Request request) throws ExitException {
        request.accept(new Request.Visitor<Void>() {
            @Override
            public Void visit(EmptyRequest request) {
                return null;
            }

            @Override
            public Void visit(CommandRequest request) throws ExitException {
                factory.createCommandRequestAction(provider, messagePrinter).take(environment, request);
                return null;
            }

            @Override
            public Void visit(EvaluationRequest request) {
                factory.createEvaluationRequestAction(strinngPrinter, messagePrinter).take(environment, request);
                return null;
            }
        });
    }
}
