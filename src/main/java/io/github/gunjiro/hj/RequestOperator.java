package io.github.gunjiro.hj;
public interface RequestOperator {
    public void operate(Environment environment, Request request) throws ExitException;
}
