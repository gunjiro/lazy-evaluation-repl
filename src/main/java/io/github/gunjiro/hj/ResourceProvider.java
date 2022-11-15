package io.github.gunjiro.hj;
import java.io.Reader;

public interface ResourceProvider {
    public Reader open(String name) throws FailedException;

    public static class FailedException extends Exception {
        public FailedException(String message) {
            super(message);
        }

        public FailedException(Throwable cause) {
            super(cause);
        }

        public FailedException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
