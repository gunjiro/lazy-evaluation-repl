package io.github.gunjiro.hj;

public interface Request {
    public <R> R accept(Visitor<R> visitor) throws ExitException;

    public static interface Visitor<R> {
        public R visit(EmptyRequest request);
        public R visit(CommandRequest request) throws ExitException;
        public R visit(EvaluationRequest request);
    }
}