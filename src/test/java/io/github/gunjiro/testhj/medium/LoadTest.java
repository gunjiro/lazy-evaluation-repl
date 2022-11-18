package io.github.gunjiro.testhj.medium;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import io.github.gunjiro.hj.App;
import io.github.gunjiro.hj.AppFactory;
import io.github.gunjiro.hj.InputReceiver;
import io.github.gunjiro.hj.MessagePrinter;
import io.github.gunjiro.hj.ResourceProvider;
import io.github.gunjiro.hj.StringPrinter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LoadTest {
    // 宣言の読み込みと評価した値の出力
    @Test
    public void testLoadAndOutputValue() {
        final LinkedList<String> inputs = new LinkedList<String>(List.of(":l napier.hj functions.hj", "take 10 napier", ":q"));
        final String expected = "[2,7,1,8,2,8,1,8,2,8]";
        final StringBuilder builder = new StringBuilder();
        final AppFactory factory = new AppFactory();
        final App app = factory.create(new InputReceiver() {
            @Override
            public String receive() {
                return inputs.poll();
            }
        }, new ResourceProvider() {
            @Override
            public Reader open(String name) throws FailedException {
                return new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(name));
            }
        }, new StringPrinter() {
            @Override
            public void print(String s) {
                builder.append(s);
            }
        }, new MessagePrinter() {
            @Override
            public void printMessage(String message) {
            }
        });

        app.run();

        assertThat(builder.toString(), is(expected));
    }
}
