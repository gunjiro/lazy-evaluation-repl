public interface Executor {
    public void execute(Environment environment, Request request) throws ExitException;
}
