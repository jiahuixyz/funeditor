package redcoder.texteditor.core.textpane;

import redcoder.texteditor.action.ActionName;
import redcoder.texteditor.action.RedoAction;
import redcoder.texteditor.action.UndoAction;
import redcoder.texteditor.core.tabpane.ButtonTabComponent;
import redcoder.texteditor.core.EditorFrame;
import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.tabpane.MainTabPane;
import redcoder.texteditor.core.file.FileProcessor;
import redcoder.texteditor.core.linenumber.JTextAreaBasedLineNumberModel;
import redcoder.texteditor.core.linenumber.LineNumberComponent;
import redcoder.texteditor.core.linenumber.LineNumberModel;
import redcoder.texteditor.utils.FileUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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

    private final List<TextPaneChangeListener> listenerList;
    private final JTextArea textArea;
    private final UndoManager undoManager;
    private final UndoAction undoAction;
    private final RedoAction redoAction;
    private final LineNumberComponent lineNumberComponent;
    private ButtonTabComponent buttonTabComponent;

    public ScrollTextPane(MainTabPane mainTabPane, String filename) {
        this(mainTabPane, filename, false, true, null);
    }

    public ScrollTextPane(MainTabPane mainTabPane, File file) {
        this(mainTabPane, file.getName(), false, true, file);
    }

    public ScrollTextPane(MainTabPane mainTabPane, String filename, boolean modified, boolean modifyAware, File file) {
        this.listenerList = new ArrayList<>();
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
        initTextArea(mainTabPane);
        this.setRowHeaderView(lineNumberComponent);
        this.setViewportView(textArea);

        if (file != null) {
            setText(file, true);
        }
    }

    /**
     * 添加文本窗格变化监听器
     */
    public void addTextPaneChangeListener(TextPaneChangeListener listener) {
        this.listenerList.add(listener);
    }

    /**
     * 通知文本窗格变化监听器
     */
    private void noticeTextPaneChangeListener() {
        if (listenerList != null) {
            for (TextPaneChangeListener textPaneChangeListener : listenerList) {
                textPaneChangeListener.onChange(new TextPaneChangeEvent(this));
            }
        }
    }

    /**
     * 当切换到某个tab是，{@link MainTabPane}会调用该方法，通知被选中的tab下的文本窗格。
     */
    public void touch() {
        noticeTextPaneChangeListener();
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

    /**
     * 将文件内容写入文本窗格内
     *
     * @param file               文件
     * @param disableModifyAware 是否临时禁用文件修改感知功能（禁用行为仅限于该方法内）
     */
    public void setText(File file, boolean disableModifyAware) {
        if (disableModifyAware) {
            modifyAware = false;
        }
        this.textArea.setText(FileUtils.readFile(file));
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

        noticeTextPaneChangeListener();
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

    public void updateTabbedTitle(String newTitle) {
        buttonTabComponent.updateTabbedTitle(newTitle);
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

    // ----------------- save & close

    /**
     * 保存当前窗格中的文本
     *
     * @return true-保存成功，false-保存失败
     */
    public boolean saveTextPane() {
        FileProcessor fileProcessor = Framework.INSTANCE.getFileProcessor();
        boolean saved = fileProcessor.saveTextPaneToFile(this);
        if (saved) {
            modified = false;
            // update tab title and filename
            updateTabbedTitle(file.getName());
            filename = file.getName();
        }
        return saved;
    }

    /**
     * 关闭当前窗格
     *
     * @return true：关闭成功，false：关闭失败
     */
    public boolean closeTextPane() {
        boolean closed = false;
        if (modified) {
            String message = String.format("Do you want to save the changes you made to %s?\n"
                    + "Your changes will be lost if you don't save them.", filename);
            int state = JOptionPane.showOptionDialog(this, message, EditorFrame.TITLE, JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, CLOSE_OPTIONS, CLOSE_OPTIONS[0]);
            if (state == JOptionPane.YES_OPTION) {
                // save file firstly, then close it.
                if (saveTextPane()) {
                    closed = true;
                }
            } else if (state == JOptionPane.NO_OPTION) {
                // close file directly
                closed = true;
            }
            // user cancel operation, don't close it.
        } else {
            closed = true;
        }

        return closed;
    }

    private void initTextArea(MainTabPane mainTabPane) {
        textArea.setFont(mainTabPane.getStpFont());
        // 绑定快捷键
        addKeyBinding(mainTabPane.getActionCollection().getKeyStrokes(), mainTabPane.getActionCollection().getActions(), textArea);
        // 添加CaretListener，用于更新编辑器底部的状态栏
        textArea.addCaretListener(e -> noticeTextPaneChangeListener());

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
                noticeTextPaneChangeListener();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                noticeTextPaneChangeListener();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                noticeTextPaneChangeListener();
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
