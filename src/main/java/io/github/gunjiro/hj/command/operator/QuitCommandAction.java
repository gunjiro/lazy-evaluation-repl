package io.github.gunjiro.hj.command.operator;

import io.github.gunjiro.hj.ExitException;
import io.github.gunjiro.hj.command.QuitCommand;

class QuitCommandAction {
    void take(QuitCommand command) throws ExitException {
        throw new ExitException();
    }
}
