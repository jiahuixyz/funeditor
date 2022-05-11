package redcoder.texteditor.shortcut;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * 快捷键监听器
 */
public class ShortcutKeyListener extends KeyAdapter {

    private final Set<Integer> pressedKeys = new HashSet<>();
    private final Set<String> keys = new HashSet<>();
    private ShortcutKeyHandlerChain handlerChain;

    public ShortcutKeyListener(ShortcutKeyHandlerChain handlerChain) {
        this.handlerChain = handlerChain;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        keys.add(KeyEvent.getKeyText(e.getKeyCode()));
        if (pressedKeys.size() > 1) {
            handlerChain.applyHandle(e, pressedKeys);
        }
        if ((pressedKeys.contains(KeyEvent.VK_CONTROL) && pressedKeys.contains(KeyEvent.VK_O))
                || (pressedKeys.contains(KeyEvent.VK_CONTROL) && pressedKeys.contains(KeyEvent.VK_S)))
            pressedKeys.clear();
        System.out.println("keyPressed: " + keys);
    }


    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        keys.remove(KeyEvent.getKeyText(e.getKeyCode()));
        System.out.println("keyReleased: " + keys);
    }
}
