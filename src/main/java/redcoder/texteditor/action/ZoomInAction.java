package redcoder.texteditor.action;

import redcoder.texteditor.MainPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 放大字体
 */
public class ZoomInAction extends AbstractAction {

    private static final int FONT_SIZE_MAXIMUM = 1000;

    private MainPane mainPane;

    public ZoomInAction(MainPane mainPane) {
        super("Zoom In");
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
        int size = currentSize + 2;
        return Math.min(size, FONT_SIZE_MAXIMUM);
    }
}
