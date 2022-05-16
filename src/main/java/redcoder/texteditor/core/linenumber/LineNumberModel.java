package redcoder.texteditor.core.linenumber;

import java.awt.*;

/**
 * A generic model interface which defines an underlying component with line numbers.
 *
 * @author Greg Cope
 */
public interface LineNumberModel {

    /**
     * returns how many total lines the underlying model contains,
     * allowing the caller to iterate over each line.
     */
    int getNumberLines();


    /**
     * Returns a Rectangle defining the location in the view of the parameter line.
     * Only the y and height fields are required by callers.
     *
     * @param line line number
     * @return A Rectangle defining the view coordinates of the line.
     */
    Rectangle getLineRect(int line);
}