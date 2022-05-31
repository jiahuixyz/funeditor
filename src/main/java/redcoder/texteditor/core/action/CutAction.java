package redcoder.texteditor.core.action;

import redcoder.texteditor.resources.IconResource;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import java.awt.event.ActionEvent;

public class CutAction extends TextAction {

    public CutAction() {
        super("Cut");
        putValue(Action.SMALL_ICON, IconResource.getImageIcon("Cut24.gif"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTextComponent target = getTextComponent(e);
        if (target != null) {
            target.cut();
        }
    }
}
