package io.github.gunjiro.hj.command.operator;

import io.github.gunjiro.hj.command.LoadCommand;

class LoadCommandAction {
    static interface Implementor {
        public void load(String name);
    }

    private final Implementor implementor;

    LoadCommandAction(Implementor implementor) {
        this.implementor = implementor;
    }

    void take(LoadCommand command) {
        for (String name : command.getResourceNames()) {
            take(name);
        }
    }

    private void take(String name) {
        implementor.load(name);
    }
}
