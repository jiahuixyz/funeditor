package redcoder.texteditor.action;

import redcoder.texteditor.core.tabpane.MainTabPane;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

import static java.awt.event.KeyEvent.*;
import static redcoder.texteditor.action.ActionName.*;

public class ActionCollection {

    private Map<ActionName, KeyStroke> keyStrokes;
    private Map<ActionName, Action> actions;

    public ActionCollection(MainTabPane mainTabPane) {
        initKeyStrokes();
        initActions(mainTabPane);
    }

    public Map<ActionName, KeyStroke> getKeyStrokes() {
        return keyStrokes;
    }

    public Map<ActionName, Action> getActions() {
        return actions;
    }

    private void initKeyStrokes() {
        keyStrokes = new HashMap<>();
        keyStrokes.put(ZOOM_IN, KeyStroke.getKeyStroke(VK_ADD, CTRL_DOWN_MASK));
        keyStrokes.put(ZOOM_OUT, KeyStroke.getKeyStroke(VK_SUBTRACT, CTRL_DOWN_MASK));
        keyStrokes.put(NEW_FILE, KeyStroke.getKeyStroke(VK_N, CTRL_DOWN_MASK));
        keyStrokes.put(OPEN_FILE, KeyStroke.getKeyStroke(VK_O, CTRL_DOWN_MASK));
        keyStrokes.put(SAVE_FILE, KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK));
        keyStrokes.put(SAVE_ALL, KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));
        keyStrokes.put(CLOSE, KeyStroke.getKeyStroke(VK_W, CTRL_DOWN_MASK));
        keyStrokes.put(CLOSE_ALL, KeyStroke.getKeyStroke(VK_W, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));
        keyStrokes.put(UNDO, KeyStroke.getKeyStroke(VK_Z, CTRL_DOWN_MASK));
        keyStrokes.put(REDO, KeyStroke.getKeyStroke(VK_Z, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));
        keyStrokes.put(CUT, KeyStroke.getKeyStroke(VK_X, CTRL_DOWN_MASK));
        keyStrokes.put(COPY, KeyStroke.getKeyStroke(VK_C, CTRL_DOWN_MASK));
        keyStrokes.put(PASTE, KeyStroke.getKeyStroke(VK_V, CTRL_DOWN_MASK));
        keyStrokes.put(EXIT, KeyStroke.getKeyStroke(VK_F4, CTRL_DOWN_MASK));
        keyStrokes.put(LINE_WRAP, KeyStroke.getKeyStroke(VK_Z, ALT_DOWN_MASK));
        keyStrokes.put(NEW_WINDOW, KeyStroke.getKeyStroke(VK_N, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));
        keyStrokes.put(CLOSE_WINDOW, KeyStroke.getKeyStroke(VK_F4, ALT_DOWN_MASK));
    }

    private void initActions(MainTabPane mainTabPane) {
        actions = new HashMap<>();
        actions.put(UNDO, new UndoActionWrapper(mainTabPane));
        actions.put(REDO, new RedoActionWrapper(mainTabPane));
        actions.put(ZOOM_IN, new ZoomInAction(mainTabPane));
        actions.put(ZOOM_OUT, new ZoomOutAction(mainTabPane));
        actions.put(NEW_FILE, new NewAction(mainTabPane));
        actions.put(OPEN_FILE, new OpenAction(mainTabPane));
        actions.put(SAVE_FILE, new SaveAction(mainTabPane));
        actions.put(SAVE_ALL, new SaveAllAction(mainTabPane));
        actions.put(CUT, new CutAction());
        actions.put(COPY, new CopyAction());
        actions.put(PASTE, new PasteAction());
        actions.put(CLOSE, new CloseAction(mainTabPane));
        actions.put(CLOSE_ALL, new CloseAllAction(mainTabPane));
        actions.put(EXIT, new ExitAction());
        actions.put(LINE_WRAP, new LineWrapAction(mainTabPane));
        actions.put(NEW_WINDOW, new NewWindowAction());
        actions.put(CLOSE_WINDOW, new CloseWindowAction());
    }
}
