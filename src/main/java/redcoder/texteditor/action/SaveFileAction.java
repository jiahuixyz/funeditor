package redcoder.texteditor.action;

import redcoder.texteditor.MainPane;
import redcoder.texteditor.utils.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class SaveFileAction extends AbstractAction {

    private MainPane mainPane;
    private JFileChooser fileChooser;

    public SaveFileAction(MainPane mainPane, JFileChooser fileChooser) {
        super("Save File");
        this.mainPane = mainPane;
        this.fileChooser = fileChooser;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        int i = fileChooser.showOpenDialog(mainPane);
        if (i == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file.exists()) {
                String message = String.format("The file '%s' is exist, would you like overriding it?", file.getName());
                int n = JOptionPane.showConfirmDialog(mainPane, message, "Warn!", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    saveFile(file);
                }
            } else {
                saveFile(file);
            }
        }
    }

    private void saveFile(File file) {
        JTextPane textPane = mainPane.getSelectedTextPane().getTextPane();
        FileUtils.writeFile(textPane.getText(), file);

        // update selected tab title
        int selectedIndex = mainPane.getSelectedIndex();
        mainPane.setTitleAt(selectedIndex, file.getName());
    }
}
