package redcoder.texteditor.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.resources.IconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CloseAction extends AbstractAction {

    public CloseAction() {
        super("Close File");
        putValue(Action.SMALL_ICON, IconResource.getImageIcon("close24.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TabPane tabPane = Framework.getActivatedFrame().getTabPane();
        tabPane.closeSelectedTab();
    }
}
