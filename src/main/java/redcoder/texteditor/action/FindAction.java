package redcoder.texteditor.action;

import redcoder.texteditor.core.EditorFrame;
import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.findreplace.FindDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class FindAction extends AbstractAction {

    public FindAction() {
        super("Find");
        Optional.ofNullable(IconResource.getImageIcon("Find24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        EditorFrame activatedFrame = Framework.getActivatedFrame();
        FindDialog.showDialog(activatedFrame);
    }
}
