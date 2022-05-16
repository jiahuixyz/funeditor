package redcoder.texteditor.core.fontsize;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FontZoomInZoomOutProcessor {

    private static final Font DEFAULT_FONT = new Font(null, Font.PLAIN, 20);
    private static final List<FontSizeChangeListener> LISTENERS = new ArrayList<>();
    private static final int FONT_SIZE_MINIMUM = 10;
    private static final int FONT_SIZE_MAXIMUM = 100;
    private static int fontSize = DEFAULT_FONT.getSize();

    public static void addListener(FontSizeChangeListener listener) {
        LISTENERS.add(listener);
    }

    public static void removeListener(FontSizeChangeListener listener) {
        LISTENERS.remove(listener);
    }

    public static Font getSharedFont() {
        return new Font(DEFAULT_FONT.getName(), DEFAULT_FONT.getStyle(), fontSize);
    }

    public static void zoomIn(Object source) {
        fontSize = Math.min(fontSize + 2, FONT_SIZE_MAXIMUM);
        fireChangeEvent(source, fontSize);
    }

    public static void zoomOut(Object source) {
        fontSize = Math.max(fontSize - 2, FONT_SIZE_MINIMUM);
        fireChangeEvent(source, fontSize);
    }

    private static void fireChangeEvent(Object source, int fontSize) {
        FontSizeChangeEvent event = new FontSizeChangeEvent(source, fontSize);
        for (FontSizeChangeListener listener : LISTENERS) {
            listener.onChange(event);
        }
    }
}
