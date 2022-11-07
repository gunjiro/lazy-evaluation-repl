import java.io.InputStreamReader;
import java.io.BufferedReader;

public class SystemInInputReceiver implements InputReceiver {
    private final LineReader reader;

    private SystemInInputReceiver(LineReader reader) {
        this.reader = reader;
    }

    public static InputReceiver create() {
        return new SystemInInputReceiver(new LineReader());
    }

    @Override
    public String receive() {
        System.out.print("> ");
        return reader.read(createBufferedReader());
    }

    private BufferedReader createBufferedReader() {
        return new BufferedReader(new InputStreamReader(System.in));
    }
}