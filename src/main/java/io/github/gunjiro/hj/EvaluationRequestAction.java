package io.github.gunjiro.hj;
import java.io.StringReader;

public class EvaluationRequestAction {
    private final ValuePrinter valuePrinter;
    private final MessagePrinter messagePrinter;

    public EvaluationRequestAction(ValuePrinter valuePrinter, MessagePrinter messagePrinter) {
        this.valuePrinter = valuePrinter;
        this.messagePrinter = messagePrinter;
    }

    public void take(Environment environment, EvaluationRequest request) {
        take(environment, request.getInput());
    }

    private void take(Environment environment, String code) {
        if (code.isEmpty()) {
            return;
        }

        try {
            valuePrinter.print(createThunk(environment, code).eval());
            messagePrinter.printMessage("");
        } catch (ApplicationException e) {
            messagePrinter.printMessage("");
            messagePrinter.printMessage(e.getMessage());
        } catch (EvaluationException e) {
            messagePrinter.printMessage("");
            messagePrinter.printMessage(e.getMessage());
        }
    }

    private Thunk createThunk(Environment environment, String code) throws ApplicationException {
        return environment.createThunk(new StringReader(code));
    }
}
