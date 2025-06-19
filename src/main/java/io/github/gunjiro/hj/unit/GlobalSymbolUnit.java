package io.github.gunjiro.hj.unit;

import io.github.gunjiro.hj.environment.GlobalSymbols;

public abstract class GlobalSymbolUnit {
    public abstract void update(GlobalSymbols symbols);
    public abstract GlobalSymbols getGlobalSymbols();
}
