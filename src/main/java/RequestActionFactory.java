public class RequestActionFactory {
    public EvaluationRequestAction createEvaluationRequestAction() {
        return new EvaluationRequestAction(createValuePrinter(), new SystemOutMessagePrinter());
    }

    public CommandRequestAction createCommandRequestAction() {
        return new CommandRequestAction(new CommandAnalyzer(), createCommandOperator());
    }

    private ValuePrinter createValuePrinter() {
        return new ValuePrinter(new SystemOutStringPrinter());
    }

    private CommandOperator createCommandOperator() {
        return new CommandOperator(createLoadAction(), new SystemOutMessagePrinter());
    }

    private LoadAction createLoadAction() {
        return new LoadAction(new FileResourceProvider(), new SystemOutMessagePrinter());
    }
}
