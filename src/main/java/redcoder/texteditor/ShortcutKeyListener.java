package redcoder.texteditor;

import redcoder.texteditor.action.*;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * 快捷键监听器
 */
public class ShortcutKeyListener extends KeyAdapter {

    private final Set<Integer> pressedKeys = new HashSet<>();

    private MainPane mainPane;
    private ZoomInAction zoomInAction;
    private ZoomOutAction zoomOutAction;
    private NewFileAction newFileAction;
    private OpenFileAction openFileAction;
    private SaveFileAction saveFileAction;

    public ShortcutKeyListener(MainPane mainPane) {
        this.mainPane = mainPane;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        if (pressedKeys.size() == 2) {
            onDoubleKeyPress(e);
        } else if (pressedKeys.size() == 3) {
            onTripleKeyPress(e);
        }
    }

    private void onDoubleKeyPress(KeyEvent e) {
        if (pressedKeys.contains(KeyEvent.VK_CONTROL)
                && pressedKeys.contains(KeyEvent.VK_Z)) {
            ActionEvent event = new ActionEvent(e.getSource(), 0, "Undo",
                    System.currentTimeMillis(), 0);
            mainPane.fireActionEvent(event);
        } else if (pressedKeys.contains(KeyEvent.VK_CONTROL)
                && pressedKeys.contains(KeyEvent.VK_ADD)) {
            zoomInAction.actionPerformed(new ActionEvent(e.getSource(), 0, "Zoom In",
                    System.currentTimeMillis(), 0));
        } else if (pressedKeys.contains(KeyEvent.VK_CONTROL)
                && pressedKeys.contains(KeyEvent.VK_SUBTRACT)) {
            zoomOutAction.actionPerformed(new ActionEvent(e.getSource(), 0, "Zoom Out",
                    System.currentTimeMillis(), 0));
        } else if (pressedKeys.contains(KeyEvent.VK_CONTROL)
                && pressedKeys.contains(KeyEvent.VK_N)) {
            newFileAction.actionPerformed(new ActionEvent(e.getSource(), 0, "New File",
                    System.currentTimeMillis(), 0));
        } else if (pressedKeys.contains(KeyEvent.VK_CONTROL)
                && pressedKeys.contains(KeyEvent.VK_O)) {
            openFileAction.actionPerformed(new ActionEvent(e.getSource(), 0, "Open File",
                    System.currentTimeMillis(), 0));
        } else if (pressedKeys.contains(KeyEvent.VK_CONTROL)
                && pressedKeys.contains(KeyEvent.VK_S)) {
            saveFileAction.actionPerformed(new ActionEvent(e.getSource(), 0, "Save File",
                    System.currentTimeMillis(), 0));
        }
    }

    private void onTripleKeyPress(KeyEvent e) {
        if (pressedKeys.contains(KeyEvent.VK_CONTROL)
                && pressedKeys.contains(KeyEvent.VK_SHIFT)
                && pressedKeys.contains(KeyEvent.VK_Z)) {
            ActionEvent event = new ActionEvent(e.getSource(), 0, "Redo",
                    System.currentTimeMillis(), 0);
            mainPane.fireActionEvent(event);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    public void setZoomInAction(ZoomInAction zoomInAction) {
        this.zoomInAction = zoomInAction;
    }

    public void setZoomOutAction(ZoomOutAction zoomOutAction) {
        this.zoomOutAction = zoomOutAction;
    }

    public void setNewFileAction(NewFileAction newFileAction) {
        this.newFileAction = newFileAction;
    }

    public void setOpenFileAction(OpenFileAction openFileAction) {
        this.openFileAction = openFileAction;
    }

    public void setSaveFileAction(SaveFileAction saveFileAction) {
        this.saveFileAction = saveFileAction;
    }
}
