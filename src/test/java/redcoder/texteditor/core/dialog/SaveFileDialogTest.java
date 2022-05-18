package redcoder.texteditor.core.dialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SaveFileDialogTest {

    public static void main(String[] args) {
        JFrame frame = new JFrame("SaveFileDialogTest");
        JButton button = new JButton("button");
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SaveFileDialog dialog = new SaveFileDialog(frame);
                dialog.setVisible(true);
            }
        });
        frame.add(button);
        frame.setVisible(true);
    }
}
