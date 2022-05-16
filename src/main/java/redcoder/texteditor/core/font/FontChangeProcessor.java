package redcoder.texteditor.core.font;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FontChangeProcessor {

    private static final Font DEFAULT_FONT = new Font(null, Font.PLAIN, 20);
    private static final List<FontChangeListener> LISTENERS = new ArrayList<>();
    private static final int FONT_SIZE_MINIMUM = 10;
    private static final int FONT_SIZE_MAXIMUM = 100;
    private static Font font = DEFAULT_FONT;

    public static void addListener(FontChangeListener listener) {
        LISTENERS.add(listener);
    }

    public static void removeListener(FontChangeListener listener) {
        LISTENERS.remove(listener);
    }

    public static Font getSharedFont() {
        return font;
    }

    public static void zoomIn(Object source) {
        int fontSize = Math.min(font.getSize() + 2, FONT_SIZE_MAXIMUM);
        font = new Font(font.getName(), font.getStyle(), fontSize);
        fireChangeEvent(source);
    }

    public static void zoomOut(Object source) {
        int fontSize = Math.max(font.getSize() - 2, FONT_SIZE_MINIMUM);
        font = new Font(font.getName(), font.getStyle(), fontSize);
        fireChangeEvent(source);
    }

    private static void fireChangeEvent(Object source) {
        FontChangeEvent event = new FontChangeEvent(source, font);
        for (FontChangeListener listener : LISTENERS) {
            listener.onChange(event);
        }
    }
}
