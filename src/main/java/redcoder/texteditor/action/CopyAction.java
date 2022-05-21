package redcoder.texteditor.action;

import redcoder.texteditor.resources.IconResource;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import java.awt.event.ActionEvent;

public class CopyAction extends TextAction {

    public CopyAction() {
        super("Copy");
        putValue(Action.SMALL_ICON, IconResource.getImageIcon("Copy24.gif"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTextComponent target = getTextComponent(e);
        if (target != null) {
            target.copy();
        }
    }
}
