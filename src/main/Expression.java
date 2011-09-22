import java.util.*;

interface Expression {
    public void accept(VoidExpressionVisitor v);
    public Value evalWith(ThunkTable tt) throws EvaluationException;
}
interface VoidExpressionVisitor {
    public void visit(LambdaExp e);
    public void visit(LetExp e);
    public void visit(HeadExp e);
    public void visit(TailExp e);
    public void visit(NegateExp e);
    public void visit(IfExp e);
    public void visit(ApplyExp e);
    public void visit(ConsExp e);
    public void visit(AndExp e);
    public void visit(OrExp e);
    public void visit(EQExp e);
    public void visit(NEQExp e);
    public void visit(LTExp e);
    public void visit(LTEQExp e);
    public void visit(GTExp e);
    public void visit(GTEQExp e);
    public void visit(AddExp e);
    public void visit(SubExp e);
    public void visit(MulExp e);
    public void visit(DivExp e);
    public void visit(ModExp e);
    public void visit(VarExp e);
    public void visit(IntExp e);
    public void visit(EmptyListExp e);
}

class LambdaExp implements Expression {
    private final Expression expression;
    private final int argNum;
    LambdaExp(int n, Expression exp) {
        expression = exp;
        argNum = n;
    }
    int getArgNum() {
        return argNum;
    }
    Expression getDefinition() {
        return expression;
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    public Value evalWith(ThunkTable tt) {
        return new BaseFunction(tt, expression, argNum);
    }
}

class LetExp implements Expression {
    private final Expression expression;
    private final List<Expression> decls;
    LetExp(List<Expression> ds, Expression exp) {
        expression = exp;
        decls = ds;
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    List<Expression> getDecls() {
        return decls;
    }
    Expression getBodyExp() {
        return expression;
    }
    private ThunkTable createThunkTable(ThunkTable parent) {
        ThunkTable table = new ThunkTable(parent, decls.size());
        for (Expression exp : decls) {
            table.add(new Thunk(exp, table));
        }
        return table;
    }
    @Override
    public Value evalWith(ThunkTable tt) throws EvaluationException {
        return this.getBodyExp().evalWith(createThunkTable(tt));
    }
}

class HeadExp implements Expression {
    private final Expression expression;
    HeadExp(Expression e) {
        expression = e;
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    Expression getOperand() {
        return expression;
    }
    @Override
    public Value evalWith(ThunkTable tt) throws EvaluationException {
        return expression.evalWith(tt).getHead();
    }
}

class TailExp implements Expression {
    private final Expression expression;
    TailExp(Expression e) {
        expression = e;
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    Expression getOperand() {
        return expression;
    }
    @Override
    public Value evalWith(ThunkTable tt) throws EvaluationException {
        return expression.evalWith(tt).getTail();
    }
}

class NegateExp implements Expression {
    private final Expression expression;
    NegateExp(Expression e) {
        expression = e;
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    Expression getOperand() {
        return expression;
    }
    @Override
    public Value evalWith(ThunkTable tt) throws EvaluationException {
        return new IntValue(-expression.evalWith(tt).toIntValue().getValue());
    }
}

class ApplyExp implements Expression {
    private final Expression func;
    private final Expression arg;
    ApplyExp(Expression f, Expression a) {
        func = f;
        arg = a;
    }
    Expression getFuncExp() {
        return func;
    }
    Expression getArgExp() {
        return arg;
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    public Value evalWith(ThunkTable tt) throws EvaluationException {
        return func.evalWith(tt).apply(new Thunk(arg, tt));
    }
}

class IfExp implements Expression {
    private final Expression conExp;
    private final Expression thenExp;
    private final Expression elseExp;
    IfExp(Expression c, Expression t, Expression e) {
        conExp = c;
        thenExp = t;
        elseExp = e;
    }
    Expression getConExp() {
        return conExp;
    }
    Expression getThenExp() {
        return thenExp;
    }
    Expression getElseExp() {
        return elseExp;
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    public Value evalWith(ThunkTable tt) throws EvaluationException {
        boolean condition = conExp.evalWith(tt).toIntValue().isTrue();
        return condition ? thenExp.evalWith(tt) : elseExp.evalWith(tt);
    }
}

abstract class BinaryOperator implements Expression {
    private final Expression left;
    private final Expression right;
    BinaryOperator(Expression l, Expression r) {
        left = l;
        right = r;
    }
    Expression getLeft() {
        return left;
    }
    Expression getRight() {
        return right;
    }
    boolean leftIsTrue(ThunkTable tt) throws EvaluationException {
        return getLeft().evalWith(tt).toIntValue().isTrue();
    }
    boolean rightIsTrue(ThunkTable tt) throws EvaluationException {
        return getRight().evalWith(tt).toIntValue().isTrue();
    }
}

class AndExp extends BinaryOperator {
    AndExp(Expression l, Expression r) {
        super(l, r);
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    public Value evalWith(ThunkTable tt) throws EvaluationException {
        return leftIsTrue(tt) && rightIsTrue(tt) ? IntValue.TRUE : IntValue.FALSE;
    }
}

class OrExp extends BinaryOperator {
    OrExp(Expression l, Expression r) {
        super(l, r);
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    public Value evalWith(ThunkTable tt) throws EvaluationException {
        return leftIsTrue(tt) || rightIsTrue(tt) ? IntValue.TRUE : IntValue.FALSE;
    }
}

abstract class CompareOperator extends BinaryOperator {
    enum Order {
        EQ,
        LT,
        GT,
        ;
    }
    CompareOperator(Expression l, Expression r) {
        super(l, r);
    }
    @Override
    public Value evalWith(ThunkTable tt) throws EvaluationException {
        return operate(this.getLeft().evalWith(tt), this.getRight().evalWith(tt)) ? IntValue.TRUE : IntValue.FALSE;
    }
    abstract boolean operate(Value left, Value right) throws EvaluationException;

