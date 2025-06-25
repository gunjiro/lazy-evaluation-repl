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
    public void notifiesLoaded() {
        final StubImplementor implementor = new StubImplementor();
        final StubObserver observer = new StubObserver();
        final FileLoader loader = new FileLoader(implementor);

        loader.addObserver(observer);
        loader.load("..... filename .....");

        assertThat(implementor.getMessages(), contains("..... added functions ....."));
        assertThat(observer.getMessages(), contains("loaded: ..... filename ....."));
    }

    @Test
    public void notifiesFailedIfThrowsIOExceptionByClose() {
        final StubImplementor implementor = new StubImplementor() {

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

        };
        final StubObserver observer = new StubObserver();
        final FileLoader loader = new FileLoader(implementor);

        loader.addObserver(observer);
        loader.load("..... filename .....");

        assertThat(implementor.getMessages(), contains("..... added functions ....."));
        assertThat(observer.getMessages(), contains("loaded: ..... filename ....." , "..... throws io exception by close ....."));
    }

    @Test
    public void notifiesFailedIfThrowsApplicationExceptionWhenAddsFunctions() {
        final StubImplementor implementor = new StubImplementor() {

            @Override
            public void addFunctions(Reader reader) throws ApplicationException {
                throw new ApplicationException("..... failed to add functions .....");
            }

        };
        final StubObserver observer = new StubObserver();
        final FileLoader loader = new FileLoader(implementor);

        loader.addObserver(observer);
        loader.load("..... filename .....");

        assertThat(implementor.getMessages(), is(empty()));
        assertThat(observer.getMessages(), contains("..... failed to add functions ....."));
    }
}
