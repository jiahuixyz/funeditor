package redcoder.texteditor.action;

import redcoder.texteditor.core.file.FileProcessor;
import redcoder.texteditor.core.tabpane.MainTabPane;
import redcoder.texteditor.core.toolbar.ToolBarIconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class OpenAction extends AbstractAction {

    private final MainTabPane mainTabPane;

    public OpenAction(MainTabPane mainTabPane) {
        super("Open File");
        this.mainTabPane = mainTabPane;
        Optional.ofNullable(ToolBarIconResource.getImageIcon("Open24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
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
