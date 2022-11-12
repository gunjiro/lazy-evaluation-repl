public class CommandOperator {
    private final CommandActionFactory factory;

    private CommandOperator(CommandActionFactory factory) {
        this.factory = factory;
    }

    public static CommandOperator create() {
        return new CommandOperator(new CommandActionFactory());
    }

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
                factory.createLoadCommandAction().take(environment, command);
                return null;
            }

            @Override
            public Void visit(UnknownCommand command) {
                factory.createUnknownCommandAction().take(command);
                return null;
            }
        });
    }
}
