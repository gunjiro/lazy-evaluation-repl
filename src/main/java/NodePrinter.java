import java.util.*;

class NodePrinter {
    private final NodeVisitor visitor;
    private final TreePrinter printer;
    NodePrinter() {
        printer = new TreePrinter(System.out);
        visitor = new NodeVisitor() {
            @Override
            public void visit(DeclsNode node) {
                Iterator<DefineNode> nodes = node.getDefineNodes().iterator();
                if (nodes.hasNext()) {
                    printer.println("#decls#");
                    printer.pushT();
                    DefineNode n = nodes.next();
                    while (nodes.hasNext()) {
                        printer.changeT();
                        n.accept(this);
                        n = nodes.next();
                    }
                    printer.changeL();
                    n.accept(this);
                    printer.pop();
                }
            }
            @Override
            public void visit(DefineNode node) {
                String str = node.getName();
                for (String s : node.getArgs()) {
                    str += " " + s;
                }
                printer.println(str);
                printer.pushL();
                node.getExpNode().accept(this);
                printer.pop();
            }
            @Override
            public void visit(LetNode node) {
                printer.println("#let#");
                printer.pushT();
                node.getDeclsNode().accept(this);
                printer.changeL();
                node.getExpNode().accept(this);
                printer.pop();
            }
            @Override
            public void visit(IfNode node) {
                printer.println("#if#");
                printer.pushT();
                node.getConNode().accept(this);
                printer.changeT();
                node.getThenNode().accept(this);
                printer.changeL();
                node.getElseNode().accept(this);
                printer.pop();
            }
            @Override
            public void visit(ApplyNode node) {
                printer.println("#apply#");
                printer.pushT();
                node.getFuncNode().accept(this);
                printer.changeL();
                node.getArgNode().accept(this);
                printer.pop();
            }
            @Override
            public void visit(VarNode node) {
                printer.println(node.getName());
            }
            @Override
            public void visit(UnaryOpNode node) {
                printer.println(node.getOp());
                printer.pushL();
                node.getBodyNode().accept(this);
                printer.pop();
            }
            @Override
            public void visit(BinOpNode node) {
                printer.println(node.getOp());
                printer.pushT();
                node.getLeft().accept(this);
                printer.changeL();
                node.getRight().accept(this);
                printer.pop();
            }
            @Override
            public void visit(IntNode node) {
                printer.println(String.valueOf(node.getValue()));
            }
            @Override
            public void visit(EmptyListNode node) {
                printer.println("[]");
            }
        };
    }
    void print(Node node) {
        node.accept(visitor);
    }
}
