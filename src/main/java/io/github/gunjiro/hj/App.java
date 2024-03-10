package io.github.gunjiro.hj;
import java.util.*;

public class App {
    private final IOLoop ioLoop;
    private final MessagePrinter printer;

    App(IOLoop ioLoop, MessagePrinter printer) {
        this.ioLoop = ioLoop;
        this.printer = printer;
    }

    public void run() {
        ioLoop.loop();
        printer.printMessage("Bye.");
    }
}

interface Request {
    public <R> R accept(Visitor<R> visitor) throws ExitException;

    public static interface Visitor<R> {
        public R visit(EmptyRequest request);
        public R visit(CommandRequest request) throws ExitException;
        public R visit(EvaluationRequest request);
    }
}

class RequestFactory {
    public Request createRequest(String input) {
        return createRequestWithTrimmedInput(input.trim());
    }

    private Request createRequestWithTrimmedInput(String input) {
        assert input.trim().equals(input);

        if ("".equals(input)) {
            return new EmptyRequest();
        }
        else if (input.charAt(0) == ':') {
            return new CommandRequest(input);
        }
        else {
            return new EvaluationRequest(input);
        }
    }
}

class EmptyRequest implements Request {
    @Override
    public <R> R accept(Request.Visitor<R> visitor) throws ExitException {
        return visitor.visit(this);
    }
}

class CommandRequest implements Request{
    private final String input;

    CommandRequest(String in) {
        input = in;
    }

    public String getInput() {
        return input;
    }

    @Override
    public <R> R accept(Request.Visitor<R> visitor) throws ExitException {
        return visitor.visit(this);
    }
}

class EvaluationRequest implements Request {
    private final String input;

    EvaluationRequest(String in) {
        input = in;
    }

    public String getInput() {
        return input;
    }

    @Override
    public <R> R accept(Request.Visitor<R> visitor) throws ExitException {
        return visitor.visit(this);
    }
}

class DefaultDeclsNode extends DeclsNode {
    DefaultDeclsNode() {
        super();
        add(createNot());
        add(createMod());
        add(createHead());
        add(createTail());
    }
    private DefineNode createNot() {
        return new DefineNode("not", Arrays.asList(new String[]{"x"}), new IfNode(new VarNode("x"), new IntNode(0), new IntNode(1)));
    }
    private DefineNode createMod() {
        return new DefineNode("mod", Arrays.asList(new String[]{"x", "y"}), new BinOpNode("!mod", new VarNode("x"), new VarNode("y")));
    }
    private DefineNode createHead() {
        return new DefineNode("head", Arrays.asList(new String[]{"xs"}), new UnaryOpNode("!head", new VarNode("xs")));
    }
    private DefineNode createTail() {
        return new DefineNode("tail", Arrays.asList(new String[]{"xs"}), new UnaryOpNode("!tail", new VarNode("xs")));
    }
}
