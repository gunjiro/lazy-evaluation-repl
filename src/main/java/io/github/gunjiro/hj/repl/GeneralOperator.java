package io.github.gunjiro.hj.repl;

import java.io.Reader;
import java.io.StringReader;

import io.github.gunjiro.hj.AppRequestOperator;
import io.github.gunjiro.hj.ApplicationException;
import io.github.gunjiro.hj.Request;
import io.github.gunjiro.hj.RequestFactory;
import io.github.gunjiro.hj.Thunk;
import io.github.gunjiro.hj.processor.FileLoader;
import io.github.gunjiro.hj.unit.ManagingStateUnit;
import io.github.gunjiro.hj.unit.TextOutputUnit;
import io.github.gunjiro.hj.unit.ThunkTableUnit;

public class GeneralOperator {
    private final Implementor implementor;

    private final ThunkTableUnit thunkTableUnit;
    private final TextOutputUnit textOutputUnit;
    private final ManagingStateUnit managingStateUnit;

    public GeneralOperator(Implementor implementor) {
        this.implementor = implementor;
        this.thunkTableUnit = implementor.getThunkTableUnit();
        this.textOutputUnit = implementor.getTextOutputUnit();
        this.managingStateUnit = implementor.getManagingStateUnit();
    }

    public static interface Implementor {
        public TextOutputUnit getTextOutputUnit();
        public ManagingStateUnit getManagingStateUnit();
        public ThunkTableUnit getThunkTableUnit();
    }

    public void operate(String input) {
        final Request request = createRequest(input);
        createOperator().operate(request);
    }

    private Request createRequest(String input) {
        final RequestFactory factory = new RequestFactory();
        return factory.createRequest(input);
    }

    private void storeFunctions(Reader reader) {
        try {
            thunkTableUnit.addFunctions(reader);
        } catch (ApplicationException e) {
            textOutputUnit.output(e.getMessage());
            textOutputUnit.newline();
        }
    }

    private FileLoader createFileLoader() {
        return new FileLoader(new FileLoader.DefaultImplementor() {

            @Override
            public void storeFunctions(Reader reader) {
                GeneralOperator.this.storeFunctions(reader);
            }

        });
    }

    private AppRequestOperator createOperator() {
        final AppRequestOperator.Implementor appRequestOperatorImplementor = new AppRequestOperator.Implementor() {

            @Override
            public void quit() {
                managingStateUnit.stopApplication();
            }

            @Override
            public void sendText(String text) {
                textOutputUnit.output(text);
            }

            @Override
            public void sendMessage(String message) {
                textOutputUnit.output(message);
                textOutputUnit.newline();
            }

            @Override
            public void load(String name) {
                final FileLoader loader = createFileLoader();
                loader.addObserver(message -> {
                    textOutputUnit.output(message);
                    textOutputUnit.newline();
                });
                loader.load(name);
            }

            @Override
            public void sendBreak() {
                textOutputUnit.newline();
            }

        };
        final AppRequestOperator.Factory factory = new AppRequestOperator.Factory() {

            @Override
            public Thunk createThunk(String code) throws ApplicationException {
                return thunkTableUnit.createThunk(new StringReader(code));
            }

        };
        return new AppRequestOperator(appRequestOperatorImplementor, factory);
    }

}
