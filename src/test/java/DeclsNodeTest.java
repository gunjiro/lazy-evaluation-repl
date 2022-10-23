import org.junit.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class DeclsNodeTest {

    // addはdefineNodeを追加しない。defineNodeがnullの場合。
    @Test
    public void addIsNotWorkWhenNodeIsNull() {
        final DefineNode defineNode = null;
        final DeclsNode node = new DeclsNode();
        node.add(defineNode);

        assertThat(node.getDefineNodes(), is(empty()));
    }

    // addはdefineNodeを追加する。
    @Test
    public void addShouldAddDefineNode() {
        final DefineNode defineNode = new DefineNode(null, null, null);
        final DeclsNode node = new DeclsNode();
        node.add(defineNode);

        assertThat(node.getDefineNodes(), hasSize(1));
    }
}
