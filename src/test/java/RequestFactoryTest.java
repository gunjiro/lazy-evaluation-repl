import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class RequestFactoryTest {
    // ""ならEmptyRequest
    @Test
    public void createRequestShoudCreateEmptyRequestIfInputIsEmptyString() {
        final Environment environment = new DefaultEnvironment();
        final String input = "";
        final RequestFactory factory = new RequestFactory();

        assertThat(factory.createRequest(environment, input), is(instanceOf(EmptyRequest.class)));
    }

    // ":q"ならCommandRequest
    @Test
    public void createRequestShoudCreateCommandRequestIfInputIsCommandString() {
        final Environment environment = new DefaultEnvironment();
        final String input = ":q";
        final RequestFactory factory = new RequestFactory();

        assertThat(factory.createRequest(environment, input), is(instanceOf(CommandRequest.class)));
    }

    // "1"ならEvaluationRequest
    @Test
    public void createRequestShoudCreateEvaluationRequestIfInputIsExpressionString() {
        final Environment environment = new DefaultEnvironment();
        final String input = "1";
        final RequestFactory factory = new RequestFactory();

        assertThat(factory.createRequest(environment, input), is(instanceOf(EvaluationRequest.class)));
    }

}
