package io.github.gunjiro.hj;

public class NewApp {
    public static void run() {
        createREPL().run();
    }

    private static REPL createREPL() {
        return new REPL(new REPL.Implementor() {
            final Environment environment = new DefaultEnvironment();

            @Override
            public String waitForInput() {
                final InputReceiver receiver = SystemInInputReceiver.create();
                return receiver.receive();
            }

            @Override
            public REPL.Result execute(String input) {
                try {
                    final RequestFactory factory = new RequestFactory();
                    final RequestOperator operator = AppRequestOperator.create(new FileResourceProvider(), new SystemOutStringPrinter(),  new SystemOutMessagePrinter());
                    Request request = factory.createRequest(input);
                    operator.operate(environment, request);
                    return REPL.Result.Continue;
                } catch (ExitException e) {
                    return REPL.Result.Quit;
                }
            }

            @Override
            public void showQuitMessage() {
                System.out.println("Bye.");
            }
            
        });
    }

}
