public interface RequestOperator {
    public void operate(Environment environment, Request request) throws ExitException;
}
