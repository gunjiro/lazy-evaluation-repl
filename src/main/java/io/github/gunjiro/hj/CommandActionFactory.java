package io.github.gunjiro.hj;
public class CommandActionFactory {
    public QuitCommandAction createQuitCommandAction() {
        return new QuitCommandAction();
    }

    public LoadCommandAction createLoadCommandAction(ResourceProvider provider, MessagePrinter printer) {
        return new LoadCommandAction(provider, printer);
    }

    public UnknownCommandAction createUnknownCommandAction(MessagePrinter printer) {
        return new UnknownCommandAction(printer);
    }
}
