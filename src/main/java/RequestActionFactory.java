public class RequestActionFactory {
    public EvaluationRequestAction createEvaluationRequestAction(StringPrinter stringPrinter, MessagePrinter messagePrinter) {
        return new EvaluationRequestAction(new ValuePrinter(stringPrinter), messagePrinter);
    }

    public CommandRequestAction createCommandRequestAction(ResourceProvider provider, MessagePrinter printer) {
        return new CommandRequestAction(new CommandAnalyzer(), AppCommandOperator.create(provider, printer));
    }
}
