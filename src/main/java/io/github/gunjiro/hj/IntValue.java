package io.github.gunjiro.hj;

public class IntValue implements Value {
    public static final IntValue TRUE = new IntValue(1);
    public static final IntValue FALSE = new IntValue(0);
    private final int value;
    IntValue(int val) {
        value = val;
    }
    @Override
    public Value apply(Thunk arg) throws ApplyException {
        throw new ApplyException("整数に引数を与えることはできません。");
    }
    public boolean isTrue() {
        return (value != 0) ? true : false;
    }
    @Override
    public Value getHead() throws ListOperationException {
        throw new ListOperationException("整数からheadを取ることはできません。");
    }
    @Override
    public ListValue getTail() throws ListOperationException {
        throw new ListOperationException("整数からtailを取ることはできません。");
    }
    @Override
    public IntValue toIntValue() {
        return this;
    }
    @Override
    public ListValue toListValue() throws ListOperationException {
        throw new ListOperationException("整数はリストに変換できません。");
    }
    public int getValue() {
        return value;
    }
}