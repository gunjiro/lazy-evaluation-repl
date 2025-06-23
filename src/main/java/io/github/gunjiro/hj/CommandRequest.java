package io.github.gunjiro.hj;

public class CommandRequest implements Request{
    private final String input;

    CommandRequest(String in) {
        input = in;
    }

    public String getInput() {
        return input;
    }

    @Override
    public <R> R accept(Request.Visitor<R> visitor) {
        return visitor.visit(this);
    }
}