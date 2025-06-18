package io.github.gunjiro.hj.unit;

import java.io.Reader;

import io.github.gunjiro.hj.DeclsNode;
import io.github.gunjiro.hj.ParseException;

public abstract class ParsingUnit {
    public abstract DeclsNode parse(Reader reader) throws ParseException;
}
