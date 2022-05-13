package redcoder.texteditor.utils;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;

public class RowColumnUtils {

    public static int getRow(int caretDot, JTextComponent editor) {
        int row = (caretDot == 0) ? 1 : 0;
        try {
            int offs = caretDot;
            while (offs > 0) {
                offs = Utilities.getRowStart(editor, offs) - 1;
                row++;
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return row;
    }

    public static int getColumn(int caretDot, JTextComponent editor) {
        try {
            return caretDot - Utilities.getRowStart(editor, caretDot) + 1;
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
