package io.github.gunjiro;
import java.io.StringReader;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ValuePrinterTest {
    // リストを出力する
    @Test
    public void printShouldOutputList() throws EvaluationException, ApplicationException {
        final String code = "1:2:(3:4:5:[]):[]";
        final Environment environment = new DefaultEnvironment();
        final Value value = environment.createThunk(new StringReader(code)).eval();

        final StringBuilder builder = new StringBuilder();
        final ValuePrinter printer = new ValuePrinter(new StringPrinter() {
            @Override
            public void print(String s) {
                builder.append(s);
            }
        });

        printer.print(value);

        assertThat(builder.toString(), is("[1,2,[3,4,5]]"));
    }
}
