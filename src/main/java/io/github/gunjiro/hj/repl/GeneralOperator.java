package io.github.gunjiro.hj.repl;

import java.io.Reader;
import java.io.StringReader;

import io.github.gunjiro.hj.AppRequestOperator;
import io.github.gunjiro.hj.ApplicationException;
import io.github.gunjiro.hj.InputConverter;
import io.github.gunjiro.hj.Request;
import io.github.gunjiro.hj.Thunk;

public class GeneralOperator {
    private final InputConverter converter = new InputConverter();
    private final Implementor implementor;

    public GeneralOperator(Implementor implementor) {
        this.implementor = implementor;
    }

    public static interface Implementor {
        public Thunk createThunk(Reader reader) throws ApplicationException;
        public void output(String text);
        public void newline();
        public void stopApplication();
        public Request convertToRequest(String input);
        public void load(String name);
    }

    public void operate(String input) {
        final Request request = convertToRequest(input);
        operate(request);
    }

    private void operate(Request request) {
        createOperator().operate(request);
    }

    private Request convertToRequest(String input) {
        return converter.convertToRequest(input);
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

    private void load(String name) {
        implementor.load(name);
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