    Order order(Value left, Value right) throws EvaluationException {
        if (left instanceof IntValue) {
            return order(left.toIntValue(), right.toIntValue());
        }
        else if (left instanceof ListValue) {
            return order(left.toListValue(), right.toListValue());
        }
        else {
            throw new EvaluationException("à¯êîÇéÊÇÈä÷êîÇÕî‰ärÇ≈Ç´Ç‹ÇπÇÒÅB");
        }
    }
    private Order order(IntValue left, IntValue right) {
        int l = left.getValue();
        int r = right.getValue();
        if (l < r) {
            return Order.LT;
        }
        else if(l == r) {
            return Order.EQ;
        }
        else {
            return Order.GT;
        }
    }
    private Order order(ListValue left, ListValue right) throws EvaluationException {
        if (left.isEmpty() && right.isEmpty()) {
            return Order.EQ;
        }
        else if (left.isEmpty() && !right.isEmpty()) {
            return Order.LT;
        }
        else if (!left.isEmpty() && right.isEmpty()) {
            return Order.GT;
        }
        else {
            Order o = order(left.getHead(), right.getHead());
            if (o == Order.EQ) {
                o = order(left.getTail(), right.getTail());
            }
            return o;
        }
    }
}

class EQExp extends CompareOperator {
    EQExp(Expression l, Expression r) {
        super(l, r);
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    boolean operate(Value left, Value right) throws EvaluationException {
        return order(left, right) == Order.EQ;
    }
}

class NEQExp extends CompareOperator {
    NEQExp(Expression l, Expression r) {
        super(l, r);
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    boolean operate(Value left, Value right) throws EvaluationException {
        return order(left, right) != Order.EQ;
    }
}

class LTExp extends CompareOperator {
    LTExp(Expression l, Expression r) {
        super(l, r);
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    boolean operate(Value left, Value right) throws EvaluationException {
        return order(left, right) == Order.LT;
    }
}

class LTEQExp extends CompareOperator {
    LTEQExp(Expression l, Expression r) {
        super(l, r);
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    boolean operate(Value left, Value right) throws EvaluationException {
        return order(left, right) != Order.GT;
    }
}

class GTExp extends CompareOperator {
    GTExp(Expression l, Expression r) {
        super(l, r);
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    boolean operate(Value left, Value right) throws EvaluationException {
        return order(left, right) == Order.GT;
    }
}

class GTEQExp extends CompareOperator {
    GTEQExp(Expression l, Expression r) {
        super(l, r);
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    boolean operate(Value left, Value right) throws EvaluationException {
        return order(left, right) != Order.LT;
    }
}

abstract class IntBinrayOperator extends BinaryOperator {
    IntBinrayOperator(Expression l, Expression r) {
        super(l, r);
    }
    @Override
    public Value evalWith(ThunkTable tt) throws EvaluationException {
        int left = this.getLeft().evalWith(tt).toIntValue().getValue();
        int right = this.getRight().evalWith(tt).toIntValue().getValue();
        return new IntValue(operate(left, right));
    }
    abstract int operate(int x, int y);
}

class AddExp extends IntBinrayOperator {
    AddExp(Expression l, Expression r) {
        super(l, r);
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    int operate(int x, int y) {
        return x + y;
    }
}

class SubExp extends IntBinrayOperator {
    SubExp(Expression l, Expression r) {
        super(l, r);
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    int operate(int x, int y) {
        return x - y;
    }
}

class MulExp extends IntBinrayOperator {
    MulExp(Expression l, Expression r) {
        super(l, r);
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    int operate(int x, int y) {
        return x * y;
    }
}

class DivExp extends IntBinrayOperator {
    DivExp(Expression l, Expression r) {
        super(l, r);
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    int operate(int x, int y) {
        return x / y;
    }
}

class ModExp extends IntBinrayOperator {
    ModExp(Expression l, Expression r) {
        super(l, r);
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    int operate(int x, int y) {
        return x % y;
    }
}

class ConsExp extends BinaryOperator {
    ConsExp(Expression l, Expression r) {
        super(l, r);
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    public Value evalWith(ThunkTable tt) {
        Thunk head = new Thunk(this.getLeft(), tt);
        Thunk tail = new Thunk(this.getRight(), tt);
        return new HeadTailListValue(head, tail);
    }
}

class VarExp implements Expression {
    private final int level;
    private final int index;
    VarExp(int l, int i) {
        level = l;
        index = i;
    }
    // ÉäÉìÉNÇíHÇÈâÒêî
    int getLevel() {
        return level;
    }
    int getIndex() {
        return index;
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    public Value evalWith(ThunkTable tt) throws EvaluationException {
        return tt.getThunk(level, index).eval();
    }
}

class IntExp implements Expression {
    private final IntValue val;
    IntExp(int v) {
        val = new IntValue(v);
    }
    int getValue() {
        return val.getValue();
    }
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    public Value evalWith(ThunkTable tt) {
        return val;
    }
}
class EmptyListExp implements Expression {
    private static final ListValue data = ListValue.EMPTY;
    @Override
    public void accept(VoidExpressionVisitor v) {
        v.visit(this);
    }
    @Override
    public Value evalWith(ThunkTable tt) {
        return data;
    }
}
