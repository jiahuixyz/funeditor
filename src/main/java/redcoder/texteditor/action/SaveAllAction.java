package redcoder.texteditor.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.resources.IconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveAllAction extends AbstractAction {

    private static final Logger LOGGER = Logger.getLogger(SaveAllAction.class.getName());

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
            LOGGER.log(Level.SEVERE, "SaveAllAction", e);
        }
    }

}
