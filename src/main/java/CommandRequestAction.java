public class CommandRequestAction {
    private final CommandAnalyzer analyzer;
    private final CommandOperator operator;

    public CommandRequestAction(CommandAnalyzer analyzer, CommandOperator operator) {
        this.analyzer = analyzer;
        this.operator = operator;
    }

    public void take(CommandRequest request) throws ExitException {
        request.extract(new CommandRequest.Operation<Void>() {
            @Override
            public Void apply(Environment environment, String input) throws ExitException {
                operator.operate(environment, analyzer.analyze(input));
                return null;
            }
        });
    }
}
