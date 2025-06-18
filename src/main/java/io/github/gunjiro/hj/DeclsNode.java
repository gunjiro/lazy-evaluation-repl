package io.github.gunjiro.hj;

import java.util.ArrayList;
import java.util.List;

public class DeclsNode implements Node {
    private final List<DefineNode> defList;
    DeclsNode() {
        defList = new ArrayList<DefineNode>();
    }
    void add(DefineNode d) {
        if (d != null) {
            defList.add(d);
        }
    }
    void addAll(DeclsNode ds) {
        defList.addAll(ds.defList);
    }
    List<DefineNode> getDefineNodes() {
        return defList;
    }
    int size() {
        return defList.size();
    }
    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }
}