package redcoder.texteditor.core;

import redcoder.texteditor.action.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.awt.event.KeyEvent.*;
import static redcoder.texteditor.action.ActionName.*;

public class Framework extends WindowAdapter {

    private static final Framework INSTANCE = new Framework();
    private static final Map<ActionName, KeyStroke> SHARED_KEY_STROKES;
    private static final Map<ActionName, Action> SHARED_ACTION;

    private static int numWindows = 0;
    private static Point lastLocation;
    private static EditorFrame activatedFrame;
    private static final List<EditorFrame> openedFrame = new ArrayList<>();

    static {
        SHARED_KEY_STROKES = new HashMap<>();
        SHARED_KEY_STROKES.put(ZOOM_IN, KeyStroke.getKeyStroke(VK_ADD, CTRL_DOWN_MASK));
        SHARED_KEY_STROKES.put(ZOOM_OUT, KeyStroke.getKeyStroke(VK_SUBTRACT, CTRL_DOWN_MASK));
        SHARED_KEY_STROKES.put(CUT, KeyStroke.getKeyStroke(VK_X, CTRL_DOWN_MASK));
        SHARED_KEY_STROKES.put(COPY, KeyStroke.getKeyStroke(VK_C, CTRL_DOWN_MASK));
        SHARED_KEY_STROKES.put(PASTE, KeyStroke.getKeyStroke(VK_V, CTRL_DOWN_MASK));
        SHARED_KEY_STROKES.put(NEW_WINDOW, KeyStroke.getKeyStroke(VK_N, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));
        SHARED_KEY_STROKES.put(CLOSE_WINDOW, KeyStroke.getKeyStroke(VK_F4, ALT_DOWN_MASK));
        SHARED_KEY_STROKES.put(EXIT, KeyStroke.getKeyStroke(VK_F4, CTRL_DOWN_MASK));
        SHARED_KEY_STROKES.put(NEW_FILE, KeyStroke.getKeyStroke(VK_N, CTRL_DOWN_MASK));
        SHARED_KEY_STROKES.put(OPEN_FILE, KeyStroke.getKeyStroke(VK_O, CTRL_DOWN_MASK));
        SHARED_KEY_STROKES.put(SAVE_FILE, KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK));
        SHARED_KEY_STROKES.put(SAVE_AS_FILE, KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK | ALT_DOWN_MASK));
        SHARED_KEY_STROKES.put(SAVE_ALL, KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));
        SHARED_KEY_STROKES.put(CLOSE, KeyStroke.getKeyStroke(VK_W, CTRL_DOWN_MASK));
        SHARED_KEY_STROKES.put(CLOSE_ALL, KeyStroke.getKeyStroke(VK_W, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));
        SHARED_KEY_STROKES.put(UNDO, KeyStroke.getKeyStroke(VK_Z, CTRL_DOWN_MASK));
        SHARED_KEY_STROKES.put(REDO, KeyStroke.getKeyStroke(VK_Z, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));
        SHARED_KEY_STROKES.put(LINE_WRAP, KeyStroke.getKeyStroke(VK_Z, ALT_DOWN_MASK));
        SHARED_KEY_STROKES.put(FIND, KeyStroke.getKeyStroke(VK_F, CTRL_DOWN_MASK));
        SHARED_KEY_STROKES.put(REPLACE, KeyStroke.getKeyStroke(VK_R, CTRL_DOWN_MASK));

        SHARED_ACTION = new HashMap<>();
        SHARED_ACTION.put(ZOOM_IN, new ZoomInAction());
        SHARED_ACTION.put(ZOOM_OUT, new ZoomOutAction());
        SHARED_ACTION.put(CUT, new CutAction());
        SHARED_ACTION.put(COPY, new CopyAction());
        SHARED_ACTION.put(PASTE, new PasteAction());
        SHARED_ACTION.put(EXIT, new ExitAction());
        SHARED_ACTION.put(NEW_WINDOW, new NewWindowAction());
        SHARED_ACTION.put(CLOSE_WINDOW, new CloseWindowAction());
        SHARED_ACTION.put(UNDO, new UndoActionWrapper());
        SHARED_ACTION.put(REDO, new RedoActionWrapper());
        SHARED_ACTION.put(NEW_FILE, new NewAction());
        SHARED_ACTION.put(OPEN_FILE, new OpenAction());
        SHARED_ACTION.put(SAVE_FILE, new SaveAction());
        SHARED_ACTION.put(SAVE_AS_FILE, new SaveAsAction());
        SHARED_ACTION.put(SAVE_ALL, new SaveAllAction());
        SHARED_ACTION.put(CLOSE, new CloseAction());
        SHARED_ACTION.put(CLOSE_ALL, new CloseAllAction());
        SHARED_ACTION.put(LINE_WRAP, new LineWrapAction());
        SHARED_ACTION.put(FIND, new FindAction());
        SHARED_ACTION.put(REPLACE, new ReplaceAction());
        SHARED_ACTION.put(ABOUT, new AboutAction());
    }

    private Framework() {
    }

    public static void makeNewWindow() {
        EditorFrame frame = new EditorFrame();
        numWindows++;
        openedFrame.add(frame);

        frame.createAndShowGUI();
        frame.addWindowListener(INSTANCE);
        if (lastLocation != null) {
            lastLocation.translate(40, 40);
            frame.setLocation(lastLocation);
        } else {
            lastLocation = frame.getLocation();
        }
        frame.setVisible(true);
    }

    public static void closeWindow() {
        if (activatedFrame.canCloseNormally()) {
            activatedFrame.setVisible(false);
            activatedFrame.dispose();
            numWindows--;
            openedFrame.remove(activatedFrame);
        }
    }

    @Override
    public void windowActivated(WindowEvent e) {
        Object source = e.getSource();
        activatedFrame = (EditorFrame) source;
    }

    @Override
    public void windowClosed(WindowEvent e) {
        if (numWindows == 0) {
            System.exit(0);
        }
    }

    public static Map<ActionName, KeyStroke> getKeyStrokes() {
        return SHARED_KEY_STROKES;
    }

    public static Map<ActionName, Action> getActions() {
        return SHARED_ACTION;
    }

    public static int getNumWindows() {
        return numWindows;
    }

    public static EditorFrame getActivatedFrame() {
        return activatedFrame;
    }

    public static List<EditorFrame> getOpenedFrame() {
        return openedFrame;
    }
}
