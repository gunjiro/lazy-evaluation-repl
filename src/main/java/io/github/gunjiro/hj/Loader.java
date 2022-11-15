package io.github.gunjiro.hj;
import java.io.Reader;

public class Loader {
    public void load(Environment environment, Reader reader) throws ApplicationException {
        environment.addFunctions(reader);
    }
}
