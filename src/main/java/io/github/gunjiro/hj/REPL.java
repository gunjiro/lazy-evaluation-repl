package io.github.gunjiro.hj;

public class REPL {
    public static interface Implementor {
        /**
         * 入力待ちをする。
         * @return 入力された文字列
         */
        public String waitForInput();

        /**
         * 入力された文字列を解析して実行する。
         * @param input 入力された文字列
         * @return 入力待ちを繰り返すか終了するか
         */
        public REPL.Result execute(String input);

        /**
         * 終了時のメッセージを表示する。
         */
        public void showQuitMessage();
    }
    
    public static enum Result {
        Continue,
        Quit,
    }

    private final Implementor implementor;

    /**
     * @param implementor 外部で定義する実装
     */
    public REPL(Implementor implementor) {
        this.implementor = implementor;
    }

    /**
     * REPLを実行する。
     */
    public void run() {
        REPL.Result result;

        do {
            final String input = implementor.waitForInput();
            result = implementor.execute(input);
        } while (result.equals(REPL.Result.Continue));

        implementor.showQuitMessage();
    }

}
