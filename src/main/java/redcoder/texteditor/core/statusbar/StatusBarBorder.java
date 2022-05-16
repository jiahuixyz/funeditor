package redcoder.texteditor.core.statusbar;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * 自定义的border，用于状态栏组件
 */
public class StatusBarBorder extends AbstractBorder {

    public static final StatusBarBorder BORDER = new StatusBarBorder();

    private StatusBarBorder(){
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Color oldColor = g.getColor();

        g.setColor(new Color(214, 215, 214));
        g.drawLine(0, 3, 0, height - 3);

        g.setColor(oldColor);
    }
}
