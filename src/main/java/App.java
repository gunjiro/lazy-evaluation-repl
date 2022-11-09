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
    private final CommandTable commandTable;
    CommandRequest(Environment env, String in) {
        environment = env;
        input = in;
        commandTable = createCommandTable();
    }
    @Override void send() throws ExitException {
        List<String> arguments = Arrays.asList(input.split("\\s+"));
        int size = arguments.size();
        if (size > 0) {
            Command command = commandTable.getCommand(arguments.get(0));
            command.execute(arguments.subList(1, size));
        }
    }
    private CommandTable createCommandTable() {
        return new CommandTable(environment);
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

abstract class Command {
    abstract void execute(List<String> args) throws ExitException;
}

class EmptyCommand extends Command {
    @Override void execute(List<String> args) throws ExitException {
    }
}

class QuitCommand extends Command {
    QuitCommand() {
    }
    @Override void execute(List<String> args) throws ExitException {
        throw new ExitException();
    }
}
class LoadCommand extends Command {
    private final LoadContractor loader;
    LoadCommand(Environment env) {
        loader = new LoadContractor(env);
    }
    @Override void execute(List<String> args) {
        for (String filename : args) {
            loadFile(filename);
        }
    }
    private void loadFile(String filename) {
        loader.load(filename);
    }
}
class LoadContractor {
    private final ResourceProvider provider;
    private final MessagePrinter printer;
    private final Environment environment;

    LoadContractor(Environment env) {
        environment = env;
        printer = new SystemOutMessagePrinter();
        provider = new FileResourceProvider();
    }

    LoadContractor(ResourceProvider provider, MessagePrinter printer, Environment environment) {
        this.provider = provider;
        this.printer = printer;
        this.environment = environment;
    }

    void load(String name) {
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
class UnknownCommand extends Command {
    private final String commandName;
    UnknownCommand(String name) {
        commandName = name;
    }
    @Override void execute(List<String> args) {
        System.out.println(String.format("unknown command '%s'", commandName));
    }
}
class CommandTable {
    private final HashMap<String, Command> map;
    CommandTable(Environment environment) {
        map = new HashMap<String, Command>();
        putQuitCommand(new QuitCommand());
        putLoadCommand(new LoadCommand(environment));
    }
    private void putCommand(String str, Command com) {
        map.put(str, com);
    }
    private void putQuitCommand(Command quit) {
        putCommand(":q", quit);
        putCommand(":qu", quit);
        putCommand(":qui", quit);
        putCommand(":quit", quit);
    }
    private void putLoadCommand(Command load) {
        putCommand(":l", load);
        putCommand(":lo", load);
        putCommand(":loa", load);
        putCommand(":load", load);
    }
    Command getCommand(String key) {
        Command command = map.get(key);
        if (command == null) {
            command = createUnknownCommand(key);
        }
        return command;
    }
    private Command createUnknownCommand(String name) {
        return new UnknownCommand(name);
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

