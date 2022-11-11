import java.io.IOError;
import java.io.IOException;
import java.io.Reader;

public class LoadAction {
    private final ResourceProvider provider;
    private final MessagePrinter printer;

    public LoadAction(ResourceProvider provider, MessagePrinter printer) {
        this.provider = provider;
        this.printer = printer;
    }

    public void apply(Environment environment, String name) {
        try (Reader reader = provider.open(name)) {
            environment.addFunctions(reader);
            printer.printMessage("loaded: " + name);
        } catch (ResourceProvider.FailedException e) {
            printer.printMessage(e.getMessage());
        } catch (ApplicationException e) {
            printer.printMessage(e.getMessage());
        } catch (IOException e) {
            throw new IOError(e);
        }
    }
}
