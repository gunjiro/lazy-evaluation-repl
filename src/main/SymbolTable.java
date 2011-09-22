import java.util.*;

class SymbolTable<T> {
    private final StackHashTable<T> table;
    private final LinkedList<TreeSet<String>> stack;
    private int depth;
    public SymbolTable() {
        table = new StackHashTable<T>();
        stack = new LinkedList<TreeSet<String>>();
        stack.push(new TreeSet<String>());
        depth = 0;
    }
    public SymbolTable(SymbolTable<T> t) {
        table = new StackHashTable<T>(t.table);
        stack = new LinkedList<TreeSet<String>>();
        for (TreeSet<String> set : t.stack) {
            stack.push(new TreeSet<String>(set));
        }
        depth = t.depth;
    }
    public void set(String s, T value) throws ConflictionException {
        TreeSet<String> symbols = stack.peek();
        if (symbols.contains(s)) {
            throw new ConflictionException(String.format("Conflicted Symbol: \"%s\"", s));
        }
        symbols.add(s);
        table.set(s, value);
    }
    public T get(String s) {
        return table.get(s);
    }
    public int getNextIndex() {
        return stack.peek().size();
    }
    public int getDepth() {
        return depth;
    }
    public void beginScope() {
        stack.push(new TreeSet<String>());
        depth++;
    }
    public void endScope() {
        if (depth < 1) {
            throw new IllegalStateException("It can't close in top level scope.");
        }
        for (String s : stack.pop()) {
            table.pop(s);
        }
        depth--;
    }
}

class StackHashTable<T> {
    private final HashMap<String, Stack> table;
    public StackHashTable() {
        table = new HashMap<String, Stack>();
    }
    public StackHashTable(StackHashTable<T> t) {
        table = new HashMap<String, Stack>(t.table);
    }
    public void set(String s, T value) {
        table.put(s, new Stack(value, table.get(s)));
    }
    public T get(String s) {
        Stack stack = table.get(s);
        return (stack == null) ? null : stack.value;
    }
    public void pop(String s) {
        table.put(s, table.get(s).next);
    }
    private class Stack {
        final T value;
        final Stack next;
        Stack(T v, Stack s) {
            value = v;
            next = s;
        }
    }
}

class ConflictionException extends Exception {
    ConflictionException() {
        super();
    }
    ConflictionException(String message) {
        super(message);
    }
    ConflictionException(String message, Throwable cause) {
        super(message, cause);
    }
    ConflictionException(Throwable cause) {
        super(cause);
    }
}
