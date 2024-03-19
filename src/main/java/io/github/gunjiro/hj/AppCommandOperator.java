package io.github.gunjiro.hj;

import java.io.IOError;
import java.io.IOException;
import java.io.Reader;

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

    private void operate(QuitCommand command) throws ExitException{
        createQuitCommandAction().take(command);
    }

    private void operate(UnknownCommand command) {
        createUnknownCommandAction().take(command);
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
            operate(command);
            return null;
        }

        @Override
        public Void visit(LoadCommand command) {
            createLoadCommandAction(environment).take(command);
            return null;
        }

        @Override
        public Void visit(UnknownCommand command) {
            operate(command);
            return null;
        }
    }

    private QuitCommandAction createQuitCommandAction() {
        return new QuitCommandAction();
    }

    private LoadCommandAction createLoadCommandAction(Environment environment) {
        return new LoadCommandAction(new LoadCommandAction.Implementor() {
            @Override
            public void load(String name) {
                try (Reader reader = provider.open(name)) {
                    environment.addFunctions(reader);
                    implementor.showMessage("loaded: " + name);
                } catch (ResourceProvider.FailedException e) {
                    implementor.showMessage(e.getMessage());
                } catch (ApplicationException e) {
                    implementor.showMessage(e.getMessage());
                } catch (IOException e) {
                    throw new IOError(e);
                }
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
