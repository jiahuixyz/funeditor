package redcoder.texteditor.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.tabpane.TabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class SaveAllAction extends AbstractAction {

    public SaveAllAction() {
        super("Save All");
        Optional.ofNullable(IconResource.getImageIcon("SaveAll24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            TabPane tabPane = Framework.getActivatedFrame().getTabPane();
            tabPane.saveAllTab();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
