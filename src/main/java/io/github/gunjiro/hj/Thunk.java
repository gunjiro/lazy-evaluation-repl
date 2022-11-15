package io.github.gunjiro.hj;
import java.util.*;

class ThunkTable {
    private final ThunkTable parent;
    private final ArrayList<Thunk> list;
    ThunkTable(ThunkTable p, int initCapacity) {
        parent = p;
        list = new ArrayList<Thunk>(initCapacity);
    }
    void add(Thunk t) {
        list.add(t);
    }
    Thunk getThunk(int level, int index) {
        ThunkTable table = this;
        for (int i = 0; i < level; i++) {
            table = table.parent;
            if (table == null) {
                throw new InternalError("The level is over top");
            }
        }
        return table.list.get(index);
    }
}

class Thunk {
    private Closure closure;
    private Value cache;
    Thunk(Expression exp, ThunkTable tt) {
        closure = new Closure(exp, tt);
        cache = null;
    }
    private Value force() throws EvaluationException {
        return closure.eval();
    }
    Value eval() throws EvaluationException {
        if (cache == null) {
            cache = force();
            closure = null;
        }
        return cache;
    }
}

class Closure {
    private final Expression expression;
    private final ThunkTable table;
    Closure(Expression exp, ThunkTable tt) {
        expression = exp;
        table = tt;
    }
    Value eval() throws EvaluationException {
        return expression.evalWith(table);
    }
}
