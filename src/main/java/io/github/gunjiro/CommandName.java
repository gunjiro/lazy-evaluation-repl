package io.github.gunjiro;
public class CommandName {
    private final String name;

    public CommandName(String name) {
        this.name = name;
    }

    public boolean matches(String input) {
        if (input.isBlank()) {
            throw new IllegalArgumentException("input should not be blank");
        }

        return name.startsWith(input);
    }
}