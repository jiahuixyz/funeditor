package redcoder.texteditor.core.textpane;

import redcoder.texteditor.core.EditorFrame;
import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.action.ActionName;
import redcoder.texteditor.core.dnd.FileTextTransferHandler;
import redcoder.texteditor.core.file.FileProcessor;
import redcoder.texteditor.core.file.UnsavedCreatedNewlyFiles;
import redcoder.texteditor.core.font.FontChangeEvent;
import redcoder.texteditor.core.font.FontChangeListener;
import redcoder.texteditor.core.font.FontChangeProcessor;
import redcoder.texteditor.core.linenumber.JTextAreaBasedLineNumberModel;
import redcoder.texteditor.core.linenumber.LineNumberComponent;
import redcoder.texteditor.core.linenumber.LineNumberModel;
import redcoder.texteditor.core.tabpane.ButtonTabComponent;
import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.utils.FileUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static redcoder.texteditor.core.action.ActionName.REDO;
import static redcoder.texteditor.core.action.ActionName.UNDO;

/**
 * 支持滚动的文本窗格
 */
public class ScrollTextPane extends JScrollPane implements FontChangeListener {

    private static final Logger LOGGER = Logger.getLogger(ScrollTextPane.class.getName());
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
    private final LineNumberComponent lineNumberComponent;
    private ButtonTabComponent buttonTabComponent;

    public ScrollTextPane(String filename) {
        this(filename, false, true, null);
    }

    public ScrollTextPane(File file) {
        this(file.getName(), false, true, file);
    }

    public ScrollTextPane(String filename, boolean modified, boolean modifyAware, File file) {
        this.listenerList = new ArrayList<>();
        this.filename = filename;
        this.modified = modified;
        this.modifyAware = modifyAware;
        this.file = file;

        this.undoManager = new UndoManager();

        this.textArea = new JTextArea();
        this.lineNumberComponent = createLineNumComponent();
        initTextArea();
        this.setRowHeaderView(lineNumberComponent);
        this.setViewportView(textArea);

        if (file != null) {
            setText(file, true);
        }

        FontChangeProcessor.addListener(this);
    }

    /**
     * 为编辑区绘制行号
     */
    public void paintLineNumber(){
        // update line number
        lineNumberComponent.adjustWidth();
    }

    /**
     * 添加文本窗格变化监听器
     */
    public void addTextPaneChangeListener(TextPaneChangeListener listener) {
        this.listenerList.add(listener);
    }

    private void fireTextPaneChangeEvent() {
        if (listenerList != null) {
            for (TextPaneChangeListener textPaneChangeListener : listenerList) {
                textPaneChangeListener.onChange(new TextPaneChangeEvent(this));
            }
        }
    }

    /**
     * 当切换到某个tab是，{@link TabPane}会调用该方法，通知被选中的tab下的文本窗格。
     */
    public void touch() {
        fireTextPaneChangeEvent();

        // 切换tab后触发绘制行号
        paintLineNumber();
    }

    /**
     * 执行undo操作
     */
    public void undo(ActionEvent e) {
        try {
            undoManager.undo();
        } catch (CannotUndoException ex) {
            LOGGER.log(Level.WARNING, "UndoAction", e);
        }
    }

    /**
     * 执行redo操作
     */
    public void redo(ActionEvent e) {
        try {
            undoManager.redo();
        } catch (CannotRedoException ex) {
            LOGGER.log(Level.WARNING, "RedoAction", e);
        }
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

    /**
     * 开启/关闭自动换行
     */
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

    @Override
    public void onChange(FontChangeEvent e) {
        Font newFont = e.getNewFont();
        if (textArea != null) {
            textArea.setFont(newFont);
        }
        if (lineNumberComponent != null) {
            lineNumberComponent.setLineNumberFont(newFont);
        }

        fireTextPaneChangeEvent();
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
        boolean saved = FileProcessor.saveTextPaneToFile(this);
        if (saved) {
            modified = false;

            // update tab title and filename
            updateTabbedTitle(file.getName());
            filename = file.getName();

            UnsavedCreatedNewlyFiles.removeTextPane(this);
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

        if (closed) {
            UnsavedCreatedNewlyFiles.removeTextPane(this);
        }

        return closed;
    }

    private void initTextArea() {
        textArea.setFont(FontChangeProcessor.getSharedFont());
        // 绑定快捷键
        addKeyBinding(Framework.getKeyStrokes(), Framework.getActions(), textArea);

        // 当插入符号变化时，通知TextPaneChangeListener
        textArea.addCaretListener(e -> fireTextPaneChangeEvent());

        // 添加文档监听器
        Document doc = this.textArea.getDocument();
        doc.addUndoableEditListener(e -> {
            undoManager.addEdit(e.getEdit());
        });
        doc.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onDocumentChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onDocumentChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onDocumentChange();
            }

            private void onDocumentChange() {
                // update tab title
                if (modifyAware) {
                    modified = true;
                    updateTabbedTitle("* " + filename);
                }

                // update line number
                lineNumberComponent.adjustWidth();

                // notice TextPaneChangeListener
                fireTextPaneChangeEvent();
            }
        });

        // enable DnD
        textArea.setDragEnabled(true);
        textArea.setDropMode(DropMode.INSERT);
        textArea.setTransferHandler(new FileTextTransferHandler());
    }

    private void addKeyBinding(Map<ActionName, KeyStroke> keyStrokes,
                               Map<ActionName, Action> actions,
                               JTextArea textPane) {
        ActionMap actionMap = textPane.getActionMap();
        actionMap.put(UNDO, actions.get(UNDO));
        actionMap.put(REDO, actions.get(REDO));

        InputMap inputMap = textPane.getInputMap();
        inputMap.put(keyStrokes.get(UNDO), UNDO);
        inputMap.put(keyStrokes.get(REDO), REDO);
    }

    private LineNumberComponent createLineNumComponent() {
        LineNumberModel lineNumberModel = new JTextAreaBasedLineNumberModel(textArea);
        return new LineNumberComponent(lineNumberModel, LineNumberComponent.RIGHT_ALIGNMENT);
    }
}
