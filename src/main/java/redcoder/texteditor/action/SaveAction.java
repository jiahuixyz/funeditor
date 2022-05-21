package redcoder.texteditor.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.resources.IconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SaveAction extends AbstractAction {

    public SaveAction() {
        super("Save File");
        putValue(Action.SMALL_ICON, IconResource.getImageIcon("Save24.gif"));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            TabPane tabPane = Framework.getActivatedFrame().getTabPane();
            tabPane.saveSelectedTab();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
