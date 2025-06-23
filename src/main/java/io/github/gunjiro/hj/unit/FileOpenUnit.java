package io.github.gunjiro.hj.unit;

import java.io.FileNotFoundException;
import java.io.Reader;

public abstract class FileOpenUnit {
    public abstract Reader open(String filename) throws FileNotFoundException;
}
