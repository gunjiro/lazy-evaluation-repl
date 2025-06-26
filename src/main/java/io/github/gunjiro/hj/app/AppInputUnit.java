package io.github.gunjiro.hj.app;

import java.io.IOException;

import io.github.gunjiro.hj.unit.InputUnit;

public class AppInputUnit extends InputUnit {
    private final ConsoleHelper helper = ConsoleHelper.create();

    @Override
    public String getInput() throws IOException {
        return helper.getInput();
    }

}
