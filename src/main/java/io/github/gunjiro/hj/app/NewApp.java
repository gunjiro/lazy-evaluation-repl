package io.github.gunjiro.hj.app;

import io.github.gunjiro.hj.DefaultEnvironment;
import io.github.gunjiro.hj.Environment;
import io.github.gunjiro.hj.REPL;
import io.github.gunjiro.hj.unit.InputUnit;
import io.github.gunjiro.hj.unit.ManagingStateUnit;
import io.github.gunjiro.hj.unit.TextOutputUnit;

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
