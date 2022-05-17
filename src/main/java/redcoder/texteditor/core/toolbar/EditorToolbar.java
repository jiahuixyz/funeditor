package redcoder.texteditor.core.toolbar;

import redcoder.texteditor.action.ActionName;
import redcoder.texteditor.core.Framework;
import redcoder.texteditor.core.tabpane.MainTabPane;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static redcoder.texteditor.action.ActionName.*;

/**
 * 工具栏
 */
public class EditorToolbar extends JPanel {

    public EditorToolbar(MainTabPane mainTabPane) {
        setLayout(new BorderLayout());
        init(mainTabPane);
    }

    private void init(MainTabPane mainTabPane) {
        Map<ActionName, Action> mergedAction = new HashMap<>();
        mergedAction.putAll(Framework.getFrameworkSharedAction());
        mergedAction.putAll(mainTabPane.getActions());

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        // new file, open file, save file, save all
        addButtonToToolBar(mergedAction.get(NEW_FILE), toolBar);
        addButtonToToolBar(mergedAction.get(OPEN_FILE), toolBar);
        addButtonToToolBar(mergedAction.get(SAVE_FILE), toolBar);
        addButtonToToolBar(mergedAction.get(SAVE_ALL), toolBar);
        toolBar.addSeparator();

        // cut, copy, paste
        addButtonToToolBar(mergedAction.get(CUT), toolBar);
        addButtonToToolBar(mergedAction.get(COPY), toolBar);
        addButtonToToolBar(mergedAction.get(PASTE), toolBar);
        toolBar.addSeparator();

        // undo, redo
        addButtonToToolBar(mergedAction.get(UNDO), toolBar);
        addButtonToToolBar(mergedAction.get(REDO), toolBar);
        toolBar.addSeparator();

        // undo, redo
        addButtonToToolBar(mergedAction.get(ZOOM_IN), toolBar);
        addButtonToToolBar(mergedAction.get(ZOOM_OUT), toolBar);

        add(toolBar, BorderLayout.LINE_START);
    }

    private void addButtonToToolBar(Action action, JToolBar toolBar) {
        JButton button = new JButton(action);
        if (button.getIcon() != null) {
            button.setText("");
            button.setToolTipText(action.getValue(Action.NAME).toString());
        }
        toolBar.add(button);
        toolBar.addSeparator(new Dimension(1,toolBar.getHeight()));
    }
}
