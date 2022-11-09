import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LoadContractorTest {
    // ResourceProviderから宣言を読み込む
    @Test
    public void loadShouldLoadFromResourceProvider() throws EvaluationException, ApplicationException {
        final ResourceProvider provider = new ResourceProvider() {
            @Override
            public Reader open(String name) throws ResourceProvider.FailedException {
                return new StringReader("one = 1");
            }
        };
        final Environment environment = new DefaultEnvironment();
        final LoadContractor contractor = new LoadContractor(provider, new MessagePrinter() {
            @Override
            public void printMessage(String message) {
            }
            
        }, environment);

        contractor.load("sample");

        final IntValue value = (IntValue)environment.createThunk(new StringReader("one")).eval();

        assertThat(value.getValue(), is(1));
    }
}
