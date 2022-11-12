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
        return new CommandOperator(createLoadCommandAction(), new SystemOutMessagePrinter());
    }

    private LoadCommandAction createLoadCommandAction() {
        return new LoadCommandAction(new FileResourceProvider(), new SystemOutMessagePrinter());
    }
}
