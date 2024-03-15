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
    }

    private final ResourceProvider provider;
    private final Implementor implementor;

    private AppCommandOperator(ResourceProvider provider, Implementor implementor) {
        this.provider = provider;
        this.implementor = implementor;
    }

    public static CommandOperator create(ResourceProvider provider, AppCommandOperator.Implementor implementor) {
        return new AppCommandOperator(provider, implementor);
    }

    @Override
    public void operate(Environment environment, Command command) throws ExitException {
        command.accept(new OperationCommandVisitor(environment));
    }

    public class OperationCommandVisitor implements Command.Visitor<Void> {
        private final Environment environment;

        public OperationCommandVisitor(Environment environment) {
            this.environment = environment;
        }

        @Override
        public Void visit(EmptyCommand command) {
            return null;
        }

        @Override
        public Void visit(QuitCommand command) throws ExitException {
            createQuitCommandAction().take(command);
            return null;
        }

        @Override
        public Void visit(LoadCommand command) {
            createLoadCommandAction().take(environment, command);
            return null;
        }

        @Override
        public Void visit(UnknownCommand command) {
            createUnknownCommandAction().take(command);
            return null;
        }
    }

    private QuitCommandAction createQuitCommandAction() {
        return new QuitCommandAction();
    }

    private LoadCommandAction createLoadCommandAction() {
        return new LoadCommandAction(provider, new LoadCommandAction.Implementor() {
            @Override
            public void showMessage(String message) {
                implementor.showMessage(message);
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
