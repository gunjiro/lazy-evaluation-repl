import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class FileResourceProvider implements ResourceProvider {
    @Override
    public Reader open(String name) throws ResourceProvider.FailedException {
        try {
            return new FileReader(name);
        } catch (FileNotFoundException e) {
            throw new ResourceProvider.FailedException(e);
        }
    }
}
