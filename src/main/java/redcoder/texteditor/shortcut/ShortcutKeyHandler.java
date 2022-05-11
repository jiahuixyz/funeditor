package redcoder.texteditor.shortcut;

import java.awt.event.KeyEvent;
import java.util.Set;

public interface ShortcutKeyHandler {

    boolean handle(KeyEvent e, Set<Integer> pressedKeys);
}
