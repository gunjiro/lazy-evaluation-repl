class ExitException extends Exception {
}
class ApplicationException extends Exception {
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
class EvaluationException extends Exception {
    EvaluationException(String message) {
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
