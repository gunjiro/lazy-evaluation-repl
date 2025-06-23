package io.github.gunjiro.hj.request;

import io.github.gunjiro.hj.CommandRequest;
import io.github.gunjiro.hj.command.CommandAnalyzer;
import io.github.gunjiro.hj.command.executor.CommandExecutor;

public class CommandRequestOperator {
    private final Implementor implementor;

    public CommandRequestOperator(Implementor implementor) {
        this.implementor = implementor;
    }

    public static interface Implementor {
        public void load(String filename);
        public void stopApplication();
        public void output(String text);
        public void newline();
    }

    public void operate(CommandRequest request) {
        final CommandExecutor executor = new CommandExecutor(new CommandExecutor.Implementor() {

            @Override
            public void load(String name) {
                CommandRequestOperator.this.load(name);
            }

            @Override
            public void quit() {
                CommandRequestOperator.this.quit();
            }

        });

        executor.addObserver(notification -> {
            if (notification instanceof CommandExecutor.CommandIsUnknown) {
                output(String.format("unknown command '%s'", ((CommandExecutor.CommandIsUnknown)notification).getCommand()));
                newline();
            }
        });

        final CommandAnalyzer analyzer = new CommandAnalyzer();
        executor.execute(analyzer.analyze(request.getInput()));
    }

    private void load(String name) {
        implementor.load(name);
    }

    private void quit() {
        implementor.stopApplication();
    }

    private void output(String text) {
        implementor.output(text);
    }

    private void newline() {
        implementor.newline();
    }

}
