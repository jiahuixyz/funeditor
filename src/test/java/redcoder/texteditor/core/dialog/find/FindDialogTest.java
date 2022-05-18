package redcoder.texteditor.core.dialog.find;

import org.junit.Test;
import redcoder.texteditor.core.find.FindDialog;

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
