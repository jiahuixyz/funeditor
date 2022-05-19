package redcoder.texteditor.core.dialog.findreplace;

import org.junit.Before;
import org.junit.Test;
import redcoder.texteditor.core.EditorFrame;
import redcoder.texteditor.core.findreplace.FindReplace;
import redcoder.texteditor.core.tabpane.TabPane;
import redcoder.texteditor.core.textpane.ScrollTextPane;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class FindReplaceTest {

    EditorFrame editorFrame;

    @Before
    public void before(){
        editorFrame = new EditorFrame();
        editorFrame.createAndShowGUI();
    }

    @Test
    public void find() {
        SwingUtilities.invokeLater(() -> {
            FindReplace.showFindDialog(editorFrame);
        });
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void replace() {
        SwingUtilities.invokeLater(() -> {
            FindReplace.showReplaceDialog(editorFrame);
        });
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
