package io.github.gunjiro.hj.app;

import java.io.Reader;
import java.io.StringReader;

import io.github.gunjiro.hj.DeclsNode;
import io.github.gunjiro.hj.ParseException;
import io.github.gunjiro.hj.Parser;
import io.github.gunjiro.hj.unit.ParsingUnit;

public class AppParsingUnit extends ParsingUnit {
    private final Parser parser = new Parser(new StringReader(""));

    @Override
    public DeclsNode parse(Reader reader) throws ParseException {
        parser.ReInit(reader);
        return parser.loadfile();
    }

}
