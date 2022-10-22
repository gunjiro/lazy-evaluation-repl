import java.util.List;

import org.junit.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class DefineNodeTest {

    // hasArgsはfalseを返す。引数を持たない場合。
    @Test
    public void hasArgsReturnsFalseWhenNotPassArguments() {
        final List<String> args = List.of();
        final DefineNode node = new DefineNode(null, args, null);
        
        assertThat(node.hasArgs(), is(false));
    }

    // hasArgsはtrueを返す。引数を持つ場合。
    @Test
    public void hasArgsReturnsTrueWhenPassSomeArguments() {
        final List<String> args = List.of("arg1", "arg2", "arg3");
        final DefineNode node = new DefineNode(null, args, null);
        
        assertThat(node.hasArgs(), is(true));
    }
}
