public class ValuePrinterFactory {
    public ValuePrinter create() {
        return new ValuePrinter(new SystemOutStringPrinter());
    }
}
