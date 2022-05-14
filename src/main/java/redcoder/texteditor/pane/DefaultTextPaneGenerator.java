package redcoder.texteditor.pane;

import redcoder.texteditor.statusbar.StatusBar;

import java.util.concurrent.atomic.AtomicInteger;

public class DefaultTextPaneGenerator implements TextPaneGenerator {

    private final AtomicInteger counter = new AtomicInteger(0);
    private StatusBar statusBar;

    public DefaultTextPaneGenerator(StatusBar statusBar) {
        this.statusBar = statusBar;
    }

    @Override
    public void setInitialCounter(int initialValue) {
        counter.set(initialValue);
    }

    @Override
    public void generate(MainTabPane mainTabPane) {
        int i = counter.getAndIncrement();
        String filename = "new-" + i;

        ScrollTextPane scrollTextPane = new ScrollTextPane(statusBar, mainTabPane, filename);
        mainTabPane.addTab(filename, scrollTextPane, true);
        mainTabPane.setSelectedComponent(scrollTextPane);
    }

}
