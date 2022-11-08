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

        final List<String> inputPieces = Arrays.asList(input.split("\\s+"));

        assert inputPieces.size() >= 1;

        return createCommandTable(environment).getCommand(inputPieces.get(0));
    }

    private CommandTable createCommandTable(Environment environment) {
        return new CommandTable(environment);
    }
}
