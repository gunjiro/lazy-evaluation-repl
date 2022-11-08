class Main {
    public static void main(String[] args) {
        createApp().execute();
    }

    private static App createApp() {
        return new App(new IOLoop(SystemInInputReceiver.create(), AppExecuter.create()), new MessagePrinter());
    }
}
