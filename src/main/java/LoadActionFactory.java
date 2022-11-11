public class LoadActionFactory {
    public LoadAction create() {
        return new LoadAction(new FileResourceProvider(), new SystemOutMessagePrinter());
    }
}
