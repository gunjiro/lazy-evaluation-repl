package io.github.gunjiro.hj.app;

import io.github.gunjiro.hj.DefaultEnvironment;
import io.github.gunjiro.hj.Environment;
import io.github.gunjiro.hj.REPL;
import io.github.gunjiro.hj.InputReceiver;
import io.github.gunjiro.hj.ui.OutputOperation;
import io.github.gunjiro.hj.unit.TextOutputUnit;

public class NewApp {
    public static void run() {
        createREPL().run();
    }

    private static REPL createREPL() {
        return REPL.create(new REPL.Factory() {

            @Override
            public Environment createEnvironment() {
                return new DefaultEnvironment();
            }

            @Override
            public OutputOperation createOutputOperation() {
                return new OutputOperation();
            }

            @Override
            public InputReceiver createInputReceiver() {
                return new InputReceiver();
            }

            @Override
            public AppInformation createAppInformation() {
                return new AppInformation();
            }

            @Override
            public TextOutputUnit createTextOutputUnit() {
                return new AppTextOutputUnit();
            }
            
        });
    }
}
