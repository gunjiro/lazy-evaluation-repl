package io.github.gunjiro.hj;
class Main {
    public static void main(String[] args) {
        createApp().run();
    }

    private static App createApp() {
        return factory().create(SystemInInputReceiver.create(), new FileResourceProvider(), new SystemOutStringPrinter(), new SystemOutMessagePrinter());
    }

    private static AppFactory factory() {
        return new AppFactory();
    }
}
