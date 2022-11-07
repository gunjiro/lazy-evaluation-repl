public class IOLoop {
    private final InputReceiver receiver;
    private final Executer executer;

    public IOLoop(InputReceiver receiver, Executer executer) {
        this.receiver = receiver;
        this.executer = executer;
    }

    public void loop() {
        try {
            while (true) {
                String input = receiver.receive();
                executer.execute(input);
            }
        } catch (ExitException e) {
        }
    }
}
