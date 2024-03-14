package io.github.gunjiro.hj;

import io.github.gunjiro.hj.command.UnknownCommand;

public class UnknownCommandAction {
    public static interface Implementor {
        public void showMessage(String message);
    }

    private final Implementor implementor;

    public UnknownCommandAction(Implementor implementor) {
        this.implementor = implementor; 
    }

    public void take(UnknownCommand command) {
        implementor.showMessage(String.format("unknown command '%s'", command.getCommandName()));
    }
}
