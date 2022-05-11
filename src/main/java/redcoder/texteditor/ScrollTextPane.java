package redcoder.texteditor;

import redcoder.texteditor.action.ActionName;
import redcoder.texteditor.action.RedoAction;
import redcoder.texteditor.action.UndoAction;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Objects;

import static redcoder.texteditor.action.ActionName.*;

/**
 * 支持滚动的文本窗格
 */
public class ScrollTextPane extends JScrollPane implements ActionListener {

    private boolean modified;
    private String filename;
    private JTextPane textPane;
    private UndoManager undoManager;
    private UndoAction undoAction;
    private RedoAction redoAction;

    private int index;

    public ScrollTextPane(MainPane mainPane, String filename) {
        this(mainPane, filename, false);
    }

    public ScrollTextPane(MainPane mainPane, String filename, boolean modified) {
        super();
        this.filename = filename;
        this.modified = modified;

        initAction();
        textPane = createTextPane(mainPane);
        setViewportView(textPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() != textPane) {
            return;
        }

        String command = e.getActionCommand();
        if (Objects.equals("Undo", command)) {
            undoAction.actionPerformed(e);
        } else if (Objects.equals("Redo", command)) {
            redoAction.actionPerformed(e);
        }
    }

    public void updateIndex(int index) {
        this.index = index;
    }

    // ---------- getter, setter
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean isModified() {
        return modified;
    }

    public JTextPane getTextPane() {
        return textPane;
    }

    public UndoAction getUndoAction() {
        return undoAction;
    }

    public RedoAction getRedoAction() {
        return redoAction;
    }

    public int getIndex() {
        return index;
    }

    // ------------------- init
    private void initAction() {
        undoManager = new UndoManager();
        undoAction = new UndoAction(undoManager);
        redoAction = new RedoAction(undoManager);

        undoAction.setRedoAction(redoAction);
        redoAction.setUndoAction(undoAction);
    }

    private JTextPane createTextPane(MainPane mainPane) {
        JTextPane textPane = new JTextPane();
        textPane.setFont(mainPane.getStpFont());

        StyledDocument doc = textPane.getStyledDocument();
        doc.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                modified = true;
                mainPane.updateTabbedTitle(index, "* " + getFilename());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                modified = true;
                mainPane.updateTabbedTitle(index, "* " + getFilename());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                modified = true;
                mainPane.updateTabbedTitle(index, "* " + getFilename());
            }
        });
        doc.addUndoableEditListener(e -> {
            undoManager.addEdit(e.getEdit());
            undoAction.updateUndoState();
            redoAction.updateRedoState();
        });

        registerActionMap(mainPane.getDefaultActions(), textPane);
        addKeyBinding(textPane);

        return textPane;
    }

    private void registerActionMap(Map<ActionName, Action> defaultActions, JTextPane textPane) {
        ActionMap actionMap = textPane.getActionMap();
        for (Map.Entry<ActionName, Action> entry : defaultActions.entrySet()) {
            actionMap.put(entry.getKey(), entry.getValue());
        }
    }

    private void addKeyBinding(JTextPane textPane) {
        InputMap inputMap = textPane.getInputMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), UNDO);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), REDO);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, InputEvent.CTRL_DOWN_MASK), ZOOM_IN);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_DOWN_MASK), ZOOM_OUT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), NEW_FILE);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK), OPEN_FILE);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), SAVE_FILE);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK), CLOSE);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), CLOSE_ALL);
    }
}
