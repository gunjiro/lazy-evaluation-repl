package io.github.gunjiro.driver.hj;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;

import io.github.gunjiro.hj.AppRequestOperator;
import io.github.gunjiro.hj.ApplicationException;
import io.github.gunjiro.hj.DefaultEnvironment;
import io.github.gunjiro.hj.Environment;
import io.github.gunjiro.hj.InputConverter;
import io.github.gunjiro.hj.Thunk;
import io.github.gunjiro.hj.processor.FileLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LoadTest {

    @Test
    public void loadAndEvaluate() {
        final StringBuilder output = new StringBuilder();

        final List<String> resouces = List.of("napier.hj","functions.hj");
        final String expression = "take 10 napier";
        final String expected = "[2,7,1,8,2,8,1,8,2,8]";

        final Environment environment = new DefaultEnvironment();
        final AppRequestOperator operator = new AppRequestOperator(new AppRequestOperator.Implementor() {

            @Override
            public void load(String name) {
                final FileLoader loader = new FileLoader(new FileLoader.Implementor() {

                    @Override
                    public void storeFunctions(Reader reader) {
                        try {
                            environment.addFunctions(reader);
                        } catch (ApplicationException e) {
                            sendMessage(e.getMessage());
                        }
                    }

                    @Override
                    public Reader open(String filename) throws FileNotFoundException {
                        return new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(filename));
                    }

                });
                loader.load(name);
            }

            @Override
            public void quit() {
                throw new UnsupportedOperationException("Unimplemented method 'quit'");
            }

            @Override
            public void sendText(String text) {
                output.append(text);
            }

            @Override
            public void sendBreak() {
            }

            @Override
            public void sendMessage(String message) {
            }

            @Override
            public Thunk createThunk(String code) throws ApplicationException {
                return environment.createThunk(new StringReader(code));
            }
            
        });
        for (String resource : resouces) {
            operator.operate((new InputConverter()).createRequest(String.format(":l %s", resource)));
        }
        operator.operate((new InputConverter()).createRequest(expression));

        assertThat(output, hasToString(expected));
    }
}
