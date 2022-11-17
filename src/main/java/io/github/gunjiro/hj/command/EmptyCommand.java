package io.github.gunjiro.hj.command;

import io.github.gunjiro.hj.ExitException;

public class EmptyCommand implements Command {
    @Override
    public <R> R accept(Command.Visitor<R> visitor) throws ExitException {
        return visitor.visit(this);
    }
}