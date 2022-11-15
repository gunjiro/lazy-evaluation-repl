package io.github.gunjiro.hj;
import java.io.*;
import java.util.*;

class TreePrinter {
    private PrintStream printer;
    private final LinkedList<BranchChar> list;
    public TreePrinter(PrintStream ps) {
        printer = ps;
        list = new LinkedList<BranchChar>();
    }
    public void println(String s) {
        for (BranchChar bc : list) {
            printer.print(bc.getChar());
        }
        printer.println(s);
    }
    public void pushT() {
        list.addLast(new BranchChar(BranchChar.T));
    }
    public void pushL() {
        list.addLast(new BranchChar(BranchChar.L));
    }
    public void changeT() {
        list.getLast().setChar(BranchChar.T);
    }
    public void changeL() {
        list.getLast().setChar(BranchChar.L);
    }
    public void pop() {
        list.removeLast();
    }
    private static class BranchChar {
        private static final char T = '├';
        private static final char L = '└';
        private static final char BAR = '｜';
        private static final char SPACE = '　';
        private char character;
        private BranchChar(char c) {
            character = c;
        }
        private char getChar() {
            char c = character;
            switch (character) {
                case T:
                    character = BAR;
                    break;
                case L:
                    character = SPACE;
                    break;
            }
            return c;
        }
        private void setChar(char c) {
            character = c;
        }
    }
}
