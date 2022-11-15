package io.github.gunjiro;
public class ValuePrinter {
    private final StringPrinter printer;

    public ValuePrinter(StringPrinter printer) {
        this.printer = printer;
    }

    public void print(Value value) throws ApplicationException {
        if (value instanceof IntValue) {
            printInt((IntValue)value);
        }
        else if (value instanceof ListValue) {
            printList((ListValue)value);
        }
        else {
            throw new ApplicationException("Unsupported Type For Printing");
        }
    }

    private void printInt(IntValue value) {
        printer.print(String.valueOf(value.getValue()));
    }

    private void printList(ListValue list) throws ApplicationException {
        try {
            printer.print("[");
            if (!list.isEmpty()) {
                print(list.getHead());
                for (list = list.getTail(); !list.isEmpty(); list = list.getTail()) {
                    printer.print(",");
                    print(list.getHead());
                }
            }
            printer.print("]");
        }
        catch (EvaluationException e) {
            throw new ApplicationException(e);
        }
    }
}
