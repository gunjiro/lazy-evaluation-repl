package io.github.gunjiro.hj.command;
public class CommandName {
    private final String name;

    public CommandName(String name) {
        this.name = name;
    }

    public boolean matches(String input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("input should not be empty string");
        }

        return name.startsWith(input);
    }
}