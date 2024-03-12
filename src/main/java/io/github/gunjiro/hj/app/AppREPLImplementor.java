package io.github.gunjiro.hj.app;

import io.github.gunjiro.hj.AppRequestOperator;
import io.github.gunjiro.hj.DefaultEnvironment;
import io.github.gunjiro.hj.Environment;
import io.github.gunjiro.hj.ExitException;
import io.github.gunjiro.hj.FileResourceProvider;
import io.github.gunjiro.hj.InputReceiver;
import io.github.gunjiro.hj.REPL;
import io.github.gunjiro.hj.Request;
import io.github.gunjiro.hj.RequestFactory;
import io.github.gunjiro.hj.RequestOperator;
import io.github.gunjiro.hj.SystemInInputReceiver;
import io.github.gunjiro.hj.SystemOutMessagePrinter;
import io.github.gunjiro.hj.SystemOutStringPrinter;

class AppREPLImplementor implements REPL.Implementor {
    private final Environment environment;

    private AppREPLImplementor(Environment environment) {
        this.environment = environment;
    }

    static AppREPLImplementor create() {
        return new AppREPLImplementor(new DefaultEnvironment());
    }

    @Override
    public String waitForInput() {
        final InputReceiver receiver = SystemInInputReceiver.create();
        return receiver.receive();
    }

    @Override
    public REPL.Result execute(String input) {
        try {
            final RequestFactory factory = new RequestFactory();
            final RequestOperator operator = AppRequestOperator.create(new FileResourceProvider(),
                    new SystemOutStringPrinter(), new SystemOutMessagePrinter());
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

}