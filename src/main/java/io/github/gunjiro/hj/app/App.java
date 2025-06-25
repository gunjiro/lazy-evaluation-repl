package io.github.gunjiro.hj.app;

import io.github.gunjiro.hj.repl.REPL;

public class App {
    public static void run() {
        create().createREPL().run();
    }

    private static App create() {
        return new App();
    }

    private REPL createREPL() {
        return new REPL(new AppImplementorOfREPL(new AppUnitContainer()));
    }
}
