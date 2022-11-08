class Main {
    public static void main(String[] args) {
        createApp().execute();
    }

    private static App createApp() {
        return factory().create();
    }

    private static AppFactory factory() {
        return new AppFactory();
    }
}
