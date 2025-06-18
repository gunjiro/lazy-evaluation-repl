package io.github.gunjiro.hj.app;

import java.io.Reader;
import io.github.gunjiro.hj.DeclsNode;
import io.github.gunjiro.hj.ParseException;
import io.github.gunjiro.hj.Parser;
import io.github.gunjiro.hj.unit.ParsingUnit;

public class AppParsingUnit extends ParsingUnit {

    @Override
    public DeclsNode parse(Reader reader) throws ParseException {
        final Parser parser = new Parser(reader);
        return parser.loadfile();
    }

}
