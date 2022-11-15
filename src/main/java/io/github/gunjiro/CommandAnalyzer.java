package io.github.gunjiro;
import java.util.List;

public class CommandAnalyzer {
    public Command analyze(String input) {
        return analyze(CommandInput.create(input));
    }

    private Command analyze(CommandInput input) {
        return input.extract(new CommandInput.Operation<Command>() {
            @Override
            public Command apply(String command, List<String> arguments) {
                if (emptyCommandName().matches(command)) {
                    return new EmptyCommand();

                } else if (quitCommandName().matches(command)) {
                    return new QuitCommand();

                } else if (loadCommandName().matches(command)) {
                    return new LoadCommand(arguments);

                } else {
                    return new UnknownCommand(command);
                }
            }
        });
    }

    private CommandName emptyCommandName() {
        return new CommandName(":");
    }

    private CommandName quitCommandName() {
        return new CommandName(":quit");
    }

    private CommandName loadCommandName() {
        return new CommandName(":load");
    }
}
