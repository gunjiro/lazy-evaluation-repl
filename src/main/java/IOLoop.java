public class IOLoop {
    private final InputReceiver receiver;
    private final Executer executer;

    public IOLoop(InputReceiver receiver, Executer executer) {
        this.receiver = receiver;
        this.executer = executer;
    }

    public void loop() {
        try {
            final Environment environment = new DefaultEnvironment();
            while (true) {
                String input = receiver.receive();
                executer.execute(environment, input);
            }
        } catch (ExitException e) {
        }
    }
}
