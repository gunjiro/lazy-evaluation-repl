package io.github.gunjiro.hj;

import io.github.gunjiro.hj.command.CommandAnalyzer;

public class CommandRequestAction {
    private final CommandAnalyzer analyzer;
    private final CommandOperator operator;

    public CommandRequestAction(CommandAnalyzer analyzer, CommandOperator operator) {
        this.analyzer = analyzer;
        this.operator = operator;
    }

    public void take(Environment environment, CommandRequest request) throws ExitException {
        operator.operate(environment, analyzer.analyze(request.getInput()));
    }
}
