package redcoder.texteditor.core.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.resources.IconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewAction extends AbstractAction {

    public NewAction() {
        super("New File");
        putValue(Action.SMALL_ICON, IconResource.getImageIcon("New24.gif"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TabPane tabPane = Framework.getActivatedFrame().getTabPane();
        tabPane.createTextPane();
    }

}
