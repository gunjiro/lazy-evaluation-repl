package io.github.gunjiro.hj;

import java.io.IOError;
import java.io.IOException;
import java.io.Reader;

public class AppRequestOperator implements RequestOperator {
    private final RequestActionFactory factory;
    private final ResourceProvider provider;
    private final StringPrinter strinngPrinter;
    private final MessagePrinter messagePrinter;

    private AppRequestOperator(RequestActionFactory factory, ResourceProvider provider, StringPrinter strinngPrinter, MessagePrinter messagePrinter) {
        this.factory = factory;
        this.provider = provider;
        this.strinngPrinter = strinngPrinter;
        this.messagePrinter = messagePrinter;
    }

    public static RequestOperator create(ResourceProvider provider, StringPrinter strinngPrinter, MessagePrinter messagePrinter) {
        return new AppRequestOperator(new RequestActionFactory(), provider, strinngPrinter, messagePrinter);
    }

    @Override
    public void operate(Environment environment, Request request) throws ExitException {
        request.accept(new Request.Visitor<Void>() {
            @Override
            public Void visit(EmptyRequest request) {
                return null;
            }

            @Override
            public Void visit(CommandRequest request) throws ExitException {
                factory.createCommandRequestAction(new AppCommandOperator.Implementor() {
                    @Override
                    public void showMessage(String message) {
                        messagePrinter.printMessage(message);
                    }

                    @Override
                    public void load(String name) {
                        try (Reader reader = provider.open(name)) {
                            environment.addFunctions(reader);
                            showMessage("loaded: " + name);
                        } catch (ResourceProvider.FailedException e) {
                            showMessage(e.getMessage());
                        } catch (ApplicationException e) {
                            showMessage(e.getMessage());
                        } catch (IOException e) {
                            throw new IOError(e);
                        }
                    }

                }).take(request);
                return null;
            }

            @Override
            public Void visit(EvaluationRequest request) {
                factory.createEvaluationRequestAction(strinngPrinter, messagePrinter).take(environment, request);
                return null;
            }
        });
    }
}
