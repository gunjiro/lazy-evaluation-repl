package io.github.gunjiro;
public class SystemOutMessagePrinter implements MessagePrinter {
    public void printMessage(String message) {
        System.out.println(message);
    }
}
