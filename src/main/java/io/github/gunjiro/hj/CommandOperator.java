package io.github.gunjiro.hj;

import io.github.gunjiro.hj.command.Command;

public interface CommandOperator {
    public void operate(Command command) throws ExitException;
    public void operate(Environment environment, Command command) throws ExitException;
}
