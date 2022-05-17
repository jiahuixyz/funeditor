package redcoder.texteditor.action;

import redcoder.texteditor.core.tabpane.MainTabPane;
import redcoder.texteditor.core.toolbar.ToolbarIconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class SaveAction extends AbstractAction {

    private MainTabPane mainTabPane;

    public SaveAction(MainTabPane mainTabPane) {
        super("Save File");
        this.mainTabPane = mainTabPane;
        Optional.ofNullable(ToolbarIconResource.getImageIcon("Save24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            mainTabPane.saveSelectedTab();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
