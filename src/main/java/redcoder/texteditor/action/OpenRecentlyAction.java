package redcoder.texteditor.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.tabpane.MainTabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class OpenRecentlyAction extends AbstractAction {

    private MainTabPane mainTabPane;
    private File file;

    public OpenRecentlyAction(MainTabPane mainTabPane, File file) {
        super(file.getAbsolutePath());
        this.mainTabPane = mainTabPane;
        this.file = file;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Framework.INSTANCE.getFileProcessor().openFile(mainTabPane, file, false);
    }
}
