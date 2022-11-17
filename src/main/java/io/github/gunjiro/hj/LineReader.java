package io.github.gunjiro.hj;
import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;

public class LineReader {
    public String read(BufferedReader reader) {
        try {
            final String line = reader.readLine();
            if (line == null) {
                throw new IOError(new IOException("the end of the stream has been reached"));
            }
            return line;
        } catch (IOException e) {
            throw new IOError(e);
        }
    }
}
