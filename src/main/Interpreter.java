import java.io.*;
import java.util.*;

class Interpreter {
    private final Console console;
    private final RequestFactory factory;
    Interpreter() {
        console = System.console();
        if (console == null) {
            throw new InternalError("コンソールが取得できませんでした。");
        }
        factory = new RequestFactory(new DefaultEnvironment());
    }
    void execute() {
        try {
            loopInteraction();
        }
        catch (ExitException e) {
            System.out.println("Bye.");
        }
    }
    private void loopInteraction() throws ExitException {
        while (true) {
            String input = console.readLine("> ");
            if (input == null) {
                input = "";
            }
            Request request = factory.createRequest(input);
            request.send();
        }
    }
}

abstract class Request {
    abstract void send() throws ExitException;
}

class RequestFactory {
    private final Environment environment;
    RequestFactory(Environment env) {
        environment = env;
    }
    Request createRequest(String input) {
        final Request request;
        input = input.trim();
        if ("".equals(input)) {
            request = createEmptyRequest();
        }
        else if (input.charAt(0) == ':') {
            request = createCommandRequest(input);
        }
        else {
            request = createEvaluationRequest(input);
        }
        return request;
    }
    private Request createEmptyRequest() {
        return new Request() {
            @Override void send() {
            }
        };
    }
    private Request createCommandRequest(String input) {
        return new CommandRequest(environment, input);
    }
    private Request createEvaluationRequest(String input) {
        return new EvaluationRequest(environment, input);
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

class QuitCommand extends Command {
    QuitCommand() {
    }
    @Override void execute(List<String> args) throws ExitException {
        throw new ExitException();
    }
}
class LoadCommand extends Command {
    private final Loader loader;
    LoadCommand(Environment env) {
        loader = new Loader(env);
    }
    @Override void execute(List<String> args) {
        for (String filename : args) {
            loadFile(filename);
        }
    }
    private void loadFile(String filename) {
        try {
            loader.load(filename);
        }
        catch (ApplicationException e) {
            System.err.println(e.getMessage());
        }
    }
}
class Loader {
    private final Environment environment;
    Loader(Environment env) {
        environment = env;
    }
    void load(String filename) throws ApplicationException {
        try {
            Reader reader = new FileReader(filename);
            try {
                environment.addFunctions(reader);
                System.out.println("loaded: " + filename);
            }
            finally {
                reader.close();
            }
        }
        catch (IOException e) {
            throw new ApplicationException(e);
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

