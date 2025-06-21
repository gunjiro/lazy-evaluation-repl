package io.github.gunjiro.hj.repl;

import io.github.gunjiro.hj.unit.ManagingStateUnit;
import io.github.gunjiro.hj.unit.TextOutputUnit;
import io.github.gunjiro.hj.unit.ThunkTableUnit;

public class GeneralOperator {
    private final Implementor implementor;

    public GeneralOperator(Implementor implementor) {
        this.implementor = implementor;
    }

    public static interface Implementor {
        public TextOutputUnit createTextOutputUnit();
        public ManagingStateUnit createManagingStateUnit();
        public ThunkTableUnit createThunkTableUnit();
    }

}
