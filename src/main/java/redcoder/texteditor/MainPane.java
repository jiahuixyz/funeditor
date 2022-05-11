package redcoder.texteditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 编辑器主窗格，支持ActionListener机制
 */
public class MainPane extends JTabbedPane {

    private ScrollTextPane selectedScrollTextPane;

    public MainPane() {
        setFont(new Font(null, Font.PLAIN, 16));
        addChangeListener(e -> selectedScrollTextPane = (ScrollTextPane) getSelectedComponent());
    }

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

    public ScrollTextPane getSelectedTextPane() {
        return selectedScrollTextPane;
    }
}
