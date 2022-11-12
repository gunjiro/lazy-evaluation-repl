import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LoadActionTest {
    // ResourceProviderから宣言を読み込む
    @Test
    public void applyShouldLoadFromResourceProvider() throws EvaluationException, ApplicationException {
        final LinkedList<String> codes = new LinkedList<String>(List.of("one = 1", "two = 2"));
        final ResourceProvider provider = new ResourceProvider() {
            @Override
            public Reader open(String name) throws ResourceProvider.FailedException {
                return new StringReader(codes.poll());
            }
        };
        final Environment environment = new DefaultEnvironment();
        final LoadCommandAction action = new LoadCommandAction(provider, new MessagePrinter() {
            @Override
            public void printMessage(String message) {
            }
            
        });

        action.take(environment, new LoadCommand(List.of("sample1", "sample2")));

        final IntValue value1 = (IntValue)environment.createThunk(new StringReader("one")).eval();
        final IntValue value2 = (IntValue)environment.createThunk(new StringReader("two")).eval();

        assertThat(value1.getValue(), is(1));
        assertThat(value2.getValue(), is(2));
    }
}
