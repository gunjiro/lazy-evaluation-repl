class ValuePrinter {
    void print(Value value) throws ApplicationException {
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
        System.out.print(value.getValue());
    }
    private void printList(ListValue list) throws ApplicationException {
        try {
            System.out.print("[");
            if (!list.isEmpty()) {
                print(list.getHead());
                for (list = list.getTail(); !list.isEmpty(); list = list.getTail()) {
                    System.out.print(',');
                    print(list.getHead());
                }
            }
            System.out.print("]");
        }
        catch (EvaluationException e) {
            throw new ApplicationException(e);
        }
    }
}
