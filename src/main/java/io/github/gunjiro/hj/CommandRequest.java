package io.github.gunjiro.hj;

public class CommandRequest implements Request{
    private final String input;

    public CommandRequest(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    @Override
    public <R> R accept(Request.Visitor<R> visitor) {
        return visitor.visit(this);
    }
}