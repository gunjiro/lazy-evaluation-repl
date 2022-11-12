public class RequestOperator {
    private final CommandAnalyzer commandAnalyzer;
    private final CommandOperator commandOperator;
    private final EvaluationRequestAction evalAction;

    public RequestOperator(CommandAnalyzer commandAnalyzer, CommandOperator commandOperator, EvaluationRequestAction evalAction) {
        this.commandAnalyzer = commandAnalyzer;
        this.commandOperator = commandOperator;
        this.evalAction = evalAction;
    }

    public void operate(Request request) throws ExitException {
        request.accept(new Request.Visitor<Void>() {
            @Override
            public Void visit(EmptyRequest request) {
                return null;
            }

            @Override
            public Void visit(CommandRequest request) throws ExitException {
                request.extract(new CommandRequest.Operation<Void>() {
                    @Override
                    public Void apply(Environment environment, String input) throws ExitException {
                        commandOperator.operate(environment, commandAnalyzer.analyze(input));
                        return null;
                    }
                });
                return null;
            }

            @Override
            public Void visit(EvaluationRequest request) {
                evalAction.take(request);
                return null;
            }
        });
    }
}
