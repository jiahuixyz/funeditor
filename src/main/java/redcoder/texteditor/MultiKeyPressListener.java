package redcoder.texteditor;

import redcoder.texteditor.action.ZoomInAction;
import redcoder.texteditor.action.ZoomOutAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiKeyPressListener extends KeyAdapter {

    private final Set<Integer> pressedKeys = new HashSet<>();
    private final AtomicInteger counter = new AtomicInteger(1);

    private MainPane mainPane;
    private ZoomInAction zoomInAction;
    private ZoomOutAction zoomOutAction;

    public MultiKeyPressListener(MainPane mainPane) {
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
            TextPane textPane = new TextPane(this);
            int i = counter.incrementAndGet();
            mainPane.addActionListener(textPane);
            mainPane.addTab("new-" + i, textPane);
            mainPane.setSelectedComponent(textPane);
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
}
