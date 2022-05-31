package redcoder.texteditor.core.action;

import redcoder.texteditor.core.EditorFrame;
import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.findreplace.FindReplace;
import redcoder.texteditor.resources.IconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ReplaceAction extends AbstractAction {

    public ReplaceAction() {
        super("Replace");
        putValue(Action.SMALL_ICON, IconResource.getImageIcon("Replace24.gif"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        EditorFrame activatedFrame = Framework.getActivatedFrame();
        if (activatedFrame.getTabPane().getSelectedTextPane() != null) {
            FindReplace.showReplaceDialog(activatedFrame);
        }
    }
}
