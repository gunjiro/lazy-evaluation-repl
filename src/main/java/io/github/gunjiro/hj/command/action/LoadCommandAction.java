package io.github.gunjiro.hj.command.action;
import java.io.IOError;
import java.io.IOException;
import java.io.Reader;

import io.github.gunjiro.hj.ApplicationException;
import io.github.gunjiro.hj.Environment;
import io.github.gunjiro.hj.MessagePrinter;
import io.github.gunjiro.hj.ResourceProvider;
import io.github.gunjiro.hj.command.LoadCommand;

public class LoadCommandAction {
    public static interface Implementor {
        public void showMessage(String message);
    }

    private final ResourceProvider provider;
    private final Implementor implementor;

    public LoadCommandAction(ResourceProvider provider, LoadCommandAction.Implementor implementor) {
        this.provider = provider;
        this.implementor = implementor;
    }

    public LoadCommandAction(ResourceProvider provider, MessagePrinter printer) {
        this(provider, new Implementor() {

            @Override
            public void showMessage(String message) {
                printer.printMessage(message);
            }

        });
    }

    public void take(Environment environment, LoadCommand command) {
        for (String name : command.getResourceNames()) {
            take(environment, name);
        }
    }

    private void take(Environment environment, String name) {
        try (Reader reader = provider.open(name)) {
            environment.addFunctions(reader);
            implementor.showMessage("loaded: " + name);
        } catch (ResourceProvider.FailedException e) {
            implementor.showMessage(e.getMessage());
        } catch (ApplicationException e) {
            implementor.showMessage(e.getMessage());
        } catch (IOException e) {
            throw new IOError(e);
        }
    }
}
