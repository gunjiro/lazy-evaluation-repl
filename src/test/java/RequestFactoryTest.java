import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class RequestFactoryTest {
    // ""ならEmptyRequest
    @Test
    public void createRequestShoudCreateEmptyRequestIfInputIsEmptyString() {
        final String input = "";
        final RequestFactory factory = new RequestFactory();

        assertThat(factory.createRequest(input), is(instanceOf(EmptyRequest.class)));
    }

    // ":q"ならCommandRequest
    @Test
    public void createRequestShoudCreateCommandRequestIfInputIsCommandString() {
        final String input = ":q";
        final RequestFactory factory = new RequestFactory();

        assertThat(factory.createRequest(input), is(instanceOf(CommandRequest.class)));
    }

    // "1"ならEvaluationRequest
    @Test
    public void createRequestShoudCreateEvaluationRequestIfInputIsExpressionString() {
        final String input = "1";
        final RequestFactory factory = new RequestFactory();

        assertThat(factory.createRequest(input), is(instanceOf(EvaluationRequest.class)));
    }

}
