package redcoder.texteditor.action;

import redcoder.texteditor.core.EditorFrame;
import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.findreplace.FindReplace;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class ReplaceAction extends AbstractAction {

    public ReplaceAction() {
        super("Replace");
        Optional.ofNullable(IconResource.getImageIcon("Replace24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        EditorFrame activatedFrame = Framework.getActivatedFrame();
        FindReplace.showReplaceDialog(activatedFrame);
    }
}
