import java.util.*;
import java.io.*;

class Environment {
    private int initialCapacity;
    private ThunkTable table;
    private Parser parser;
    private NodeToExpression nodeToExpression;
    Environment(int initCapacity) {
        initialCapacity = initCapacity;
        initFunctions();
    }
    void initFunctions() {
        parser = new Parser(new StringReader(""));
        table = new ThunkTable(null, initialCapacity);
        nodeToExpression = new NodeToExpression();
    }
    void addFunctions(Reader reader) throws ApplicationException {
        try {
            parser.ReInit(reader);
            addFunctions(parser.loadfile());
        }
        catch (ParseException e) {
            throw new ApplicationException(e);
        }
    }
    void addFunctions(DeclsNode ds) throws ApplicationException {
        try {
            if (ds.size() > 0) {
                List<Expression> es = nodeToExpression.createExpressions(ds);
                addFunctions(es);
            }
        }
        catch (IllegalExpressionException e) {
            throw new ApplicationException(e);
        }
    }
    private void addFunctions(List<Expression> exps) {
        for (Expression exp : exps) {
            table.add(new Thunk(exp, table));
        }
    }
    Thunk createThunk(Reader reader) throws ApplicationException {
        try {
            parser.ReInit(reader);
            return createThunk(parser.input());
        }
        catch (ParseException e) {
            throw new ApplicationException(e);
        }
    }
    Thunk createThunk(ExpNode node) throws ApplicationException {
        try {
            return new Thunk(createExpression(node), table);
        }
        catch (IllegalExpressionException e) {
            throw new ApplicationException(e);
        }
    }
    private Expression createExpression(ExpNode node) throws IllegalExpressionException {
        return nodeToExpression.createExpression(node);
    }
    private static class NodeToExpression implements ExpNodeVisitor {
        private VarSymbolTable table;
        NodeToExpression() {
            table = new VarSymbolTable();
        }
        List<Expression> createExpressions(DeclsNode decls) throws IllegalExpressionException {
            VarSymbolTable t = new VarSymbolTable(table);
            try {
                return registerDeclares(decls);
            }
            catch (IllegalExpressionException e) {
                table = t;
                throw e;
            }
        }
        Expression createExpression(ExpNode node) throws IllegalExpressionException {
            VarSymbolTable t = new VarSymbolTable(table);
            try {
                return node.accept(this);
            }
            catch (IllegalExpressionException e) {
                table = t;
                throw e;
            }
        }
        private List<Expression> registerDeclares(DeclsNode decls) throws IllegalExpressionException {
            try {
                List<DefineNode> defs = decls.getDefineNodes();
                for (DefineNode d : defs) {
                    table.addSymbol(d.getName());
                }
                List<Expression> exps = new ArrayList<Expression>(defs.size());
                for (DefineNode d : defs) {
                    exps.add(registerDefine(d));
                }
                return exps;
            }
            catch (ConflictionException e) {
                throw new IllegalExpressionException(e);
            }
        }
        private Expression registerDefine(DefineNode def) throws IllegalExpressionException {
            Expression exp;
            if (def.hasArgs()) {
                table.beginScope();
                exp = createLambdaExp(def);
                table.endScope();
            }
            else {
                exp = def.getExpNode().accept(this);
            }
            return exp;
        }
        private Expression createLambdaExp(DefineNode def) throws IllegalExpressionException {
            try {
                List<String> args = def.getArgs();
                for (String s : args) {
                    table.addSymbol(s);
                }
                return new LambdaExp(args.size(), def.getExpNode().accept(this));
            }
            catch (ConflictionException e) {
                throw new IllegalExpressionException(e);
            }
        }
        @Override public Expression visit(LetNode node) throws IllegalExpressionException {
            Expression exp;
            if (node.hasDecls()) {
                table.beginScope();
                List<Expression> decls = registerDeclares(node.getDeclsNode());
                exp = new LetExp(decls, node.getExpNode().accept(this));
                table.endScope();
            }
            else {
                exp = node.getExpNode().accept(this);
            }
            return exp;
        }
        @Override public Expression visit(IfNode node) throws IllegalExpressionException {
            return new IfExp(node.getConNode().accept(this), node.getThenNode().accept(this), node.getElseNode().accept(this));
        }
        @Override public Expression visit(ApplyNode node) throws IllegalExpressionException {
            return new ApplyExp(node.getFuncNode().accept(this), node.getArgNode().accept(this));
        }
        @Override public Expression visit(VarNode node) throws IllegalExpressionException {
            String name = node.getName();
            VarSymbolTable.Property p = table.get(name);
            if (p == null) {
                throw new IllegalExpressionException("êÈåæÇ≥ÇÍÇƒÇ¢Ç»Ç¢éØï éq: \"" + name +"\"");
            }
            return new VarExp(p.getLevel(table.getDepth()), p.getIndex());
        }
        @Override public Expression visit(UnaryOpNode node) throws IllegalExpressionException {
            String op = node.getOp();
            if ("-".equals(op)) {
                return new NegateExp(node.getBodyNode().accept(this));
            }
            else if ("!head".equals(op)) {
                return new HeadExp(node.getBodyNode().accept(this));
            }
            else if ("!tail".equals(op)) {
                return new TailExp(node.getBodyNode().accept(this));
            }
            else {
                throw new IllegalExpressionException("îFéØÇ≈Ç´Ç»Ç¢ââéZéq: \"" + op + "\"");
            }
        }
        @Override public Expression visit(BinOpNode node) throws IllegalExpressionException {
            String op = node.getOp();
            if ("+".equals(op)) {
                return new AddExp(node.getLeft().accept(this), node.getRight().accept(this));
            }
            else if ("-".equals(op)) {
                return new SubExp(node.getLeft().accept(this), node.getRight().accept(this));
            }
            else if ("*".equals(op)) {
                return new MulExp(node.getLeft().accept(this), node.getRight().accept(this));
            }
            else if ("/".equals(op)) {
                return new DivExp(node.getLeft().accept(this), node.getRight().accept(this));
            }
            else if ("!mod".equals(op)) {
                return new ModExp(node.getLeft().accept(this), node.getRight().accept(this));
            }
            else if (":".equals(op)) {
                return new ConsExp(node.getLeft().accept(this), node.getRight().accept(this));
            }
            else if ("==".equals(op)) {
                return new EQExp(node.getLeft().accept(this), node.getRight().accept(this));
            }
            else if ("/=".equals(op)) {
                return new NEQExp(node.getLeft().accept(this), node.getRight().accept(this));
            }
            else if ("<=".equals(op)) {
                return new LTEQExp(node.getLeft().accept(this), node.getRight().accept(this));
            }
            else if (">=".equals(op)) {
                return new GTEQExp(node.getLeft().accept(this), node.getRight().accept(this));
            }
            else if ("<".equals(op)) {
                return new LTExp(node.getLeft().accept(this), node.getRight().accept(this));
            }
            else if (">".equals(op)) {
                return new GTExp(node.getLeft().accept(this), node.getRight().accept(this));
            }
            else if ("&&".equals(op)) {
                return new AndExp(node.getLeft().accept(this), node.getRight().accept(this));
            }
            else if ("||".equals(op)) {
                return new OrExp(node.getLeft().accept(this), node.getRight().accept(this));
            }
            else {
                throw new IllegalExpressionException("îFéØÇ≈Ç´Ç»Ç¢ââéZéq: \"" + op + "\"");
            }
        }
        @Override public Expression visit(IntNode node) throws IllegalExpressionException {
            return new IntExp(node.getValue());
        }
        @Override public Expression visit(EmptyListNode node) throws IllegalExpressionException {
            return new EmptyListExp();
        }
    }
}


class VarSymbolTable extends SymbolTable<VarSymbolTable.Property> {
    VarSymbolTable() {
        super();
    }
    VarSymbolTable(VarSymbolTable table) {
        super(table);
    }
    void addSymbol(String symbol) throws ConflictionException {
        set(symbol, new Property(getDepth(), getNextIndex()));
    }
    static class Property {
        private final int depth;
        private final int index;
        private Property(int d, int i) {
            depth = d;
            index = i;
        }
        // ÉäÉìÉNÇíHÇÈâÒêî
        int getLevel(int currentDepth) {
            return currentDepth - depth;
        }
        int getIndex() {
            return index;
        }
    }
}
