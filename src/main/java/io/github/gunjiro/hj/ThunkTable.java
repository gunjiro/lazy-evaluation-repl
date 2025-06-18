package io.github.gunjiro.hj;

import java.util.ArrayList;

public class ThunkTable {
    private final ThunkTable parent;
    private final ArrayList<Thunk> list;
    ThunkTable(ThunkTable p, int initCapacity) {
        parent = p;
        list = new ArrayList<>(initCapacity);
    }
    void add(Thunk t) {
        list.add(t);
    }
    Thunk getThunk(int level, int index) {
        ThunkTable table = this;
        for (int i = 0; i < level; i++) {
            table = table.parent;
            if (table == null) {
                throw new InternalError("The level is over top");
            }
        }
        return table.list.get(index);
    }
}