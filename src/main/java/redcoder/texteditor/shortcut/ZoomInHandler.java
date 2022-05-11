package redcoder.texteditor.shortcut;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Set;

public class ZoomInHandler extends AbstractShortcutKeyHandler {

    public ZoomInHandler(Action action) {
        super(action);
    }

    @Override
    public boolean handle(KeyEvent e, Set<Integer> pressedKeys) {
        if (pressedKeys.contains(KeyEvent.VK_CONTROL)
                && pressedKeys.contains(KeyEvent.VK_ADD)) {
            ActionEvent event = new ActionEvent(e.getSource(), 0, "Zoom In",
                    System.currentTimeMillis(), 0);
            action.actionPerformed(event);

            return false;
        }
        return true;
    }
}
