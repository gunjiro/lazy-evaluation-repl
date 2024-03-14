package io.github.gunjiro.hj;

import io.github.gunjiro.hj.command.Command;
import io.github.gunjiro.hj.command.EmptyCommand;
import io.github.gunjiro.hj.command.LoadCommand;
import io.github.gunjiro.hj.command.QuitCommand;
import io.github.gunjiro.hj.command.UnknownCommand;

public class AppCommandOperator implements CommandOperator {
    private final CommandActionFactory factory;
    private final ResourceProvider provider;
    private final MessagePrinter printer;

    private AppCommandOperator(CommandActionFactory factory, ResourceProvider provider, MessagePrinter printer) {
        this.factory = factory;
        this.provider = provider;
        this.printer = printer;
    }

    public static CommandOperator create(ResourceProvider provider, MessagePrinter printer) {
        return new AppCommandOperator(new CommandActionFactory(), provider, printer);
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
                factory.createQuitCommandAction().take(command);
                return null;
            }

            @Override
            public Void visit(LoadCommand command) {
                factory.createLoadCommandAction(provider, printer).take(environment, command);
                return null;
            }

            @Override
            public Void visit(UnknownCommand command) {
                createUnknownCommandAction().take(command);
                return null;
            }
        });
    }

    private UnknownCommandAction createUnknownCommandAction() {
        return new UnknownCommandAction(new UnknownCommandAction.Implementor() {

            @Override
            public void showMessage(String message) {
                printer.printMessage(message);
            }

        });
    }
}
