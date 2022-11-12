public class RequestOperator {
    private final RequestActionFactory factory;

    private RequestOperator(RequestActionFactory factory) {
        this.factory = factory;
    }

    public static RequestOperator create() {
        return new RequestOperator(new RequestActionFactory());
    }

    public void operate(Environment environment, Request request) throws ExitException {
        request.accept(new Request.Visitor<Void>() {
            @Override
            public Void visit(EmptyRequest request) {
                return null;
            }

            @Override
            public Void visit(CommandRequest request) throws ExitException {
                factory.createCommandRequestAction().take(request);
                return null;
            }

            @Override
            public Void visit(EvaluationRequest request) {
                factory.createEvaluationRequestAction().take(request);
                return null;
            }
        });
    }
}
