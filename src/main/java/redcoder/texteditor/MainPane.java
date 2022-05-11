package redcoder.texteditor;

import redcoder.texteditor.action.*;
import redcoder.texteditor.utils.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return !f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Select File Only";
            }
        });

        // 创建默认的Action
        defaultActions = createDefaultActions();

        setFont(new Font(null, Font.PLAIN, 16));
        addChangeListener(e -> {
            selectedScrollTextPane = (ScrollTextPane) getSelectedComponent();
            selectedScrollTextPane.updateIndex(this.getSelectedIndex());
        });
    }

    private Map<ActionName, Action> createDefaultActions() {
        Map<ActionName, Action> actions = new HashMap<>();
        actions.put(UNDO, new UndoActionWrapper(this));
        actions.put(REDO, new RedoActionWrapper(this));
        actions.put(ZOOM_IN, new ZoomInAction(this));
        actions.put(ZOOM_OUT, new ZoomOutAction(this));
        actions.put(NEW_FILE, new NewFileAction(this));
        actions.put(OPEN_FILE, new OpenFileAction(this));
        actions.put(SAVE_FILE, new SaveFileAction(this));
        actions.put(CUT, new CutAction());
        actions.put(COPY, new CopyAction());
        actions.put(PASTE, new PasteAction());
        actions.put(CLOSE, new CloseFileAction(this));
        actions.put(CLOSE_ALL, new CloseAllFileAction(this));
        return actions;
    }

    public void updateTabbedTitle(int index, String title) {
        this.setTitleAt(index, title);
    }

    // --------- operation about ActionListener
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

    // ------------ operation about font
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

    // -------------  operation about file
    public boolean openFile() {
        int state = fileChooser.showOpenDialog(this);
        if (state == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            openFile(file.getName(), FileUtils.readFile(file));
            return true;
        }
        return false;
    }

    private void openFile(String filename, String fileContent) {
        ScrollTextPane scrollTextPane = new ScrollTextPane(this, filename, false);
        scrollTextPane.getTextPane().setText(fileContent);

        this.addActionListener(scrollTextPane);
        this.addTab(filename, scrollTextPane);
        this.setSelectedComponent(scrollTextPane);

        scrollTextPane.updateIndex(this.getSelectedIndex());
    }

    public boolean saveFile() {
        boolean saved = false;
        int i = fileChooser.showOpenDialog(this);
        if (i == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file.exists()) {
                String message = String.format("%s already exist, would you like overwriting it?", file.getName());
                int n = JOptionPane.showConfirmDialog(this, message, EditorFrame.TITLE, JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    saveFile(file);
                    saved = true;
                }
            } else {
                saveFile(file);
                saved = true;
            }

            if (saved) {
                this.getSelectedTextPane().setModified(false);
            } else {
                System.err.println("Failed to save file " + file.getName());
            }
        }
        return saved;
    }

    private void saveFile(File file) {
        ScrollTextPane selectedTextPane = this.getSelectedTextPane();
        JTextPane textPane = selectedTextPane.getTextPane();
        FileUtils.writeFile(textPane.getText(), file);

        // update tab title and filename
        int selectedIndex = this.getSelectedIndex();
        this.setTitleAt(selectedIndex, file.getName());
        selectedTextPane.setFilename(file.getName());
    }

    public boolean closeFile() {
        ScrollTextPane selectedTextPane = this.getSelectedTextPane();
        if (closeFile(selectedTextPane)) {
            return true;
        } else {
            System.err.println("Failed to close file " + selectedTextPane.getFilename());
            return false;
        }
    }

    private boolean closeFile(ScrollTextPane scrollTextPane) {
        boolean closed = false;
        if (scrollTextPane.isModified()) {
            String message = String.format("Do you want to save the changes you made to %s?\n"
                    + "Your changes will be lost if you don't save them.", scrollTextPane.getFilename());
            int state = JOptionPane.showConfirmDialog(this, message, EditorFrame.TITLE, JOptionPane.YES_NO_CANCEL_OPTION);
            if (state == JOptionPane.YES_OPTION) {
                // save file firstly, then close it.
                if (saveFile()) {
                    this.removeTabAt(this.getSelectedIndex());
                    closed = true;
                }
            } else if (state == JOptionPane.NO_OPTION) {
                // close file directly
                this.removeTabAt(this.getSelectedIndex());
                closed = true;
            }
            // user cancel operation, don't close it.
        } else {
            this.removeTabAt(this.getSelectedIndex());
            closed = true;
        }
        return closed;
    }

    public boolean closeAllFile() {
        for (Component component : this.getComponents()) {
            if (component instanceof ScrollTextPane) {
                if (!closeFile((ScrollTextPane) component)) {
                    return false;
                }
            }
        }
        return true;
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
