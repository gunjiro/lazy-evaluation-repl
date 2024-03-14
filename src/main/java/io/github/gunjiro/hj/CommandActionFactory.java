package io.github.gunjiro.hj;

import io.github.gunjiro.hj.command.action.LoadCommandAction;
import io.github.gunjiro.hj.command.action.QuitCommandAction;

public class CommandActionFactory {
    public QuitCommandAction createQuitCommandAction() {
        return new QuitCommandAction();
    }

    public LoadCommandAction createLoadCommandAction(ResourceProvider provider, MessagePrinter printer) {
        return new LoadCommandAction(provider, printer);
    }

}
