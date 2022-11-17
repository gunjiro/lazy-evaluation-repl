package io.github.gunjiro.hj;
import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class LineReaderTest {
    // readLineでIOExceptionが投げられたらIOErrorにして投げる
    @Test(expected = IOError.class)
    public void readShouldThrowIOErrorWhenReadLineThrowsIOException() {
        final BufferedReader br = new BufferedReader(new StringReader("")) {
            @Override
            public String readLine() throws IOException {
                throw new IOException();
            }
        };
        final LineReader reader = new LineReader();

        reader.read(br);
    }

    // readLineでnullが返されたら、IOErrorを投げる
    @Test(expected = IOError.class)
    public void readShouldThrowIOErrorWhenReadLineReturnsNull() {
        final BufferedReader br = new BufferedReader(new StringReader("")) {
            @Override
            public String readLine() throws IOException {
                return null;
            }
        };
        final LineReader reader = new LineReader();

        reader.read(br);
    }
}
