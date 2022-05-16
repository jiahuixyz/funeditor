package redcoder.texteditor.core.fontsize;

import java.util.EventObject;

/**
 * 字体大小变化事件
 */
public class FontSizeChangeEvent extends EventObject {

    private final int fontSize;

    public FontSizeChangeEvent(Object source, int fontSize) {
        super(source);
        this.fontSize = fontSize;
    }

    /**
     * 新的字体大小
     */
    public int getFontSize() {
        return fontSize;
    }
}
