package redcoder.texteditor.action;

import redcoder.texteditor.MainPane;
import redcoder.texteditor.ScrollTextPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicInteger;

public class NewFileAction extends AbstractAction {

    private final AtomicInteger counter = new AtomicInteger(1);
    private MainPane mainPane;

    public NewFileAction(MainPane mainPane) {
        super("New File");
        this.mainPane = mainPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ScrollTextPane scrollTextPane = new ScrollTextPane(mainPane);
        int i = counter.incrementAndGet();
        mainPane.addActionListener(scrollTextPane);
        mainPane.addTab("* new-" + i, scrollTextPane);
        mainPane.setSelectedComponent(scrollTextPane);
    }
}
