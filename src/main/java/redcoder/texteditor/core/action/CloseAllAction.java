package redcoder.texteditor.core.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.resources.IconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CloseAllAction extends AbstractAction {

    public CloseAllAction(){
        super("Close All");
        putValue(Action.SMALL_ICON, IconResource.getImageIcon("close_all24.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TabPane tabPane = Framework.getActivatedFrame().getTabPane();
        tabPane.closeAllTab();
    }
}
