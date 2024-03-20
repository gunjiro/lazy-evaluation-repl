package io.github.gunjiro.hj;

public class RequestActionFactory {
    public EvaluationRequestAction createEvaluationRequestAction(StringPrinter stringPrinter, MessagePrinter messagePrinter) {
        return new EvaluationRequestAction(new ValuePrinter(stringPrinter), messagePrinter);
    }
}
