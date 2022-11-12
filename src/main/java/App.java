import java.util.*;

class App {
    private final IOLoop ioLoop;
    private final MessagePrinter printer;

    App(IOLoop ioLoop, MessagePrinter printer) {
        this.ioLoop = ioLoop;
        this.printer = printer;
    }

    void run() {
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
    public Request createRequest(Environment environment, String input) {
        final Request request;
        input = input.trim();
        if ("".equals(input)) {
            request = createEmptyRequest();
        }
        else if (input.charAt(0) == ':') {
            request = createCommandRequest(environment, input);
        }
        else {
            request = createEvaluationRequest(environment, input);
        }
        return request;
    }

    private Request createEmptyRequest() {
        return new EmptyRequest();
    }
    private Request createCommandRequest(Environment environment, String input) {
        return new CommandRequest(input);
    }
    private Request createEvaluationRequest(Environment environment, String input) {
        return new EvaluationRequest(input);
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

interface Command {
    public <R> R accept(Visitor<R> visitor) throws ExitException;

    public static interface Visitor<R> {
        public R visit(EmptyCommand command);
        public R visit(QuitCommand command) throws ExitException;
        public R visit(LoadCommand command);
        public R visit(UnknownCommand command);
    }
}

class EmptyCommand implements Command {
    @Override
    public <R> R accept(Command.Visitor<R> visitor) throws ExitException {
        return visitor.visit(this);
    }
}

class QuitCommand implements Command {
    @Override
    public <R> R accept(Command.Visitor<R> visitor) throws ExitException {
        return visitor.visit(this);
    }
}

class LoadCommand implements Command {
    private final List<String> resourceNames;

    LoadCommand(List<String> resourceNames) {
        this.resourceNames = resourceNames;
    }

    public List<String> getResourceNames() {
        return resourceNames;
    }

    @Override
    public <R> R accept(Command.Visitor<R> visitor) throws ExitException {
        return visitor.visit(this);
    }
}

class UnknownCommand implements Command {
    private final String commandName;

    UnknownCommand(String name) {
        commandName = name;
    }

    public String getCommandName() {
        return commandName;
    }

    @Override
    public <R> R accept(Command.Visitor<R> visitor) throws ExitException {
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

class DefaultEnvironment extends Environment {
    DefaultEnvironment() {
        super(100);
    }
    @Override void initFunctions() {
        super.initFunctions();
        addDefaultFunctions();
    }
    private void addDefaultFunctions() {
        try {
            addFunctions(createDeclsNode());
        }
        catch (ApplicationException e) {
            throw new InternalError(e.getMessage());
        }
    }
    private DeclsNode createDeclsNode() {
        return new DefaultDeclsNode();
    }
}
