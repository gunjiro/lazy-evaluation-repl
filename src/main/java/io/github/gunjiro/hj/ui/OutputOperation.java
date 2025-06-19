package io.github.gunjiro.hj.ui;

import io.github.gunjiro.hj.app.AppTextOutputUnit;
import io.github.gunjiro.hj.unit.TextOutputUnit;

public class OutputOperation {
    private final TextOutputUnit unit = new AppTextOutputUnit();

    public void printMessage(String message) {
        unit.output(message);
        unit.newline();
    }

    public void printText(String text) {
        unit.output(text);
    }

    public void startANewLine() {
        unit.newline();
    }
}
