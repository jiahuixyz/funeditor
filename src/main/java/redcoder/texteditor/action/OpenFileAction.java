package redcoder.texteditor.action;

import redcoder.texteditor.MainPane;
import redcoder.texteditor.shortcut.ShortcutKeyListener;
import redcoder.texteditor.ScrollTextPane;
import redcoder.texteditor.utils.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class OpenFileAction extends AbstractAction {

    private MainPane mainPane;
    private ShortcutKeyListener shortcutKeyListener;
    private JFileChooser fileChooser;

    public OpenFileAction(MainPane mainPane, ShortcutKeyListener shortcutKeyListener, JFileChooser fileChooser) {
        super("Open File");
        this.mainPane = mainPane;
        this.shortcutKeyListener = shortcutKeyListener;
        this.fileChooser = fileChooser;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int state = fileChooser.showOpenDialog(mainPane);
        if (state == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            openFile(file.getName(), FileUtils.readFile(file));
        }
    }

    private void openFile(String filename, String fileContent) {
        ScrollTextPane scrollTextPane = new ScrollTextPane(shortcutKeyListener);
        scrollTextPane.getTextPane().setText(fileContent);

        mainPane.addActionListener(scrollTextPane);
        mainPane.addTab(filename, scrollTextPane);
        mainPane.setSelectedComponent(scrollTextPane);
    }
}
