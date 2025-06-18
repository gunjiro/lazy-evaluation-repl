package io.github.gunjiro.hj.app;

import java.io.IOException;

import io.github.gunjiro.hj.InputReceiver;
import io.github.gunjiro.hj.unit.InputUnit;

public class AppInputUnit extends InputUnit {
    private final InputReceiver receiver = new InputReceiver();

    @Override
    public String getInput() throws IOException {
        return receiver.receive();
    }

}
