package io.github.gunjiro.hj;

import io.github.gunjiro.hj.command.CommandAnalyzer;

public class RequestActionFactory {
    public EvaluationRequestAction createEvaluationRequestAction(StringPrinter stringPrinter, MessagePrinter messagePrinter) {
        return new EvaluationRequestAction(new ValuePrinter(stringPrinter), messagePrinter);
    }

    public CommandRequestAction createCommandRequestAction(ResourceProvider provider, MessagePrinter printer, AppCommandOperator.Implementor implementor) {
        return new CommandRequestAction(new CommandAnalyzer(), new AppCommandOperator(implementor));
    }
}
