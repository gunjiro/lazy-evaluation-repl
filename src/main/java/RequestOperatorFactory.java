public class RequestOperatorFactory {
    public RequestOperator create() {
        final MessagePrinter messagePrinter = messagePrinter();
        return new RequestOperator(new CommandAnalyzer(), commandOperator(messagePrinter), evalAction(messagePrinter));
    }

    private MessagePrinter messagePrinter() {
        return new SystemOutMessagePrinter();
    }

    private CommandOperator commandOperator(MessagePrinter messagePrinter) {
        return new CommandOperator(loadAction(messagePrinter), messagePrinter);
    }

    private LoadAction loadAction(MessagePrinter messagePrinter) {
        return new LoadAction(new FileResourceProvider(), messagePrinter);
    }

    private EvalAction evalAction(MessagePrinter messagePrinter) {
        return new EvalAction(valuePrinter(), messagePrinter);
    }

    private ValuePrinter valuePrinter() {
        return new ValuePrinter(new SystemOutStringPrinter());
    }
}
