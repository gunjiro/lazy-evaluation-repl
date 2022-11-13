public class CommandOperator {
    private final CommandActionFactory factory;
    private final ResourceProvider provider;
    private final MessagePrinter printer;

    private CommandOperator(CommandActionFactory factory, ResourceProvider provider, MessagePrinter printer) {
        this.factory = factory;
        this.provider = provider;
        this.printer = printer;
    }

    public static CommandOperator create(ResourceProvider provider, MessagePrinter printer) {
        return new CommandOperator(new CommandActionFactory(), provider, printer);
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
                factory.createLoadCommandAction(provider, printer).take(environment, command);
                return null;
            }

            @Override
            public Void visit(UnknownCommand command) {
                factory.createUnknownCommandAction(printer).take(command);
                return null;
            }
        });
    }
}
