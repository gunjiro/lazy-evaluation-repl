package io.github.gunjiro.hj;

public class InputConverter {
    public Request convertToRequest(String input) {
        return convertTrimmedInputToRequest(input.trim());
    }

    private Request convertTrimmedInputToRequest(String trimmedInput) {
        assert trimmedInput.trim().equals(trimmedInput);

        if ("".equals(trimmedInput)) {
            return new EmptyRequest();
        }
        else if (trimmedInput.charAt(0) == ':') {
            return new CommandRequest(trimmedInput);
        }
        else {
            return new EvaluationRequest(trimmedInput);
        }
    }
}