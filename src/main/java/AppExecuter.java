public class AppExecuter implements Executer {
    private final RequestFactory factory;

    private AppExecuter(RequestFactory factory) {
        this.factory = factory;
    }

    public static Executer create() {
        return new AppExecuter(new RequestFactory());
    }

    @Override
    public void execute(Request request) throws ExitException {
        request.send();
    }
}
