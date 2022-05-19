package redcoder.texteditor.core.dialog.findreplace;

import org.junit.Test;
import redcoder.texteditor.core.findreplace.FindDialog;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class FindDialogTest {

    @Test
    public void test() {
        SwingUtilities.invokeLater(() -> {
            FindDialog.showDialog(null);
        });
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
