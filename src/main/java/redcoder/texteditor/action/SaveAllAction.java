package redcoder.texteditor.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.resources.IconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SaveAllAction extends AbstractAction {

    public SaveAllAction() {
        super("Save All");
        putValue(Action.SMALL_ICON, IconResource.getImageIcon("SaveAll24.gif"));
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
