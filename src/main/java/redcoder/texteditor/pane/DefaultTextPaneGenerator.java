package redcoder.texteditor.pane;

import java.util.concurrent.atomic.AtomicInteger;

public class DefaultTextPaneGenerator implements TextPaneGenerator {

    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void setInitialCounter(int initialValue) {
        counter.set(initialValue);
    }

    @Override
    public void generate(MainTabPane mainTabPane) {
        int i = counter.getAndIncrement();
        String filename = "new-" + i;

        ScrollTextPane scrollTextPane = new ScrollTextPane(mainTabPane, filename);
        mainTabPane.addTab(filename, scrollTextPane, true);
        mainTabPane.setSelectedComponent(scrollTextPane);
    }

}
