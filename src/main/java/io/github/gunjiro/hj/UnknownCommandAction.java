package io.github.gunjiro.hj;

import io.github.gunjiro.hj.command.UnknownCommand;

public class UnknownCommandAction {
    private final MessagePrinter printer;

    public UnknownCommandAction(MessagePrinter printer) {
        this.printer = printer;
    }

    public void take(UnknownCommand command) {
        printer.printMessage(String.format("unknown command '%s'", command.getCommandName()));
    }
}
