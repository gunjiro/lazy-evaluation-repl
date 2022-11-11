import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LoadActionTest {
    // ResourceProviderから宣言を読み込む
    @Test
    public void applyShouldLoadFromResourceProvider() throws EvaluationException, ApplicationException {
        final ResourceProvider provider = new ResourceProvider() {
            @Override
            public Reader open(String name) throws ResourceProvider.FailedException {
                return new StringReader("one = 1");
            }
        };
        final Environment environment = new DefaultEnvironment();
        final LoadAction action = new LoadAction(provider, new MessagePrinter() {
            @Override
            public void printMessage(String message) {
            }
            
        });

        action.apply(environment, List.of("sample"));

        final IntValue value = (IntValue)environment.createThunk(new StringReader("one")).eval();

        assertThat(value.getValue(), is(1));
    }
}
