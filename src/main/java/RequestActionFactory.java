public class RequestActionFactory {
    public EvaluationRequestAction createEvaluationRequestAction() {
        return new EvaluationRequestAction(createValuePrinter(), new SystemOutMessagePrinter());
    }

    public CommandRequestAction createCommandRequestAction() {
        return new CommandRequestAction(new CommandAnalyzer(), CommandOperator.create(new FileResourceProvider(), new SystemOutMessagePrinter()));
    }

    private ValuePrinter createValuePrinter() {
        return new ValuePrinter(new SystemOutStringPrinter());
    }
}
