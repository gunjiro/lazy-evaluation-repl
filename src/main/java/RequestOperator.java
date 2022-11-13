public class RequestOperator {
    private final RequestActionFactory factory;
    private final CommandOperator operator;
    private final StringPrinter strinngPrinter;
    private final MessagePrinter messagePrinter;

    private RequestOperator(RequestActionFactory factory, CommandOperator operator, StringPrinter strinngPrinter,
            MessagePrinter messagePrinter) {
        this.factory = factory;
        this.operator = operator;
        this.strinngPrinter = strinngPrinter;
        this.messagePrinter = messagePrinter;
    }

    public static RequestOperator create(CommandOperator operator, StringPrinter strinngPrinter, MessagePrinter messagePrinter) {
        return new RequestOperator(new RequestActionFactory(), operator, strinngPrinter, messagePrinter);
    }

    public void operate(Environment environment, Request request) throws ExitException {
        request.accept(new Request.Visitor<Void>() {
            @Override
            public Void visit(EmptyRequest request) {
                return null;
            }

            @Override
            public Void visit(CommandRequest request) throws ExitException {
                factory.createCommandRequestAction(operator).take(environment, request);
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
