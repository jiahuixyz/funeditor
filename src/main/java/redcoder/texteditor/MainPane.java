package redcoder.texteditor;

import redcoder.texteditor.action.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import static redcoder.texteditor.action.ActionName.*;

/**
 * 编辑器主窗格，支持ActionListener机制
 */
public class MainPane extends JTabbedPane {

    private static final Font DEFAULT_FONT = new Font(null, Font.PLAIN, 16);
    private static final int FONT_SIZE_MINIMUM = 10;
    private static final int FONT_SIZE_MAXIMUM = 1000;

    private ScrollTextPane selectedScrollTextPane;
    private JFileChooser fileChooser;
    private Map<ActionName, Action> defaultActions;
    // ScrollTextPane font
    private Font stpFont = DEFAULT_FONT;

    public MainPane() {
        // 文件选择器
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // 创建默认的Action
        defaultActions = createDefaultActions(fileChooser);
        
        setFont(new Font(null, Font.PLAIN, 16));
        addChangeListener(e -> selectedScrollTextPane = (ScrollTextPane) getSelectedComponent());
    }

    private Map<ActionName, Action> createDefaultActions(JFileChooser fileChooser) {
        Map<ActionName, Action> actions = new HashMap<>();
        actions.put(UNDO, new UndoActionWrapper(this));
        actions.put(REDO, new RedoActionWrapper(this));
        actions.put(ZOOM_IN, new ZoomInAction(this));
        actions.put(ZOOM_OUT, new ZoomOutAction(this));
        actions.put(NEW_FILE, new NewFileAction(this));
        actions.put(OPEN_FILE, new OpenFileAction(this, fileChooser));
        actions.put(SAVE_FILE, new SaveFileAction(this, fileChooser));
        actions.put(CUT, new CutAction());
        actions.put(COPY, new CopyAction());
        actions.put(PASTE, new PasteAction());
        return actions;
    }

    public synchronized void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    public synchronized void remove(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }

    public void fireActionEvent(ActionEvent event) {
        ActionListener[] listeners = listenerList.getListeners(ActionListener.class);
        for (ActionListener listener : listeners) {
            listener.actionPerformed(event);
        }
    }

    public void zoomInFont() {
        int newSize = Math.min(stpFont.getSize() + 2, FONT_SIZE_MAXIMUM);
        stpFont = new Font(stpFont.getName(), stpFont.getStyle(), newSize);
        setChildComponentFont();
    }

    public void zoomOutFont() {
        int newSize = Math.max(stpFont.getSize() - 2, FONT_SIZE_MINIMUM);
        stpFont = new Font(stpFont.getName(), stpFont.getStyle(), newSize);
        setChildComponentFont();
    }

    private void setChildComponentFont() {
        for (Component component : getComponents()) {
            if (component instanceof ScrollTextPane) {
                ScrollTextPane scrollTextPane = (ScrollTextPane) component;
                scrollTextPane.getTextPane().setFont(stpFont);
            }
        }
    }

    // ----------- getter
    public ScrollTextPane getSelectedTextPane() {
        return selectedScrollTextPane;
    }

    public JFileChooser getFileChooser() {
        return fileChooser;
    }

    public Map<ActionName, Action> getDefaultActions() {
        return defaultActions;
    }

    public Font getStpFont() {
        return stpFont;
    }
}
