package redcoder.texteditor.action;

import redcoder.texteditor.core.file.FileProcessor;
import redcoder.texteditor.core.tabpane.MainTabPane;
import redcoder.texteditor.core.textpane.ScrollTextPane;
import redcoder.texteditor.core.toolbar.ToolbarIconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Optional;

public class SaveAsAction extends AbstractAction {

    private final MainTabPane mainTabPane;

    public SaveAsAction(MainTabPane mainTabPane) {
        super("Save As");
        this.mainTabPane = mainTabPane;
        Optional.ofNullable(ToolbarIconResource.getImageIcon("SaveAs24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ScrollTextPane textPane = mainTabPane.getSelectedTextPane();
        if (textPane != null) {
            File file = FileProcessor.saveTextPaneToAnotherFile(textPane);
            if (file != null) {
                mainTabPane.replaceSelectedTab(file);
            }
        }
    }
}
