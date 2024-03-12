package io.github.gunjiro.hj;

public class RequestFactory {
    public Request createRequest(String input) {
        return createRequestWithTrimmedInput(input.trim());
    }

    private Request createRequestWithTrimmedInput(String input) {
        assert input.trim().equals(input);

        if ("".equals(input)) {
            return new EmptyRequest();
        }
        else if (input.charAt(0) == ':') {
            return new CommandRequest(input);
        }
        else {
            return new EvaluationRequest(input);
        }
    }
}