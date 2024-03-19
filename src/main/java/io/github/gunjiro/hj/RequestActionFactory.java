package io.github.gunjiro.hj;

import io.github.gunjiro.hj.command.CommandAnalyzer;

public class RequestActionFactory {
    public EvaluationRequestAction createEvaluationRequestAction(StringPrinter stringPrinter, MessagePrinter messagePrinter) {
        return new EvaluationRequestAction(new ValuePrinter(stringPrinter), messagePrinter);
    }

    public CommandRequestAction createCommandRequestAction(ResourceProvider provider, MessagePrinter printer) {
        return new CommandRequestAction(new CommandAnalyzer(), AppCommandOperator.create(provider, new AppCommandOperator.Implementor() {

            @Override
            public void showMessage(String message) {
                printer.printMessage(message);
            }

            @Override
            public void load(String name) {
                throw new UnsupportedOperationException("Unimplemented method 'load'");
            }
            
        }));
    }

    public CommandRequestAction createCommandRequestAction(ResourceProvider provider, MessagePrinter printer, AppCommandOperator.Implementor implementor) {
        return new CommandRequestAction(new CommandAnalyzer(), AppCommandOperator.create(provider, implementor));
    }
}
