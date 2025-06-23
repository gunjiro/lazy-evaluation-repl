package io.github.gunjiro.hj;

import io.github.gunjiro.hj.request.CommandRequestOperator;

public class AppRequestOperator {
    private final Implementor implementor;

    public static interface Implementor {
        public void load(String name);
        public void quit();
        public void sendText(String text);
        public void sendBreak();
        public void sendMessage(String message);
        public Thunk createThunk(String code) throws ApplicationException;
    }

    public AppRequestOperator(Implementor implementor) {
        this.implementor = implementor;
    }

    private void operate(CommandRequest request) {
        final CommandRequestOperator operator = new CommandRequestOperator(new CommandRequestOperator.Implementor() {

            @Override
            public void load(String filename) {
                implementor.load(filename);
            }

            @Override
            public void stopApplication() {
                implementor.quit();
            }

            @Override
            public void output(String text) {
                implementor.sendText(text);
            }

            @Override
            public void newline() {
                implementor.sendBreak();
            }
            
        });

        operator.operate(request);
    }

    public void operate(Request request) {
        request.accept(new Request.Visitor<Void>() {

            @Override
            public Void visit(EmptyRequest request) {
                return null;
            }

            @Override
            public Void visit(CommandRequest request) {
                operate(request);
                return null;
            }

            @Override
            public Void visit(EvaluationRequest request) {
                final EvaluationRequestAction action = new EvaluationRequestAction(new EvaluationRequestAction.Implementor() {

                    @Override
                    public void sendValue(Value value) {
                        final ValuePrinter printer = new ValuePrinter(new ValuePrinter.Implementor() {

                            @Override
                            public void print(String output) {
                                implementor.sendText(output);
                            }

                        });

                        try {
                            printer.print(value);
                        } catch (ApplicationException e) {
                            sendMessage("");
                            sendMessage(e.getMessage());
                        }
                    }

                    @Override
                    public void sendMessage(String message) {
                        implementor.sendMessage(message);
                    }

                    @Override
                    public void sendBreak() {
                        implementor.sendBreak();
                    }

                }, new EvaluationRequestAction.Factory() {

                    @Override
                    public Thunk createThunk(String code) throws ApplicationException {
                        return implementor.createThunk(code);
                    }
                    
                });
                action.take(request);
                return null;
            }
        });
    }
}
