package redcoder.texteditor.shortcut;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Set;

public class ZoomOutHandler extends AbstractShortcutKeyHandler {

    public ZoomOutHandler(Action action) {
        super(action);
    }

    @Override
    public boolean handle(KeyEvent e, Set<Integer> pressedKeys) {
        if (pressedKeys.contains(KeyEvent.VK_CONTROL)
                && pressedKeys.contains(KeyEvent.VK_SUBTRACT)) {
            ActionEvent event = new ActionEvent(e.getSource(), 0, "Zoom Out",
                    System.currentTimeMillis(), 0);
            action.actionPerformed(event);

            return false;
        }
        return true;
    }
}
