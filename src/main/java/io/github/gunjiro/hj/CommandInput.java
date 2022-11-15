package io.github.gunjiro.hj;
import java.util.Arrays;
import java.util.List;

public class CommandInput {
    private final String command;
    private final List<String> arguments;

    private CommandInput(String command, List<String> arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public static CommandInput create(String input) {
        if (!input.startsWith(":")) {
            throw new IllegalArgumentException("input should start :");
        }

        return create(split(input));
    }

    private static List<String> split(String input) {
        return Arrays.asList(input.split("\\s+"));
    }

    private static CommandInput create(List<String> inputPieces) {
        assert inputPieces.size() >= 1;

        final String command = inputPieces.get(0);
        final List<String> arguments = inputPieces.subList(1, inputPieces.size());

        return new CommandInput(command, arguments);
    }

    public <R> R extract(Operation<R> operation) {
        return operation.apply(command, arguments);
    }

    public static interface Operation<R> {
        public R apply(String command, List<String> arguments);
    }
}
