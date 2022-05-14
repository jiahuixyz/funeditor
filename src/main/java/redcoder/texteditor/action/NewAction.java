package redcoder.texteditor.action;

import redcoder.texteditor.pane.MainPane;
import redcoder.texteditor.pane.ScrollTextPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicInteger;

public class NewAction extends AbstractAction {

    // fixme
    private final AtomicInteger counter = new AtomicInteger(1);
    private MainPane mainPane;

    public NewAction(MainPane mainPane) {
        super("New File");
        this.mainPane = mainPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int i = counter.incrementAndGet();
        String filename = "new-" + i;

        ScrollTextPane scrollTextPane = new ScrollTextPane(mainPane, filename);
        mainPane.addTab(filename, scrollTextPane, true);
        mainPane.setSelectedComponent(scrollTextPane);
    }
}
