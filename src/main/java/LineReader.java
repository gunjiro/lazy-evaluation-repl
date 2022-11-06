import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;

public class LineReader {
    public String read(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new IOError(e);
        }
    }
}
