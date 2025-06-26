package redcoder.texteditor.core.help;

import redcoder.texteditor.core.EditorFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

public class AboutDialog extends JDialog {

    private static final Font F1 = new Font(null, Font.PLAIN, 16);
    private static final Font F2 = new Font(null, Font.PLAIN, 16);
    private static final Font F3 = new Font(null, Font.PLAIN, 16);

    public static void showAbout(EditorFrame frame) {
        JDialog dialog = new AboutDialog(frame);
        dialog.pack();
        dialog.setVisible(true);
    }

    public AboutDialog(EditorFrame frame) {
        super(frame, EditorFrame.TITLE, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frame);
        setResizable(false);

        setLayout(new BorderLayout());
        add(creatInformationPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel creatInformationPanel() {
        JLabel name = new JLabel(EditorFrame.TITLE);
        name.setFont(F1);
        name.setBorder(BorderFactory.createEmptyBorder(5, 3, 10, 3));

        JLabel author = new JLabel(AUTHOR);
        author.setFont(F2);
        name.setBorder(BorderFactory.createEmptyBorder(2, 3, 2, 3));

        JLabel version = new JLabel(VERSION);
        version.setFont(F2);
        name.setBorder(BorderFactory.createEmptyBorder(2, 3, 2, 3));

        JLabel createdAt = new JLabel(CREATED_AT);
        createdAt.setFont(F2);
        name.setBorder(BorderFactory.createEmptyBorder(2, 3, 2, 3));

        Box box = Box.createVerticalBox();
        box.add(name);
        box.add(author);
        box.add(version);
        box.add(createdAt);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(box);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    private JPanel createButtonPanel() {
        Button okButton = new Button("OK");
        okButton.setFont(F3);
        okButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                setVisible(false);
            }
        });

        Button copyButton = new Button("Copy");
        copyButton.setFont(F3);
        copyButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(COPY_TEXT), null);
                dispose();
                setVisible(false);
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(okButton);
        btnPanel.add(copyButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(btnPanel, BorderLayout.LINE_END);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 5));

        return panel;
    }

    private static final String AUTHOR = "Author: redcoder54";
    private static final String VERSION = "Version: 0.0.1";
    private static final String CREATED_AT = "Created at: 2022/5/10";
    private static final String COPY_TEXT = String.format("%s%n%s%n%s%n%s", EditorFrame.TITLE, AUTHOR, VERSION, CREATED_AT);
}
