package io.github.gunjiro.hj.command;

import java.util.List;

import io.github.gunjiro.hj.ExitException;

public class LoadCommand implements Command {
    private final List<String> resourceNames;

    public LoadCommand(List<String> resourceNames) {
        this.resourceNames = resourceNames;
    }

    public List<String> getResourceNames() {
        return resourceNames;
    }

    @Override
    public <R> R accept(Command.Visitor<R> visitor) throws ExitException {
        return visitor.visit(this);
    }
}