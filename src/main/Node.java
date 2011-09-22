import java.util.*;

interface Node {
    public void accept(NodeVisitor v);
}
interface ExpNode extends Node {
    public Expression accept(ExpNodeVisitor v) throws IllegalExpressionException;
}
interface DeclNode extends Node {
    public String getName();
}
interface NodeVisitor {
    public void visit(DeclsNode node);
    public void visit(DefineNode node);
    public void visit(LetNode node);
    public void visit(IfNode node);
    public void visit(UnaryOpNode node);
    public void visit(BinOpNode node);
    public void visit(ApplyNode node);
    public void visit(VarNode node);
    public void visit(IntNode node);
    public void visit(EmptyListNode node);
}
interface ExpNodeVisitor {
    public Expression visit(LetNode node) throws IllegalExpressionException;
    public Expression visit(IfNode node) throws IllegalExpressionException;
    public Expression visit(ApplyNode node) throws IllegalExpressionException;
    public Expression visit(VarNode node) throws IllegalExpressionException;
    public Expression visit(UnaryOpNode node) throws IllegalExpressionException;
    public Expression visit(BinOpNode node) throws IllegalExpressionException;
    public Expression visit(IntNode node) throws IllegalExpressionException;
    public Expression visit(EmptyListNode node) throws IllegalExpressionException;
}

class DeclsNode implements Node {
    private final List<DefineNode> defList;
    DeclsNode() {
        defList = new ArrayList<DefineNode>();
    }
    void add(DefineNode d) {
        if (d != null) {
            defList.add(d);
        }
    }
    void addAll(DeclsNode ds) {
        defList.addAll(ds.defList);
    }
    List<DefineNode> getDefineNodes() {
        return defList;
    }
    int size() {
        return defList.size();
    }
    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }
}
class DefineNode implements DeclNode {
    private final String name;
    private final List<String> args;
    private final ExpNode exp;
    DefineNode(String s, List<String> as, ExpNode e) {
        name = s;
        args = as;
        exp = e;
    }
    List<String> getArgs() {
        return args;
    }
    ExpNode getExpNode() {
        return exp;
    }
    boolean hasArgs() {
        return args.size() > 0;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }
}
class LetNode implements ExpNode {
    private final DeclsNode decls;
    private final ExpNode exp;
    LetNode(DeclsNode ds, ExpNode e) {
        decls = ds;
        exp = e;
    }
    boolean hasDecls() {
        return decls.size() > 0;
    }
    DeclsNode getDeclsNode() {
        return decls;
    }
    ExpNode getExpNode() {
        return exp;
    }
    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }
    @Override
    public Expression accept(ExpNodeVisitor v) throws IllegalExpressionException {
        return v.visit(this);
    }
}
class IfNode implements ExpNode {
    private final ExpNode conNode;
    private final ExpNode thenNode;
    private final ExpNode elseNode;
    IfNode(ExpNode c, ExpNode t, ExpNode e) {
        conNode = c;
        thenNode = t;
        elseNode = e;
    }
    ExpNode getConNode() {
        return conNode;
    }
    ExpNode getThenNode() {
        return thenNode;
    }
    ExpNode getElseNode() {
        return elseNode;
    }
    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }
    @Override
    public Expression accept(ExpNodeVisitor v) throws IllegalExpressionException {
        return v.visit(this);
    }
}
class UnaryOpNode implements ExpNode {
    private final String op;
    private final ExpNode exp;
    UnaryOpNode(String s, ExpNode e) {
        op = s;
        exp = e;
    }
    String getOp() {
        return op;
    }
    ExpNode getBodyNode() {
        return exp;
    }
    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }
    @Override
    public Expression accept(ExpNodeVisitor v) throws IllegalExpressionException {
        return v.visit(this);
    }
}
class BinOpNode implements ExpNode {
    private final String op;
    private final ExpNode left;
    private final ExpNode right;
    BinOpNode(String s, ExpNode l, ExpNode r) {
        op = s;
        left = l;
        right = r;
    }
    String getOp() {
        return op;
    }
    ExpNode getLeft() {
        return left;
    }
    ExpNode getRight() {
        return right;
    }
    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }
    @Override
    public Expression accept(ExpNodeVisitor v) throws IllegalExpressionException {
        return v.visit(this);
    }
}
class ApplyNode implements ExpNode {
    private final ExpNode func;
    private final ExpNode arg;
    ApplyNode(ExpNode f, ExpNode a) {
        func = f;
        arg = a;
    }
    ExpNode getFuncNode() {
        return func;
    }
    ExpNode getArgNode() {
        return arg;
    }
    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }
    @Override
    public Expression accept(ExpNodeVisitor v) throws IllegalExpressionException {
        return v.visit(this);
    }
}
class VarNode implements ExpNode {
    private final String name;
    VarNode(String n) {
        name = n;
    }
    String getName() {
        return name;
    }
    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }
    @Override
    public Expression accept(ExpNodeVisitor v) throws IllegalExpressionException {
        return v.visit(this);
    }
}
class IntNode implements ExpNode {
    private final int value;
    IntNode(int i) {
        value = i;
    }
    int getValue() {
        return value;
    }
    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }
    @Override
    public Expression accept(ExpNodeVisitor v) throws IllegalExpressionException {
        return v.visit(this);
    }
}
class EmptyListNode implements ExpNode {
    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }
    @Override
    public Expression accept(ExpNodeVisitor v) throws IllegalExpressionException {
        return v.visit(this);
    }
}
