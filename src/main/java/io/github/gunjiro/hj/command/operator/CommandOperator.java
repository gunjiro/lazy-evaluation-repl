package io.github.gunjiro.hj.command.operator;

import io.github.gunjiro.hj.ExitException;
import io.github.gunjiro.hj.command.Command;

public interface CommandOperator {
    public void operate(Command command) throws ExitException;
}
