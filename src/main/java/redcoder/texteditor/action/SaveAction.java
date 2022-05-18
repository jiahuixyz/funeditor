package redcoder.texteditor.action;

import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.core.toolbar.ToolbarIconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class SaveAction extends AbstractAction {

    private TabPane tabPane;

    public SaveAction(TabPane tabPane) {
        super("Save File");
        this.tabPane = tabPane;
        Optional.ofNullable(ToolbarIconResource.getImageIcon("Save24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            tabPane.saveSelectedTab();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
