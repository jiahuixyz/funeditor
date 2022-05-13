package redcoder.texteditor.pane;

import redcoder.texteditor.action.ActionName;
import redcoder.texteditor.action.RedoAction;
import redcoder.texteditor.action.UndoAction;
import redcoder.texteditor.linenumber.JTextAreaBasedLineNumberModel;
import redcoder.texteditor.linenumber.LineNumberComponent;
import redcoder.texteditor.linenumber.LineNumberModel;
import redcoder.texteditor.statusbar.CaretStatusIndicator;
import redcoder.texteditor.statusbar.TextLengthIndicator;
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

    private final MainPane mainPane;
    private final JTextArea textArea;
    private final UndoManager undoManager;
    private final UndoAction undoAction;
    private final RedoAction redoAction;
    private final LineNumberComponent lineNumberComponent;
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

        this.undoManager = new UndoManager();
        this.undoAction = new UndoAction(undoManager);
        this.redoAction = new RedoAction(undoManager);
        this.undoAction.setRedoAction(redoAction);
        this.redoAction.setUndoAction(undoAction);

        this.textArea = new JTextArea();
        this.lineNumberComponent = createLineNumComponent();
        initTextArea(mainPane);
        this.setRowHeaderView(lineNumberComponent);
        this.setViewportView(textArea);
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
        modifyAware = false;
        this.textArea.setText(text);
        modifyAware = true;
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

    public void lineWrapSwitch() {
        if (textArea.getLineWrap()) {
            textArea.setLineWrap(false);
            textArea.setWrapStyleWord(false);
        } else {
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
        }
    }

    public void updateTabbedTitle(String newTitle){
        buttonTabComponent.updateTabbedTitle(newTitle);
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
                    // remove from UnsavedNewFile
                    mainPane.getUnsavedNewFile().removeTextPane(this);
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
        updateTabbedTitle(file.getName());
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

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setButtonTabComponent(ButtonTabComponent buttonTabComponent) {
        this.buttonTabComponent = buttonTabComponent;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFilename() {
        return filename;
    }

    public boolean isModified() {
        return modified;
    }

    // ------------------- init ScrollTextPane
    private void initTextArea(MainPane mainPane) {
        textArea.setFont(mainPane.getStpFont());
        // 绑定快捷键
        addKeyBinding(mainPane.getKeyStrokes(), mainPane.getActions(), textArea);
        // 添加CaretListener，用于更新编辑器底部的状态栏
        textArea.addCaretListener(CaretStatusIndicator.INDICATOR);

        // 添加几个文档监听器
        Document doc = this.textArea.getDocument();
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
                    updateTabbedTitle("* " + filename);
                }
            }
        });
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
        // 处理undo，redo的监听器
        doc.addUndoableEditListener(e -> {
            undoManager.addEdit(e.getEdit());
            undoAction.updateUndoState();
            redoAction.updateRedoState();
        });
        // 用于更新编辑器底部的状态栏的监听器
        doc.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                TextLengthIndicator.INDICATOR.refresh(textArea);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                TextLengthIndicator.INDICATOR.refresh(textArea);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                TextLengthIndicator.INDICATOR.refresh(textArea);
            }
        });
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

    private LineNumberComponent createLineNumComponent() {
        LineNumberModel lineNumberModel = new JTextAreaBasedLineNumberModel(textArea);
        return new LineNumberComponent(lineNumberModel, LineNumberComponent.RIGHT_ALIGNMENT);
    }
}
