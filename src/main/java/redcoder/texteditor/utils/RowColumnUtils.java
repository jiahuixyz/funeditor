package redcoder.texteditor.utils;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RowColumnUtils {

    private static final Logger LOGGER = Logger.getLogger(RowColumnUtils.class.getName());

    public static int getRow(int caretDot, JTextComponent editor) {
        int row = (caretDot == 0) ? 1 : 0;
        try {
            int offs = caretDot;
            while (offs > 0) {
                offs = Utilities.getRowStart(editor, offs) - 1;
                row++;
            }
        } catch (BadLocationException e) {
            LOGGER.log(Level.WARNING, "getRow error", e);
        }
        return row;
    }

    public static int getColumn(int caretDot, JTextComponent editor) {
        try {
            return caretDot - Utilities.getRowStart(editor, caretDot) + 1;
        } catch (BadLocationException e) {
            LOGGER.log(Level.WARNING, "getColumn error", e);
        }
        return -1;
    }
}
