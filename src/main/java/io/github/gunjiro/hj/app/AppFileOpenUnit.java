package io.github.gunjiro.hj.app;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import io.github.gunjiro.hj.unit.FileOpenUnit;

public class AppFileOpenUnit extends FileOpenUnit {

    @Override
    public Reader open(String filename) throws FileNotFoundException {
        return new FileReader(filename);
    }

}
