package io.github.gunjiro.hj.app;

import io.github.gunjiro.hj.unit.TextOutputUnit;

public class AppTextOutputUnit extends TextOutputUnit {
    private final ConsoleHelper helper = ConsoleHelper.create();

    @Override
    public void output(String text) {
        helper.output(text);
    }

    @Override
    public void newline() {
        helper.newline();
    }

}
