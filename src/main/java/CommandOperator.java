public class CommandOperator {
    private final LoadAction loadAction;
    private final MessagePrinter printer;

    public CommandOperator(LoadAction loadAction, MessagePrinter printer) {
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
                loadAction.apply(environment, command.getResourceNames());
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
