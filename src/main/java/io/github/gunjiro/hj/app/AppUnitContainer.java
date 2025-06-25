package io.github.gunjiro.hj.app;

import io.github.gunjiro.hj.unit.FileOpenUnit;
import io.github.gunjiro.hj.unit.InputUnit;
import io.github.gunjiro.hj.unit.ManagingStateUnit;
import io.github.gunjiro.hj.unit.TextOutputUnit;
import io.github.gunjiro.hj.unit.ThunkTableUnit;

public class AppUnitContainer {
    private final TextOutputUnit textOutputUnit = new AppTextOutputUnit();
    private final ManagingStateUnit managingStateUnit = new AppManagingStateUnit();
    private final InputUnit inputUnit = new AppInputUnit();
    private final ThunkTableUnit thunkTableUnit = new AppThunkTableUnit();
    private final FileOpenUnit fileOpenUnit = new AppFileOpenUnit();

    public TextOutputUnit getTextOutputUnit() {
        return textOutputUnit;
    }

    public ManagingStateUnit getManagingStateUnit() {
        return managingStateUnit;
    }

    public InputUnit getInputUnit() {
        return inputUnit;
    }

    public ThunkTableUnit getThunkTableUnit() {
        return thunkTableUnit;
    }

    public FileOpenUnit getFileOpenUnit() {
        return fileOpenUnit;
    }
}
