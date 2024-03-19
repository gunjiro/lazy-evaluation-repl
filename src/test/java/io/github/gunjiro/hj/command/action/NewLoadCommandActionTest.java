package io.github.gunjiro.hj.command.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import io.github.gunjiro.hj.command.LoadCommand;

public class NewLoadCommandActionTest {
    @Test
    public void takesLoadCommand() {
        // 読み込みコマンドを処理する。
        // このテストでは複数のリソースを読み込んだとき、その分だけ読み込み処理が行われることを確認する。
        final LoadCommand command = new LoadCommand(List.of("resource1", "resource2"));
        final LinkedList<String> results = new LinkedList<String>();
        final NewLoadCommandAction action = new NewLoadCommandAction(new NewLoadCommandAction.Implementor() {

            @Override
            public void load(String name) {
                results.add(String.format("loaded: %s", name));
            }
            
        });

        action.take(command);

        assertThat(results, is(List.of("loaded: resource1", "loaded: resource2")));
    }
}