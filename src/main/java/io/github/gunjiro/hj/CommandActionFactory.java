package io.github.gunjiro.hj;

import io.github.gunjiro.hj.command.action.QuitCommandAction;

public class CommandActionFactory {
    public QuitCommandAction createQuitCommandAction() {
        return new QuitCommandAction();
    }
}
