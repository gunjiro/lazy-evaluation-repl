package io.github.gunjiro.hj;

import io.github.gunjiro.hj.command.Command;
import io.github.gunjiro.hj.command.EmptyCommand;
import io.github.gunjiro.hj.command.LoadCommand;
import io.github.gunjiro.hj.command.QuitCommand;
import io.github.gunjiro.hj.command.UnknownCommand;
import io.github.gunjiro.hj.command.action.LoadCommandAction;
import io.github.gunjiro.hj.command.action.QuitCommandAction;

public class AppCommandOperator implements CommandOperator {
    public static interface Implementor {
        public void showMessage(String message);
        public void load(String name);
    }

    private final Implementor implementor;

    public AppCommandOperator(Implementor implementor) {
        this.implementor = implementor;
    }

    private AppCommandOperator(ResourceProvider provider, Implementor implementor) {
        this.implementor = implementor;
    }

    public static CommandOperator create(ResourceProvider provider, AppCommandOperator.Implementor implementor) {
        return new AppCommandOperator(provider, implementor);
    }

    @Override
    public void operate(Environment environment, Command command) throws ExitException {
        command.accept(new Command.Visitor<Void>() {

            @Override
            public Void visit(EmptyCommand command) {
                return null;
            }

            @Override
            public Void visit(QuitCommand command) throws ExitException {
                operate(command);
                return null;
            }

            @Override
            public Void visit(LoadCommand command) {
                operate(command);
                return null;
            }

            @Override
            public Void visit(UnknownCommand command) {
                operate(command);
                return null;
            }
        });
    }

    private void operate(QuitCommand command) throws ExitException {
        createQuitCommandAction().take(command);
    }

    private void operate(LoadCommand command) {
        createLoadCommandAction().take(command);
    }

    private void operate(UnknownCommand command) {
        createUnknownCommandAction().take(command);
    }

    private QuitCommandAction createQuitCommandAction() {
        return new QuitCommandAction();
    }

    private LoadCommandAction createLoadCommandAction() {
        return new LoadCommandAction(new LoadCommandAction.Implementor() {
            @Override
            public void load(String name) {
                implementor.load(name);
            }
        });
    }

    private UnknownCommandAction createUnknownCommandAction() {
        return new UnknownCommandAction(new UnknownCommandAction.Implementor() {

            @Override
            public void showMessage(String message) {
                implementor.showMessage(message);
            }

        });
    }
}
