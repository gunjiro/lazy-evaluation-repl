package io.github.gunjiro.hj.app;

import java.io.Reader;

import io.github.gunjiro.hj.ApplicationException;
import io.github.gunjiro.hj.Thunk;
import io.github.gunjiro.hj.processor.FileLoader;
import io.github.gunjiro.hj.repl.GeneralOperator;

public class AppImplementorOfGeneralOperator implements GeneralOperator.Implementor {
    private final AppUnitContainer container;
    private final FileLoader loader;

    public AppImplementorOfGeneralOperator(AppUnitContainer container, FileLoader loader) {
        this.container = container;
        this.loader = loader;
    }

    public static AppImplementorOfGeneralOperator create(AppUnitContainer container) {
        return new AppImplementorOfGeneralOperator(container, createFileLoader(container));
    }

    @Override
    public Thunk createThunk(Reader reader) throws ApplicationException {
        return container.getThunkTableUnit().createThunk(reader);
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
    public void stopApplication() {
        container.getManagingStateUnit().stopApplication();
    }

    @Override
    public void load(String name) {
        loader.load(name);
    }

    private static FileLoader createFileLoader(AppUnitContainer container) {
        final FileLoader newLoader = new FileLoader(new AppImplementorOfFileLoader(container));

        newLoader.addObserver(new AppObserverOfFileLoader(container));

        return newLoader;
    }
}
