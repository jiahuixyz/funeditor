package redcoder.texteditor;

import redcoder.texteditor.action.ActionName;
import redcoder.texteditor.action.RedoAction;
import redcoder.texteditor.action.UndoAction;
import redcoder.texteditor.linenumber.JTextAreaBasedLineNumberModel;
import redcoder.texteditor.linenumber.LineNumberComponent;
import redcoder.texteditor.linenumber.LineNumberModel;
import redcoder.texteditor.utils.FileUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Map;

import static redcoder.texteditor.action.ActionName.REDO;
import static redcoder.texteditor.action.ActionName.UNDO;

/**
 * 支持滚动的文本窗格
 */
public class ScrollTextPane extends JScrollPane {

    private static final Object[] CLOSE_OPTIONS = {"Save", "Don't Save", "Cancel"};

    private String filename;
    // 表示文本内容是否被修改
    private boolean modified;
    // ModifyAwareDocumentListener的文本内容变化感知功能是否开启
    private boolean modifyAware;
    // 本地文件
    private File file;

    private JTextArea textArea;
    private UndoManager undoManager;
    private UndoAction undoAction;
    private RedoAction redoAction;
    private final MainPane mainPane;
    private ButtonTabComponent buttonTabComponent;
    private LineNumberComponent lineNumberComponent;

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

        init(mainPane);
    }

    /**
     * 执行undo操作
     */
    public void undo(ActionEvent e) {
        undoAction.actionPerformed(e);
    }

    /**
     * 执行redo操作
     */
    public void redo(ActionEvent e) {
        redoAction.actionPerformed(e);
    }

    public void setText(String text) {
        this.textArea.setText(text);
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);

        if (textArea != null) {
            textArea.setFont(font);
        }
        if (lineNumberComponent != null) {
            lineNumberComponent.setLineNumberFont(font);
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
        FileUtils.writeFile(textArea.getText(), file);

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
                    + "Your changes will be lost if you don't save them.", filename);
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
    public void setButtonTabComponent(ButtonTabComponent buttonTabComponent) {
        this.buttonTabComponent = buttonTabComponent;
    }

    public void setModifyAware(boolean modifyAware) {
        this.modifyAware = modifyAware;
    }

    // ------------------- init ScrollTextPane

    private void init(MainPane mainPane) {
        initAction();
        this.textArea = createTextArea(mainPane);
        this.setViewportView(textArea);
    }

    private void initAction() {
        undoManager = new UndoManager();
        undoAction = new UndoAction(undoManager);
        redoAction = new RedoAction(undoManager);

        undoAction.setRedoAction(redoAction);
        redoAction.setUndoAction(undoAction);
    }

    private JTextArea createTextArea(MainPane mainPane) {
        JTextArea jTextArea = new JTextArea();
        jTextArea.setFont(mainPane.getStpFont());

        Document doc = jTextArea.getDocument();
        // 处理文本内容变化的监听器
        doc.addDocumentListener(new DocumentListener() {
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
                    buttonTabComponent.updateTabbedTitle("* " + filename);
                }
            }
        });

        // 添加渲染行号的组件
        this.lineNumberComponent = createLineNumComponent(jTextArea);
        this.setRowHeaderView(lineNumberComponent);

        // 渲染文本行号的监听器
        doc.addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                lineNumberComponent.adjustWidth();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lineNumberComponent.adjustWidth();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lineNumberComponent.adjustWidth();
            }
        });
        doc.addUndoableEditListener(e -> {
            undoManager.addEdit(e.getEdit());
            undoAction.updateUndoState();
            redoAction.updateRedoState();
        });

        // 绑定快捷键
        addKeyBinding(mainPane.getKeyStrokes(), mainPane.getActions(), jTextArea);

        return jTextArea;
    }

    private void addKeyBinding(Map<ActionName, KeyStroke> keyStrokes,
                               Map<ActionName, Action> actions,
                               JTextArea textPane) {
        ActionMap actionMap = textPane.getActionMap();
        for (Map.Entry<ActionName, Action> entry : actions.entrySet()) {
            actionMap.put(entry.getKey(), entry.getValue());
        }

        InputMap inputMap = textPane.getInputMap();
        inputMap.put(keyStrokes.get(UNDO), UNDO);
        inputMap.put(keyStrokes.get(REDO), REDO);
    }

    private LineNumberComponent createLineNumComponent(JTextArea textArea) {
        LineNumberModel lineNumberModel = new JTextAreaBasedLineNumberModel(textArea);
        LineNumberComponent lineNumberComponent = new LineNumberComponent(lineNumberModel);
        lineNumberComponent.setAlignment(LineNumberComponent.CENTER_ALIGNMENT);
        return lineNumberComponent;
    }
}
