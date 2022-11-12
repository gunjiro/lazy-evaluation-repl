import java.io.IOError;
import java.io.IOException;
import java.io.Reader;

public class LoadCommandAction {
    private final ResourceProvider provider;
    private final MessagePrinter printer;

    public LoadCommandAction(ResourceProvider provider, MessagePrinter printer) {
        this.provider = provider;
        this.printer = printer;
    }

    public void take(Environment environment, LoadCommand command) {
        for (String name : command.getResourceNames()) {
            take(environment, name);
        }
    }

    private void take(Environment environment, String name) {
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
