package redcoder.texteditor.action;

import redcoder.texteditor.MainPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 缩小字体
 */
public class ZoomOutAction extends AbstractAction {

    private static final int FONT_SIZE_MINIMUM = 10;

    private MainPane mainPane;

    public ZoomOutAction(MainPane mainPane) {
        super("Zoom Out");
        this.mainPane = mainPane;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        for (Component component : mainPane.getComponents()) {
            Font font = component.getFont();
            component.setFont(new Font(font.getName(), font.getStyle(), getNewSize(font.getSize())));
        }
    }

    private int getNewSize(int currentSize) {
        int size = currentSize - 1;
        return Math.max(size, FONT_SIZE_MINIMUM);
    }
}
