public class AppExecuter implements Executer {
    private final RequestFactory factory;

    private AppExecuter(RequestFactory factory) {
        this.factory = factory;
    }

    public static Executer create() {
        return new AppExecuter(new RequestFactory(new DefaultEnvironment()));
    }

    @Override
    public void execute(String input) throws ExitException {
        Request request = factory.createRequest(input);
        request.send();
    }
}
