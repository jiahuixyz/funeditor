package redcoder.texteditor.shortcut;

import javax.swing.*;

public abstract class AbstractShortcutKeyHandler implements ShortcutKeyHandler {

    protected Action action;

    public AbstractShortcutKeyHandler(Action action) {
        this.action = action;
    }
}
