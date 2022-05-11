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
import java.io.File;
import java.util.Map;
import java.util.Objects;

import static redcoder.texteditor.action.ActionName.REDO;
import static redcoder.texteditor.action.ActionName.UNDO;

/**
 * 支持滚动的文本窗格
 */
public class ScrollTextPane extends JScrollPane implements ActionListener {

    private String filename;
    // 表示文本内容是否被修改
    private boolean modified;
    // ModifyAwareDocumentListener的文本内容变化感知功能是否开启
    private boolean modifyAware;
    // 文本内容来自于本地文件
    private boolean local;
    // 本地文件
    private File file;

    private final JTextPane textPane;
    private UndoManager undoManager;
    private UndoAction undoAction;
    private RedoAction redoAction;
    private ButtonTabComponent buttonTabComponent;
    private int index;

    public ScrollTextPane(MainPane mainPane, String filename) {
        this(mainPane, filename, false, true, null);
    }

    public ScrollTextPane(MainPane mainPane, File file) {
        this(mainPane, file.getName(), false, true, file);
    }

    public ScrollTextPane(MainPane mainPane, String filename, boolean modified, boolean modifyAware, File file) {
        super();
        this.filename = filename;
        this.modified = modified;
        this.modifyAware = modifyAware;
        this.local = (file != null);
        this.file = file;

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

    public ButtonTabComponent getButtonTabComponent() {
        return buttonTabComponent;
    }

    public void setButtonTabComponent(ButtonTabComponent buttonTabComponent) {
        this.buttonTabComponent = buttonTabComponent;
    }

    public void setModifyAware(boolean modifyAware) {
        this.modifyAware = modifyAware;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
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
        doc.addDocumentListener(new ModifyAwareDocumentListener());
        doc.addUndoableEditListener(e -> {
            undoManager.addEdit(e.getEdit());
            undoAction.updateUndoState();
            redoAction.updateRedoState();
        });

        // add key-binding
        addKeyBinding(mainPane.getDefaultActions(), textPane);

        return textPane;
    }


    private void addKeyBinding(Map<ActionName, Action> defaultActions, JTextPane textPane) {
        ActionMap actionMap = textPane.getActionMap();
        for (Map.Entry<ActionName, Action> entry : defaultActions.entrySet()) {
            actionMap.put(entry.getKey(), entry.getValue());
        }

        InputMap inputMap = textPane.getInputMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), UNDO);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), REDO);
    }

    /**
     * 感知文本变化的文档监听器
     */
    private class ModifyAwareDocumentListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            doAware();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            doAware();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            doAware();
        }

        private void doAware() {
            if (modifyAware) {
                modified = true;
                buttonTabComponent.updateTabbedTitle("* " + getFilename());
            }
        }
    }
}
