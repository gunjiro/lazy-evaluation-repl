package io.github.gunjiro.hj.processor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import io.github.gunjiro.hj.ApplicationException;

public class FileLoaderTest {
    private static class StubImplementor implements FileLoader.Implementor {
        private final List<String> messages = new LinkedList<>();

        @Override
        public Reader open(String filename) throws FileNotFoundException {
            return new StringReader("..... code .....");
        }

        @Override
        public void addFunctions(Reader reader) throws ApplicationException {
            messages.add("..... added functions .....");
        }

        private List<String> getMessages() {
            return Collections.unmodifiableList(messages);
        }

    }

    private static class StubObserver implements FileLoader.Observer {
        private final List<String> messages = new LinkedList<>();

        @Override
        public void failed(String message) {
            messages.add(message);
        }

        @Override
        public void loaded(String filename) {
            messages.add("loaded: " + filename);
        }

        private List<String> getMessages() {
            return Collections.unmodifiableList(messages);
        }

    }

    @Test
    public void notifiesFailedIfFileNotFound() {
        final StubImplementor implementor = new StubImplementor() {
            @Override
            public Reader open(String filename) throws FileNotFoundException {
                throw new FileNotFoundException("..... file not found .....");
            }
        };
        final StubObserver observer = new StubObserver();
        final FileLoader loader = new FileLoader(implementor);

        loader.addObserver(observer);
        loader.load("..... filename .....");

        assertThat(observer.getMessages(), contains("..... file not found ....."));
    }

    @Test
    public void storeFunctionsFromReader() {
        // Readerから関数を取り込む。
        final List<String> messages = new LinkedList<>();
        final FileLoader loader = new FileLoader(new FileLoader.Implementor() {

            @Override
            public Reader open(String filename) throws FileNotFoundException {
                return new StringReader(".....code.....");
            }

            @Override
            public void addFunctions(Reader reader) throws ApplicationException {
                messages.add("..... stored .....");
            }

        });
        loader.addObserver(new FileLoader.Observer() {

            @Override
            public void failed(String message) {
                messages.add(message);
            }

            @Override
            public void loaded(String filename) {
                messages.add("loaded: " + filename);
            }

        });
        loader.load("..... filename .....");

        assertThat(messages, contains("..... stored .....", "loaded: ..... filename ....."));
    }

    @Test
    public void sendMessageIfThrowsIOExceptionByClose() {
        // Readerを閉じたときにIOExceptionが発生したら、メッセージを送る。
        final List<String> messages = new LinkedList<>();

        final FileLoader loader = new FileLoader(new FileLoader.Implementor() {

            @Override
            public Reader open(String filename) throws FileNotFoundException {
                return new Reader() {

                    @Override
                    public int read(char[] cbuf, int off, int len) throws IOException {
                        throw new UnsupportedOperationException("Unimplemented method 'read'");
                    }

                    @Override
                    public void close() throws IOException {
                        throw new IOException("..... throws io exception by close .....");
                    }
                };
            }

            @Override
            public void addFunctions(Reader reader) throws ApplicationException {
                // addFunctions
            }

        });
        loader.addObserver(new FileLoader.Observer() {

            @Override
            public void failed(String message) {
                messages.add(message);
            }

            @Override
            public void loaded(String filename) {
                // loaded
            }
            
        });
        loader.load("..... filename .....");

        assertThat(messages, hasItem("..... throws io exception by close ....."));
    }

    @Test
    public void notifiesFailedIfThrowsApplicationExceptionWhenAddsFunctions() {
        final List<String> messages = new LinkedList<>();

        final FileLoader loader = new FileLoader(new FileLoader.Implementor() {

            @Override
            public Reader open(String filename) throws FileNotFoundException {
                return new StringReader("..... code .....");
            }

            @Override
            public void addFunctions(Reader reader) throws ApplicationException {
                throw new ApplicationException("..... failed to add functions .....");
            }

        });
        loader.addObserver(new FileLoader.Observer() {

            @Override
            public void failed(String message) {
                messages.add(message);
            }

            @Override
            public void loaded(String filename) {
                messages.add("loaded: " + filename);
            }
            
        });
        loader.load("..... filename .....");

        assertThat(messages, hasItem("..... failed to add functions ....."));
        assertThat(messages, not(hasItem("loaded: ..... filename .....")));
    }
}
