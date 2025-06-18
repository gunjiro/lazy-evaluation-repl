package io.github.gunjiro.hj;

import io.github.gunjiro.hj.environment.ThunkTable;

public class Thunk {
    private Closure closure;
    private Value cache;
    Thunk(Expression exp, ThunkTable tt) {
        closure = new Closure(exp, tt);
        cache = null;
    }
    private Value force() throws EvaluationException {
        return closure.eval();
    }
    public Value eval() throws EvaluationException {
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
