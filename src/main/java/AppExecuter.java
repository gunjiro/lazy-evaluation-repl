public class AppExecuter implements Executer {
    private final RequestFactory factory;

    private AppExecuter(RequestFactory factory) {
        this.factory = factory;
    }

    public static Executer create() {
        return new AppExecuter(new RequestFactory());
    }

    @Override
    public void execute(Environment environment, String input) throws ExitException {
        Request request = factory.createRequest(environment, input);
        request.send();
    }
}
