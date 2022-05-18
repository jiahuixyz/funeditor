package redcoder.texteditor.action;

import redcoder.texteditor.core.file.FileProcessor;
import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.core.textpane.ScrollTextPane;
import redcoder.texteditor.core.toolbar.ToolbarIconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Optional;

public class SaveAsAction extends AbstractAction {

    private final TabPane tabPane;

    public SaveAsAction(TabPane tabPane) {
        super("Save As");
        this.tabPane = tabPane;
        Optional.ofNullable(ToolbarIconResource.getImageIcon("SaveAs24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ScrollTextPane textPane = tabPane.getSelectedTextPane();
        if (textPane != null) {
            File file = FileProcessor.saveTextPaneToAnotherFile(textPane);
            if (file != null) {
                tabPane.replaceSelectedTab(file);
            }
        }
    }
}
