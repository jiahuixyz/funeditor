package redcoder.texteditor.action;

import redcoder.texteditor.core.EditorFrame;
import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.find.FindDialog;
import redcoder.texteditor.core.toolbar.ToolbarIconResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class FindAction extends AbstractAction {

    public FindAction() {
        super("Find");
        Optional.ofNullable(ToolbarIconResource.getImageIcon("Find24.gif"))
                .ifPresent(icon -> putValue(Action.SMALL_ICON, icon));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        EditorFrame activatedFrame = Framework.getActivatedFrame();
        FindDialog.showDialog(activatedFrame);
    }
}
