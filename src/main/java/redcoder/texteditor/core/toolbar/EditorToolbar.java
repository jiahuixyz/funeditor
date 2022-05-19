package redcoder.texteditor.core.toolbar;

import redcoder.texteditor.action.ActionName;
import redcoder.texteditor.core.Framework;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static redcoder.texteditor.action.ActionName.*;

/**
 * 工具栏
 */
public class EditorToolbar extends JPanel {

    public EditorToolbar() {
        setLayout(new BorderLayout());
        init();
    }

    private void init() {
        Map<ActionName, Action> actions = Framework.getActions();

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        // new file, open file, save file, save all
        addButtonToToolBar(actions.get(NEW_FILE), toolBar);
        addButtonToToolBar(actions.get(OPEN_FILE), toolBar);
        addButtonToToolBar(actions.get(SAVE_FILE), toolBar);
        addButtonToToolBar(actions.get(SAVE_ALL), toolBar);
        toolBar.addSeparator();

        // cut, copy, paste
        addButtonToToolBar(actions.get(CUT), toolBar);
        addButtonToToolBar(actions.get(COPY), toolBar);
        addButtonToToolBar(actions.get(PASTE), toolBar);
        toolBar.addSeparator();

        // find, replace
        addButtonToToolBar(actions.get(FIND), toolBar);
        addButtonToToolBar(actions.get(REPLACE), toolBar);
        toolBar.addSeparator();

        // undo, redo
        addButtonToToolBar(actions.get(UNDO), toolBar);
        addButtonToToolBar(actions.get(REDO), toolBar);
        toolBar.addSeparator();

        // undo, redo
        addButtonToToolBar(actions.get(ZOOM_IN), toolBar);
        addButtonToToolBar(actions.get(ZOOM_OUT), toolBar);

        add(toolBar, BorderLayout.LINE_START);
    }

    private void addButtonToToolBar(Action action, JToolBar toolBar) {
        JButton button = new JButton(action);
        if (button.getIcon() != null) {
            button.setText("");
            button.setToolTipText(action.getValue(Action.NAME).toString());
        }
        toolBar.add(button);
        toolBar.addSeparator(new Dimension(1, toolBar.getHeight()));
    }
}
