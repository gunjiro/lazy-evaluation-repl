package io.github.gunjiro.hj;

import io.github.gunjiro.hj.command.action.LoadCommandAction;
import io.github.gunjiro.hj.command.action.QuitCommandAction;

public class CommandActionFactory {
    public QuitCommandAction createQuitCommandAction() {
        return new QuitCommandAction();
    }

    public LoadCommandAction createLoadCommandAction(ResourceProvider provider, LoadCommandAction.Implementor implementor) {
        return new LoadCommandAction(provider, implementor);
    }

}
