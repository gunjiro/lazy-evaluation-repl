public class CommandOperator {
    private final LoadCommandAction loadAction;
    private final MessagePrinter printer;

    public CommandOperator(LoadCommandAction loadAction, MessagePrinter printer) {
        this.loadAction = loadAction;
        this.printer = printer;
    }

    public void operate(Environment environment, Command command) throws ExitException {
        command.accept(new Command.Visitor<Void>() {
            @Override
            public Void visit(EmptyCommand command) {
                return null;
            }

            @Override
            public Void visit(QuitCommand command) throws ExitException {
                throw new ExitException();
            }

            @Override
            public Void visit(LoadCommand command) {
                loadAction.take(environment, command);
                return null;
            }

            @Override
            public Void visit(UnknownCommand command) {
                printer.printMessage(String.format("unknown command '%s'", command.getCommandName()));
                return null;
            }
        });
    }
}
