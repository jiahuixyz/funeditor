package redcoder.texteditor.action;

import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;

public class RedoAction extends AbstractAction {

    private UndoManager undoManager;
    private UndoAction undoAction;

    public RedoAction(UndoManager undoManager) {
        super("Redo");
        this.undoManager = undoManager;
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            undoManager.redo();
        } catch (CannotRedoException ex) {
            // System.err.println("Unable to redo: " + ex);
            // ex.printStackTrace();
        }
        updateRedoState();
        undoAction.updateUndoState();
    }

    public void updateRedoState() {
        if (undoManager.canRedo()) {
            setEnabled(true);
            putValue(Action.NAME, undoManager.getRedoPresentationName());
        } else {
            setEnabled(false);
            putValue(Action.NAME, "Redo");
        }
    }

    public UndoManager getUndoManager() {
        return undoManager;
    }

    public void setUndoManager(UndoManager undoManager) {
        this.undoManager = undoManager;
    }

    public UndoAction getUndoAction() {
        return undoAction;
    }

    public void setUndoAction(UndoAction undoAction) {
        this.undoAction = undoAction;
    }
}
