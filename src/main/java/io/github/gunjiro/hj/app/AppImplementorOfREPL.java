package io.github.gunjiro.hj.app;

import java.io.IOException;
import io.github.gunjiro.hj.repl.GeneralOperator;
import io.github.gunjiro.hj.repl.REPL;
import io.github.gunjiro.hj.state.State;

public class AppImplementorOfREPL implements REPL.Implementor {
    private final AppUnitContainer container;
    private final GeneralOperator operator = createGeneralOperator();

    public AppImplementorOfREPL(AppUnitContainer container) {
        this.container = container;
    }

    @Override
    public void operate(String input) {
        operator.operate(input);
    }

    @Override
    public void output(String text) {
        container.getTextOutputUnit().output(text);
    }

    @Override
    public void newline() {
        container.getTextOutputUnit().newline();
    }

    @Override
    public State getState() {
        return container.getManagingStateUnit().getState();
    }

    @Override
    public String getInput() throws IOException {
        return container.getInputUnit().getInput();
    }

    private GeneralOperator createGeneralOperator() {
        return new GeneralOperator(new AppImplementorOfGeneralOperator(container));
    }
}
