package redcoder.texteditor.action;

import redcoder.texteditor.core.tabpane.MainTabPane;
import redcoder.texteditor.core.toolbar.ToolbarIconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class NewAction extends AbstractAction {

    private MainTabPane mainTabPane;

    public NewAction(MainTabPane mainTabPane) {
        super("New File");
        this.mainTabPane = mainTabPane;
        Optional.ofNullable(ToolbarIconResource.getImageIcon("New24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainTabPane.createTextPane();
    }

}
