package redcoder.texteditor.utils;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.util.HashMap;
import java.util.Map;

public class JTextPaneSharedAction {

    private static final Map<Object, Action> ACTIONS = new HashMap<>();

    static {
        JTextPane textPane = new JTextPane();
        // default action
        for (Action action : textPane.getActions()) {
            ACTIONS.put(action.getValue(Action.NAME), action);
        }
    }

    public static Action getShared(Object name) {
        return ACTIONS.get(name);
    }
}
