package redcoder.texteditor.action;

import javax.swing.*;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UndoAction extends AbstractAction {

    private static final Logger LOGGER = Logger.getLogger(UndoAction.class.getName());

    private UndoManager undoManager;
    private RedoAction redoAction;

    public UndoAction(UndoManager undoManager) {
        super("Undo");
        this.undoManager = undoManager;
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            undoManager.undo();
        } catch (CannotUndoException ex) {
            LOGGER.log(Level.SEVERE, "UndoAction", e);
        }
        updateUndoState();
        redoAction.updateRedoState();
    }

    public void updateUndoState() {
        if (undoManager.canUndo()) {
            setEnabled(true);
            putValue(Action.NAME, undoManager.getUndoPresentationName());
        } else {
            setEnabled(false);
            putValue(Action.NAME, "Undo");
        }
    }

    public UndoManager getUndoManager() {
        return undoManager;
    }

    public void setUndoManager(UndoManager undoManager) {
        this.undoManager = undoManager;
    }

    public RedoAction getRedoAction() {
        return redoAction;
    }

    public void setRedoAction(RedoAction redoAction) {
        this.redoAction = redoAction;
    }
}
