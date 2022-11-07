class Main {
    public static void main(String[] args) {
        createInterpreter().execute();
    }

    private static Interpreter createInterpreter() {
        return new Interpreter(new IOLoop(SystemInInputReceiver.create(), AppExecuter.create()), new MessagePrinter());
    }
}
