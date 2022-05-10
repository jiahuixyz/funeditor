package redcoder.texteditor.action;

import redcoder.texteditor.MainPane;
import redcoder.texteditor.ShortcutKeyListener;
import redcoder.texteditor.TextPane;

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
        TextPane textPane = new TextPane(shortcutKeyListener);
        int i = counter.incrementAndGet();
        mainPane.addActionListener(textPane);
        mainPane.addTab("* new-" + i, textPane);
        mainPane.setSelectedComponent(textPane);
    }
}
