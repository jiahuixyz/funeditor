package redcoder.texteditor.shortcut;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShortcutKeyHandlerChain {

    private List<ShortcutKeyHandler> handlers = new ArrayList<>();

    public void addHandler(ShortcutKeyHandler handler) {
        this.handlers.add(handler);
    }

    public List<ShortcutKeyHandler> getHandlers() {
        return this.handlers;
    }

    public boolean applyHandle(KeyEvent e, Set<Integer> pressedKeys) {
        for (ShortcutKeyHandler handler : handlers) {
            if (!handler.handle(e, pressedKeys)) {
                return false;
            }
        }
        return true;
    }

}
