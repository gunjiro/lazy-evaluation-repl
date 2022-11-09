import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LoaderTest {
    // Readerから宣言を読み取る
    @Test
    public void loadShouldLoadFromReader() throws ApplicationException, EvaluationException {
        final Reader reader = new StringReader("one = 1");
        final Environment environment = new DefaultEnvironment();
        final Loader loader = new Loader();

        loader.load(environment, reader);

        final Thunk thunk = environment.createThunk(new StringReader("one"));
        final IntValue value = (IntValue)thunk.eval();

        assertThat(value.getValue(), is(1));
    }
}
