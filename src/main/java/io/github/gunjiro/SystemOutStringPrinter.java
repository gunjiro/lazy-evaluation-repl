package io.github.gunjiro;
public class SystemOutStringPrinter implements StringPrinter {
    @Override
    public void print(String s) {
        System.out.print(s);
    }
}
