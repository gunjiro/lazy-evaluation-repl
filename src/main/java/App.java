import java.io.*;
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

abstract class Request {
    abstract void send() throws ExitException;
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
        return new CommandRequest(environment, input);
    }
    private Request createEvaluationRequest(Environment environment, String input) {
        return new EvaluationRequest(environment, input);
    }
}

class EmptyRequest extends Request {
    @Override
    void send() {
    }
}

class CommandRequest extends Request{
    private final Environment environment;
    private final String input;

    CommandRequest(Environment env, String in) {
        environment = env;
        input = in;
    }

    @Override
    void send() throws ExitException {
        analyzer().analyze(environment, input).execute();
    }

    private CommandAnalyzer analyzer() {
        return new CommandAnalyzer();
    }
}

class EvaluationRequest extends Request {
    private final Environment environment;
    private final String input;
    private final ValuePrinter printer;
    EvaluationRequest(Environment env, String in) {
        environment = env;
        input = in;
        printer = new ValuePrinter();
    }
    @Override void send() {
        try {
            evaluate();
        }
        catch (ApplicationException e) {
            System.err.println(e.getMessage());
        }
    }
    private void evaluate() throws ApplicationException {
        try {
            Reader reader = new StringReader(input);
            try {
                Thunk thunk = environment.createThunk(reader);
                printer.print(thunk.eval());
                System.out.println();
            }
            finally {
                reader.close();
            }
        }
        catch (EvaluationException e) {
            throw new ApplicationException(e);
        }
        catch (IOException e) {
            throw new ApplicationException(e);
        }
    }
}

interface Command {
    public void execute() throws ExitException;
}

class EmptyCommand implements Command {
    @Override
    public void execute() throws ExitException {
    }
}

class QuitCommand implements Command {
    @Override
    public void execute() throws ExitException {
        throw new ExitException();
    }
}

class LoadCommand implements Command {
    private final List<String> resourceNames;
    private final Environment environment;

    LoadCommand(Environment env) {
        resourceNames = List.of();
        environment = env;
    }

    LoadCommand(List<String> resources, Environment env) {
        this.resourceNames = resources;
        environment = env;
    }

    public List<String> getResourceNames() {
        return resourceNames;
    }

    void execute(List<String> args) {
        for (String filename : args) {
            loadFile(filename);
        }
    }

    private void loadFile(String filename) {
        factory().create().load(environment, filename);
    }

    private LoadContractorFactory factory() {
        return new LoadContractorFactory();
    }

    @Override
    public void execute() throws ExitException {
        execute(resourceNames);
    }
}

class LoadContractor {
    private final ResourceProvider provider;
    private final MessagePrinter printer;

    LoadContractor(ResourceProvider provider, MessagePrinter printer) {
        this.provider = provider;
        this.printer = printer;
    }

    void load(Environment environment, String name) {
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

class UnknownCommand implements Command {
    private final String commandName;

    UnknownCommand(String name) {
        commandName = name;
    }

    @Override
    public void execute() throws ExitException {
        System.out.println(String.format("unknown command '%s'", commandName));
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
