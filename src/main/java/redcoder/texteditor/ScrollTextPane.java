package redcoder.texteditor;

import redcoder.texteditor.action.ActionName;
import redcoder.texteditor.action.RedoAction;
import redcoder.texteditor.action.UndoAction;
import redcoder.texteditor.utils.FileUtils;

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

    private static final Object[] CLOSE_OPTIONS = {"Save", "Don't Save", "Cancel"};

    private String filename;
    // 表示文本内容是否被修改
    private boolean modified;
    // ModifyAwareDocumentListener的文本内容变化感知功能是否开启
    private boolean modifyAware;
    // 本地文件
    private File file;

    private final JTextPane textPane;
    private UndoManager undoManager;
    private UndoAction undoAction;
    private RedoAction redoAction;
    private final MainPane mainPane;
    private ButtonTabComponent buttonTabComponent;

    public ScrollTextPane(MainPane mainPane, String filename) {
        this(mainPane, filename, false, true, null);
    }

    public ScrollTextPane(MainPane mainPane, File file) {
        this(mainPane, file.getName(), false, true, file);
    }

    public ScrollTextPane(MainPane mainPane, String filename, boolean modified, boolean modifyAware, File file) {
        super();
        this.mainPane = mainPane;
        this.filename = filename;
        this.modified = modified;
        this.modifyAware = modifyAware;
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

    // ----------------------- operation about file

    /**
     * 保存当前窗格中的文本
     *
     * @return true-保存成功，false-保存失败
     */
    public boolean saveTextPane() {
        boolean saved = false;
        if (file != null) {
            saveToFile(this.file);
            saved = true;
        } else {
            int state = mainPane.getFileChooser().showSaveDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                File file = mainPane.getFileChooser().getSelectedFile();
                if (file.exists()) {
                    String message = String.format("%s already exist, would you like overwriting it?", file.getName());
                    int n = JOptionPane.showConfirmDialog(this, message, EditorFrame.TITLE, JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        saveToFile(file);
                        saved = true;
                    }
                } else {
                    saveToFile(file);
                    saved = true;
                }

                if (saved) {
                    this.file = file;
                }
            }
        }

        if (saved) {
            modified = false;
        }
        return saved;
    }

    private void saveToFile(File file) {
        FileUtils.writeFile(textPane.getText(), file);

        // update tab title and filename
        buttonTabComponent.updateTabbedTitle(file.getName());
        filename = file.getName();
    }

    /**
     * 关闭当前窗格
     *
     * @param index 当前窗格在主窗口中的位置
     * @return true：关闭成功，false：关闭失败
     */
    public boolean closeTextPane(int index) {
        boolean closed = false;
        if (modified) {
            String message = String.format("Do you want to save the changes you made to %s?\n"
                    + "Your changes will be lost if you don't save them.", file.getName());
            int state = JOptionPane.showOptionDialog(this, message, EditorFrame.TITLE, JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, CLOSE_OPTIONS, CLOSE_OPTIONS[0]);
            if (state == JOptionPane.YES_OPTION) {
                // save file firstly, then close it.
                if (saveTextPane()) {
                    mainPane.removeTabAt(index);
                    closed = true;
                }
            } else if (state == JOptionPane.NO_OPTION) {
                // close file directly
                mainPane.removeTabAt(index);
                closed = true;
            }
            // user cancel operation, don't close it.
        } else {
            mainPane.removeTabAt(index);
            closed = true;
        }

        return closed;
    }


    // ---------- getter, setter
    public String getFilename() {
        return filename;
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

    public void setButtonTabComponent(ButtonTabComponent buttonTabComponent) {
        this.buttonTabComponent = buttonTabComponent;
    }

    public void setModifyAware(boolean modifyAware) {
        this.modifyAware = modifyAware;
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
