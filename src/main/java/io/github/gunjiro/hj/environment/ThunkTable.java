package io.github.gunjiro.hj.environment;

import java.util.ArrayList;

import io.github.gunjiro.hj.Thunk;

public class ThunkTable {
    private final ThunkTable parent;
    private final ArrayList<Thunk> list;
    public ThunkTable(ThunkTable p, int initCapacity) {
        parent = p;
        list = new ArrayList<>(initCapacity);
    }
    public void add(Thunk t) {
        list.add(t);
    }
    public Thunk getThunk(int level, int index) {
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