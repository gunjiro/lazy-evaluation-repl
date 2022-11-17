package io.github.gunjiro.hj;
public class SystemOutStringPrinter implements StringPrinter {
    @Override
    public void print(String s) {
        System.out.print(s);
    }
}
