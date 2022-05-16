package redcoder.texteditor.action;

import redcoder.texteditor.core.file.FileProcessor;
import redcoder.texteditor.core.tabpane.MainTabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OpenAction extends AbstractAction {

    private final MainTabPane mainTabPane;

    public OpenAction(MainTabPane mainTabPane) {
        super("Open File");
        this.mainTabPane = mainTabPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            FileProcessor.openFile(mainTabPane);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
