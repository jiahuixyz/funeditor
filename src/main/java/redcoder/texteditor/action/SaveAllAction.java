package redcoder.texteditor.action;

import redcoder.texteditor.core.tabpane.MainTabPane;
import redcoder.texteditor.core.toolbar.ToolBarIconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class SaveAllAction extends AbstractAction {

    private MainTabPane mainTabPane;

    public SaveAllAction(MainTabPane mainTabPane) {
        super("Save All");
        this.mainTabPane = mainTabPane;
        Optional.ofNullable(ToolBarIconResource.getImageIcon("SaveAll24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            mainTabPane.saveAllTab();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
