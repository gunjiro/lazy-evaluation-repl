package io.github.gunjiro.hj;
public interface CommandOperator {
    public void operate(Environment environment, Command command) throws ExitException;
}
