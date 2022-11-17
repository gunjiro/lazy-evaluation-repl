package io.github.gunjiro.hj;
public class ApplicationException extends Exception {
    ApplicationException(String message) {
        super(message);
    }
    ApplicationException(Throwable cause) {
        super(cause);
    }
}
class IllegalExpressionException extends Exception {
    IllegalExpressionException(Throwable cause) {
        super(cause);
    }
    IllegalExpressionException(String message) {
        super(message);
    }
}
class IntOperationException extends EvaluationException {
    IntOperationException(String message) {
        super(message);
    }
}
class ApplyException extends EvaluationException {
    ApplyException(String message) {
        super(message);
    }
}
class ListOperationException extends EvaluationException {
    ListOperationException(String message) {
        super(message);
    }
}
