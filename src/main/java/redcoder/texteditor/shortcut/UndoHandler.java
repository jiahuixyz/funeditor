package redcoder.texteditor.shortcut;

import redcoder.texteditor.MainPane;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Set;

public class UndoHandler implements ShortcutKeyHandler {

    private MainPane mainPane;

    public UndoHandler(MainPane mainPane) {
        this.mainPane = mainPane;
    }

    @Override
    public boolean handle(KeyEvent e, Set<Integer> pressedKeys) {
        if (pressedKeys.contains(KeyEvent.VK_CONTROL)
                && pressedKeys.contains(KeyEvent.VK_Z)) {
            ActionEvent event = new ActionEvent(e.getSource(), 0, "Undo",
                    System.currentTimeMillis(), 0);
            mainPane.fireActionEvent(event);

            return false;
        }
        return true;
    }
}
