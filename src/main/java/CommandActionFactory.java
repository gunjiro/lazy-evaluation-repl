public class CommandActionFactory {
    public QuitCommandAction createQuitCommandAction() {
        return new QuitCommandAction();
    }

    public LoadCommandAction createLoadCommandAction() {
        return new LoadCommandAction(new FileResourceProvider(), new SystemOutMessagePrinter());
    }

    public UnknownCommandAction createUnknownCommandAction() {
        return new UnknownCommandAction(new SystemOutMessagePrinter());
    }
}
