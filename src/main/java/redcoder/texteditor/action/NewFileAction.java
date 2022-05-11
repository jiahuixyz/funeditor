package redcoder.texteditor.action;

import redcoder.texteditor.MainPane;
import redcoder.texteditor.shortcut.ShortcutKeyListener;
import redcoder.texteditor.ScrollTextPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicInteger;

public class NewFileAction extends AbstractAction {

    private final AtomicInteger counter = new AtomicInteger(1);
    private MainPane mainPane;
    private ShortcutKeyListener shortcutKeyListener;

    public NewFileAction(MainPane mainPane, ShortcutKeyListener shortcutKeyListener) {
        super("New File");
        this.mainPane = mainPane;
        this.shortcutKeyListener = shortcutKeyListener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ScrollTextPane scrollTextPane = new ScrollTextPane(shortcutKeyListener);
        int i = counter.incrementAndGet();
        mainPane.addActionListener(scrollTextPane);
        mainPane.addTab("* new-" + i, scrollTextPane);
        mainPane.setSelectedComponent(scrollTextPane);
    }
}
