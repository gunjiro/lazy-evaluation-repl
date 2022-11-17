package io.github.gunjiro.hj.command;

import io.github.gunjiro.hj.ExitException;

public interface Command {
    public <R> R accept(Visitor<R> visitor) throws ExitException;

    public static interface Visitor<R> {
        public R visit(EmptyCommand command);
        public R visit(QuitCommand command) throws ExitException;
        public R visit(LoadCommand command);
        public R visit(UnknownCommand command);
    }
}
