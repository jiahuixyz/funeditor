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
    private static final Map<ActionName, KeyStroke> FRAMEWORK_SHARE_KEY_STROKES;
    private static final Map<ActionName, Action> FRAMEWORK_SHARED_ACTION;

    private static int numWindows = 0;
    private static Point lastLocation;
    private static EditorFrame activatedFrame;
    private static final List<EditorFrame> frames= new ArrayList<>();

    static {
        FRAMEWORK_SHARE_KEY_STROKES = new HashMap<>();
        FRAMEWORK_SHARE_KEY_STROKES.put(ZOOM_IN, KeyStroke.getKeyStroke(VK_ADD, CTRL_DOWN_MASK));
        FRAMEWORK_SHARE_KEY_STROKES.put(ZOOM_OUT, KeyStroke.getKeyStroke(VK_SUBTRACT, CTRL_DOWN_MASK));
        FRAMEWORK_SHARE_KEY_STROKES.put(CUT, KeyStroke.getKeyStroke(VK_X, CTRL_DOWN_MASK));
        FRAMEWORK_SHARE_KEY_STROKES.put(COPY, KeyStroke.getKeyStroke(VK_C, CTRL_DOWN_MASK));
        FRAMEWORK_SHARE_KEY_STROKES.put(PASTE, KeyStroke.getKeyStroke(VK_V, CTRL_DOWN_MASK));
        FRAMEWORK_SHARE_KEY_STROKES.put(NEW_WINDOW, KeyStroke.getKeyStroke(VK_N, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));
        FRAMEWORK_SHARE_KEY_STROKES.put(CLOSE_WINDOW, KeyStroke.getKeyStroke(VK_F4, ALT_DOWN_MASK));
        FRAMEWORK_SHARE_KEY_STROKES.put(EXIT, KeyStroke.getKeyStroke(VK_F4, CTRL_DOWN_MASK));
        FRAMEWORK_SHARE_KEY_STROKES.put(NEW_FILE, KeyStroke.getKeyStroke(VK_N, CTRL_DOWN_MASK));
        FRAMEWORK_SHARE_KEY_STROKES.put(OPEN_FILE, KeyStroke.getKeyStroke(VK_O, CTRL_DOWN_MASK));
        FRAMEWORK_SHARE_KEY_STROKES.put(SAVE_FILE, KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK));
        FRAMEWORK_SHARE_KEY_STROKES.put(SAVE_ALL, KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));
        FRAMEWORK_SHARE_KEY_STROKES.put(CLOSE, KeyStroke.getKeyStroke(VK_W, CTRL_DOWN_MASK));
        FRAMEWORK_SHARE_KEY_STROKES.put(CLOSE_ALL, KeyStroke.getKeyStroke(VK_W, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));
        FRAMEWORK_SHARE_KEY_STROKES.put(UNDO, KeyStroke.getKeyStroke(VK_Z, CTRL_DOWN_MASK));
        FRAMEWORK_SHARE_KEY_STROKES.put(REDO, KeyStroke.getKeyStroke(VK_Z, CTRL_DOWN_MASK | SHIFT_DOWN_MASK));
        FRAMEWORK_SHARE_KEY_STROKES.put(LINE_WRAP, KeyStroke.getKeyStroke(VK_Z, ALT_DOWN_MASK));

        FRAMEWORK_SHARED_ACTION = new HashMap<>();
        FRAMEWORK_SHARED_ACTION.put(ZOOM_IN, new ZoomInAction());
        FRAMEWORK_SHARED_ACTION.put(ZOOM_OUT, new ZoomOutAction());
        FRAMEWORK_SHARED_ACTION.put(CUT, new CutAction());
        FRAMEWORK_SHARED_ACTION.put(COPY, new CopyAction());
        FRAMEWORK_SHARED_ACTION.put(PASTE, new PasteAction());
        FRAMEWORK_SHARED_ACTION.put(EXIT, new ExitAction());
        FRAMEWORK_SHARED_ACTION.put(NEW_WINDOW, new NewWindowAction());
        FRAMEWORK_SHARED_ACTION.put(CLOSE_WINDOW, new CloseWindowAction());
    }

    private Framework() {
    }

    public static void makeNewWindow() {
        EditorFrame frame = new EditorFrame();
        numWindows++;
        frames.add(frame);

        frame.init();
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
        if (activatedFrame.shouldClose()) {
            activatedFrame.setVisible(false);
            activatedFrame.dispose();
            numWindows--;
            frames.remove(activatedFrame);
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

    public static Map<ActionName, KeyStroke> getFrameworkShareKeyStrokes() {
        return FRAMEWORK_SHARE_KEY_STROKES;
    }

    public static Map<ActionName, Action> getFrameworkSharedAction() {
        return FRAMEWORK_SHARED_ACTION;
    }

    public static int getNumWindows() {
        return numWindows;
    }

    public static void switchTheme(String themeName) {
        try {
            if (UIManager.getLookAndFeel().getClass().getName().equals(themeName)) {
                return;
            }

            UIManager.setLookAndFeel(themeName);
            for (EditorFrame frame : frames) {
                SwingUtilities.updateComponentTreeUI(frame);
            }
        } catch (Exception e) {
            System.err.printf("Failed to switch %s theme%n", themeName);
            e.printStackTrace();
        }
    }
}
