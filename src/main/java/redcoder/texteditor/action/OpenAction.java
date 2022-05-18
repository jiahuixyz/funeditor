package redcoder.texteditor.action;

import redcoder.texteditor.core.file.FileProcessor;
import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.core.toolbar.ToolbarIconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class OpenAction extends AbstractAction {

    private final TabPane tabPane;

    public OpenAction(TabPane tabPane) {
        super("Open File");
        this.tabPane = tabPane;
        Optional.ofNullable(ToolbarIconResource.getImageIcon("Open24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            FileProcessor.openFile(tabPane);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
