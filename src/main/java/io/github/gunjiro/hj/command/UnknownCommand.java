package io.github.gunjiro.hj.command;

import io.github.gunjiro.hj.ExitException;

public class UnknownCommand implements Command {
    private final String commandName;

    public UnknownCommand(String name) {
        commandName = name;
    }

    public String getCommandName() {
        return commandName;
    }

    @Override
    public <R> R accept(Command.Visitor<R> visitor) throws ExitException {
        return visitor.visit(this);
    }
}