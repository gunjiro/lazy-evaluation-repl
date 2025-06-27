package io.github.gunjiro.hj.app;

import java.io.FileNotFoundException;
import java.io.Reader;

import io.github.gunjiro.hj.ApplicationException;
import io.github.gunjiro.hj.file.FileLoader;

public class AppImplementorOfFileLoader implements FileLoader.Implementor {
    private final AppUnitContainer container;

    public AppImplementorOfFileLoader(AppUnitContainer container) {
        this.container = container;
    }

    @Override
    public Reader open(String filename) throws FileNotFoundException {
        return container.getFileOpenUnit().open(filename);
    }

    @Override
    public void addFunctions(Reader reader) throws ApplicationException {
        container.getThunkTableUnit().addFunctions(reader);
    }

}
