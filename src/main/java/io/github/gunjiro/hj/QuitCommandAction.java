package io.github.gunjiro.hj;

import io.github.gunjiro.hj.command.QuitCommand;

public class QuitCommandAction {
    public void take(QuitCommand command) throws ExitException {
        throw new ExitException();
    }
}
