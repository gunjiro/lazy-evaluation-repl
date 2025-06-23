package io.github.gunjiro.hj.repl;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringReader;

import io.github.gunjiro.hj.AppRequestOperator;
import io.github.gunjiro.hj.ApplicationException;
import io.github.gunjiro.hj.Request;
import io.github.gunjiro.hj.InputConverter;
import io.github.gunjiro.hj.Thunk;
import io.github.gunjiro.hj.processor.FileLoader;

public class GeneralOperator {
    private final Implementor implementor;

    public GeneralOperator(Implementor implementor) {
        this.implementor = implementor;
    }

    public static interface Implementor {
        public void addFunctions(Reader reader) throws ApplicationException;
        public Thunk createThunk(Reader reader) throws ApplicationException;
        public void output(String text);
        public void newline();
        public void stopApplication();
        public Reader open(String filename) throws FileNotFoundException;
    }

    public void operate(String input) {
        final Request request = createRequest(input);
        createOperator().operate(request);
    }

    private Request createRequest(String input) {
        final InputConverter converter = new InputConverter();
        return converter.convertToRequest(input);
    }

    private void storeFunctions(Reader reader) {
        try {
            implementor.addFunctions(reader);
        } catch (ApplicationException e) {
            implementor.output(e.getMessage());
            implementor.newline();
        }
    }

    private Thunk createThunk(String code) throws ApplicationException {
        return implementor.createThunk(new StringReader(code));
    }

    private void sendText(String text) {
        implementor.output(text);
    }

    private void sendMessage(String message) {
        implementor.output(message);
        implementor.newline();
    }

    private void sendBreak() {
        implementor.newline();
    }

    private void quit() {
        implementor.stopApplication();
    }

    private Reader open(String filename) throws FileNotFoundException {
        return implementor.open(filename);
    }

    private void load(String name) {
        final FileLoader loader = createFileLoader();
        loader.addObserver(this::sendMessage);
        loader.load(name);
    }

    private FileLoader createFileLoader() {
        return new FileLoader(new FileLoader.Implementor() {
            @Override
            public Reader open(String filename) throws FileNotFoundException {
                return GeneralOperator.this.open(filename);
            }

            @Override
            public void storeFunctions(Reader reader) {
                GeneralOperator.this.storeFunctions(reader);
            }

        });
    }

    private AppRequestOperator createOperator() {
        return new AppRequestOperator(new AppRequestOperator.Implementor() {

            @Override
            public void quit() {
                GeneralOperator.this.quit();
            }

            @Override
            public void sendText(String text) {
                GeneralOperator.this.sendText(text);
            }

            @Override
            public void sendMessage(String message) {
                GeneralOperator.this.sendMessage(message);
            }

            @Override
            public void load(String name) {
                GeneralOperator.this.load(name);
            }

            @Override
            public void sendBreak() {
                GeneralOperator.this.sendBreak();
            }

            @Override
            public Thunk createThunk(String code) throws ApplicationException {
                return GeneralOperator.this.createThunk(code);
            }

        });
    }

}
