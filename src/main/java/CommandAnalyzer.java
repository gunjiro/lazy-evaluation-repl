import java.util.Arrays;
import java.util.List;

public class CommandAnalyzer {
    public Command analyze(Environment environment, String input) {
        if (!input.startsWith(":")) {
            throw new IllegalArgumentException();
        }

        if (input.equals(":")) {
            return new EmptyCommand();
        }

        return analyze(environment, split(input));
    }

    private List<String> split(String input) {
        return Arrays.asList(input.split("\\s+"));
    }
    
    private Command analyze(Environment environment, List<String> inputPieces) {
        assert inputPieces.size() >= 1;

        if (quitCommandName().matches(inputPieces.get(0))) {
            return new QuitCommand();
        } else if (loadCommandName().matches(inputPieces.get(0))) {
            return new LoadCommand(environment);
        } else {
            return new UnknownCommand(inputPieces.get(0));
        }
    }

    private CommandName quitCommandName() {
        return new CommandName(":quit");
    }

    private CommandName loadCommandName() {
        return new CommandName(":load");
    }
}
