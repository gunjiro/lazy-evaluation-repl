package io.github.gunjiro;
public interface CommandOperator {
    public void operate(Environment environment, Command command) throws ExitException;
}
