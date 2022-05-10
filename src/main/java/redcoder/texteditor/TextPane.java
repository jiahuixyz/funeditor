package redcoder.texteditor;

import redcoder.texteditor.action.RedoAction;
import redcoder.texteditor.action.UndoAction;

import javax.swing.*;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * 支持滚动的文本窗格
 */
public class TextPane extends JScrollPane implements ActionListener {

    private static final Font DEFAULT_FONT = new Font(null, Font.PLAIN, 16);

    private JTextPane textPane;
    private UndoManager undoManager;
    private UndoAction undoAction;
    private RedoAction redoAction;

    public TextPane(MultiKeyPressListener multiKeyPressListener) {
        super();
        initAction();
        textPane = createTextPane(multiKeyPressListener);
        setViewportView(textPane);
    }

    private void initAction() {
        undoManager = new UndoManager();
        undoAction = new UndoAction(undoManager);
        redoAction = new RedoAction(undoManager);

        undoAction.setRedoAction(redoAction);
        redoAction.setUndoAction(undoAction);
    }

    private JTextPane createTextPane(MultiKeyPressListener multiKeyPressListener) {
        JTextPane textPane = new JTextPane();
        textPane.setFont(DEFAULT_FONT);

        StyledDocument doc = textPane.getStyledDocument();
        doc.addUndoableEditListener(e -> {
            undoManager.addEdit(e.getEdit());
            undoAction.updateUndoState();
            redoAction.updateRedoState();
        });
        // 注册键盘监听器
        textPane.setFocusable(true);
        textPane.addKeyListener(multiKeyPressListener);

        return textPane;
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


    public UndoAction getUndoAction() {
        return undoAction;
    }

    public RedoAction getRedoAction() {
        return redoAction;
    }
}
