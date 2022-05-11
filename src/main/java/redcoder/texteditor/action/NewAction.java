package redcoder.texteditor.action;

import redcoder.texteditor.MainPane;
import redcoder.texteditor.ScrollTextPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicInteger;

public class NewAction extends AbstractAction {

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
        mainPane.addActionListener(scrollTextPane);
        mainPane.addTab(filename, scrollTextPane);
        mainPane.setSelectedComponent(scrollTextPane);
    }
}
