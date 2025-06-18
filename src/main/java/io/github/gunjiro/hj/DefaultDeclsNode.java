package io.github.gunjiro.hj;

import java.util.Arrays;

class DefaultDeclsNode extends DeclsNode {
    DefaultDeclsNode() {
        super();
        add(createNot());
        add(createMod());
        add(createHead());
        add(createTail());
    }
    private DefineNode createNot() {
        return new DefineNode("not", Arrays.asList("x"), new IfNode(new VarNode("x"), new IntNode(0), new IntNode(1)));
    }
    private DefineNode createMod() {
        return new DefineNode("mod", Arrays.asList("x", "y"), new BinOpNode("!mod", new VarNode("x"), new VarNode("y")));
    }
    private DefineNode createHead() {
        return new DefineNode("head", Arrays.asList("xs"), new UnaryOpNode("!head", new VarNode("xs")));
    }
    private DefineNode createTail() {
        return new DefineNode("tail", Arrays.asList("xs"), new UnaryOpNode("!tail", new VarNode("xs")));
    }
}