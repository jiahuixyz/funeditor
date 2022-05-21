package redcoder.texteditor.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.file.FileProcessor;
import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.core.textpane.ScrollTextPane;
import redcoder.texteditor.resources.IconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class SaveAsAction extends AbstractAction {

    public SaveAsAction() {
        super("Save As");
        putValue(Action.SMALL_ICON, IconResource.getImageIcon("SaveAs24.gif"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TabPane tabPane = Framework.getActivatedFrame().getTabPane();
        ScrollTextPane textPane = tabPane.getSelectedTextPane();
        if (textPane != null) {
            File file = FileProcessor.saveTextPaneToAnotherFile(textPane);
            if (file != null) {
                tabPane.replaceSelectedTab(file);
            }
        }
    }
}
