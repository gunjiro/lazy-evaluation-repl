package io.github.gunjiro.hj.app;

import io.github.gunjiro.hj.processor.FileLoader;

public class AppObserverOfFileLoader implements FileLoader.Observer {
    private final AppUnitContainer container;

    public AppObserverOfFileLoader(AppUnitContainer container) {
        this.container = container;
    }

    @Override
    public void loaded(String filename) {
        container.getTextOutputUnit().output("loaded: " + filename);
        container.getTextOutputUnit().newline();
    }

    @Override
    public void failed(String message) {
        container.getTextOutputUnit().output(message);
        container.getTextOutputUnit().newline();
    }
            
}
