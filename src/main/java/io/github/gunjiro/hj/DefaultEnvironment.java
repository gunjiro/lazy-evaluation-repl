package io.github.gunjiro.hj;

public class DefaultEnvironment extends Environment {
    public DefaultEnvironment() {
        super(100);
    }
    @Override void initFunctions() {
        super.initFunctions();
        addDefaultFunctions();
    }
    private void addDefaultFunctions() {
        try {
            addFunctions(createDeclsNode());
        }
        catch (ApplicationException e) {
            throw new InternalError(e.getMessage());
        }
    }
    private DeclsNode createDeclsNode() {
        return new DefaultDeclsNode();
    }
}