package redcoder.texteditor.action;

import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.core.toolbar.ToolbarIconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class SaveAllAction extends AbstractAction {

    private TabPane tabPane;

    public SaveAllAction(TabPane tabPane) {
        super("Save All");
        this.tabPane = tabPane;
        Optional.ofNullable(ToolbarIconResource.getImageIcon("SaveAll24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            tabPane.saveAllTab();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
