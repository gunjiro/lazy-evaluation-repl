package io.github.gunjiro.hj.app;

import io.github.gunjiro.hj.unit.TextOutputUnit;

public class AppTextOutputUnit extends TextOutputUnit {

    @Override
    public void output(String text) {
        System.out.print(text);
    }

    @Override
    public void newline() {
        System.out.println();
    }

}
