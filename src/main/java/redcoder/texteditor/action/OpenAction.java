package redcoder.texteditor.action;

import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.file.FileProcessor;
import redcoder.texteditor.core.tabpane.TabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class OpenAction extends AbstractAction {

    public OpenAction() {
        super("Open File");
        Optional.ofNullable(IconResource.getImageIcon("Open24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            TabPane tabPane = Framework.getActivatedFrame().getTabPane();
            FileProcessor.openFile(tabPane);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
