package redcoder.texteditor.core.about;

import org.junit.Before;
import org.junit.Test;
import redcoder.texteditor.core.EditorFrame;
import redcoder.texteditor.core.help.AboutDialog;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class AboutDialogTest {

    EditorFrame editorFrame;

    @Before
    public void before(){
        editorFrame = new EditorFrame();
        editorFrame.init();
    }

    @Test
    public void about(){
        SwingUtilities.invokeLater(() -> {
            AboutDialog.showAbout(editorFrame);
        });
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
