package io.github.gunjiro.hj.app;

import io.github.gunjiro.hj.REPL;

public class NewApp {
    public static void run() {
        create().createREPL().run();
    }

    private static NewApp create() {
        return new NewApp();
    }

    private REPL createREPL() {
        return new REPL(new AppImplementorOfREPL());
    }
}
