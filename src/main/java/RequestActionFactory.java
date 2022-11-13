public class RequestActionFactory {
    public EvaluationRequestAction createEvaluationRequestAction(StringPrinter stringPrinter, MessagePrinter messagePrinter) {
        return new EvaluationRequestAction(new ValuePrinter(stringPrinter), messagePrinter);
    }

    public CommandRequestAction createCommandRequestAction(CommandOperator operator) {
        return new CommandRequestAction(new CommandAnalyzer(), operator);
    }
}
