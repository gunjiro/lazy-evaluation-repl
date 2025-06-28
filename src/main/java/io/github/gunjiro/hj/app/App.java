package io.github.gunjiro.hj.app;

import io.github.gunjiro.hj.file.FileLoader;
import io.github.gunjiro.hj.repl.GeneralOperator;
import io.github.gunjiro.hj.repl.REPL;

public class App {
    public static void run() {
        create().createREPL().run();
    }

    private static App create() {
        return new App();
    }

    private REPL createREPL() {
        final AppUnitContainer container = new AppUnitContainer();
        final FileLoader newLoader = new FileLoader(new AppImplementorOfFileLoader(container));
        final GeneralOperator operator = GeneralOperator.create(new AppImplementorOfGeneralOperator(container, newLoader));

        newLoader.addObserver(new AppObserverOfFileLoader(container));

        return new REPL(new AppImplementorOfREPL(container, operator));
    }
}
