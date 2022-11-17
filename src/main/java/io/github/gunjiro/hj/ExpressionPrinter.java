package io.github.gunjiro.hj;
import java.util.*;

class ExpressionPrinter implements VoidExpressionVisitor {
    private final TreePrinter printer;
    ExpressionPrinter() {
        printer = new TreePrinter(System.out);
    }
    void print(Iterable<Expression> decls) {
        Iterator<Expression> it = decls.iterator();
        if (it.hasNext()) {
            printer.println("#decls#");
            printer.pushT();
            Expression e;
            for (e = it.next(); it.hasNext(); e = it.next()) {
                printer.changeT();
                e.accept(this);
            }
            printer.changeL();
            e.accept(this);
            printer.pop();
        }
    }
    void print(Expression[] decls) {
        print(Arrays.asList(decls));
    }
    public void visit(LambdaExp e) {
        printer.println("#function#");
        printer.pushT();
        {
            printer.println("#args#");
            printer.pushT();
            int n = e.getArgNum() - 1;
            for (int i = 0; i < n; i++) {
                printer.changeT();
                printer.println("#" + i);
            }
            printer.changeL();
            printer.println("#" + n);
            printer.pop();
        }
        printer.changeL();
        e.getDefinition().accept(this);
        printer.pop();
    }
    public void visit(LetExp e) {
        printer.println("#let#");
        printer.pushT();
        print(e.getDecls());
        printer.changeL();
        e.getBodyExp().accept(this);
        printer.pop();
    }
    public void visit(HeadExp e) {
        printer.println("#head#");
        printer.pushL();
        e.getOperand().accept(this);
        printer.pop();
    }
    public void visit(TailExp e) {
        printer.println("#tail#");
        printer.pushL();
        e.getOperand().accept(this);
        printer.pop();
    }
    public void visit(NegateExp e) {
        printer.println("-");
        printer.pushL();
        e.getOperand().accept(this);
        printer.pop();
    }
    public void visit(IfExp e) {
        printer.println("#if#");
        printer.pushT();
        e.getConExp().accept(this);
        printer.changeT();
        e.getThenExp().accept(this);
        printer.changeL();
        e.getElseExp().accept(this);
        printer.pop();
    }
    public void visit(ApplyExp e) {
        printer.println("#apply#");
        printer.pushT();
        e.getFuncExp().accept(this);
        printer.changeL();
        e.getArgExp().accept(this);
        printer.pop();
    }
    public void visit(AndExp e) {
        printer.println("&&");
        printer.pushT();
        e.getLeft().accept(this);
        printer.changeL();
        e.getRight().accept(this);
        printer.pop();
    }
    public void visit(OrExp e) {
        printer.println("||");
        printer.pushT();
        e.getLeft().accept(this);
        printer.changeL();
        e.getRight().accept(this);
        printer.pop();
    }
    public void visit(EQExp e) {
        printer.println("==");
        printer.pushT();
        e.getLeft().accept(this);
        printer.changeL();
        e.getRight().accept(this);
        printer.pop();
    }
    public void visit(NEQExp e) {
        printer.println("/=");
        printer.pushT();
        e.getLeft().accept(this);
        printer.changeL();
        e.getRight().accept(this);
        printer.pop();
    }
    public void visit(LTExp e) {
        printer.println("<");
        printer.pushT();
        e.getLeft().accept(this);
        printer.changeL();
        e.getRight().accept(this);
        printer.pop();
    }
    public void visit(LTEQExp e) {
        printer.println("<=");
        printer.pushT();
        e.getLeft().accept(this);
        printer.changeL();
        e.getRight().accept(this);
        printer.pop();
    }
    public void visit(GTExp e) {
        printer.println(">");
        printer.pushT();
        e.getLeft().accept(this);
        printer.changeL();
        e.getRight().accept(this);
        printer.pop();
    }
    public void visit(GTEQExp e) {
        printer.println(">=");
        printer.pushT();
        e.getLeft().accept(this);
        printer.changeL();
        e.getRight().accept(this);
        printer.pop();
    }
    public void visit(AddExp e) {
        printer.println("+");
        printer.pushT();
        e.getLeft().accept(this);
        printer.changeL();
        e.getRight().accept(this);
        printer.pop();
    }
    public void visit(SubExp e) {
        printer.println("-");
        printer.pushT();
        e.getLeft().accept(this);
        printer.changeL();
        e.getRight().accept(this);
        printer.pop();
    }
    public void visit(MulExp e) {
        printer.println("*");
        printer.pushT();
        e.getLeft().accept(this);
        printer.changeL();
        e.getRight().accept(this);
        printer.pop();
    }
    public void visit(DivExp e) {
        printer.println("/");
        printer.pushT();
        e.getLeft().accept(this);
        printer.changeL();
        e.getRight().accept(this);
        printer.pop();
    }
    public void visit(ModExp e) {
        printer.println("#mod#");
        printer.pushT();
        e.getLeft().accept(this);
        printer.changeL();
        e.getRight().accept(this);
        printer.pop();
    }
    public void visit(ConsExp e) {
        printer.println(":");
        printer.pushT();
        e.getLeft().accept(this);
        printer.changeL();
        e.getRight().accept(this);
        printer.pop();
    }
    public void visit(VarExp e) {
        printer.println("$(" + e.getLevel() + ", " + e.getIndex() + ")");
    }
    public void visit(IntExp e) {
        printer.println(String.valueOf(e.getValue()));
    }
    public void visit(EmptyListExp e) {
        printer.println("[]");
    }
}
