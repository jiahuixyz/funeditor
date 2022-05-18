package redcoder.texteditor.action;

import redcoder.texteditor.core.file.FileProcessor;
import redcoder.texteditor.core.tabpane.TabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class OpenRecentlyAction extends AbstractAction {

    private TabPane tabPane;
    private File file;

    public OpenRecentlyAction(TabPane tabPane, File file) {
        super(file.getAbsolutePath());
        this.tabPane = tabPane;
        this.file = file;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileProcessor.openFile(tabPane, file, false);
    }
}
