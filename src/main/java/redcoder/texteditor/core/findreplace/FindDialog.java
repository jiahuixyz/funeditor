package redcoder.texteditor.core.findreplace;

import redcoder.texteditor.core.EditorFrame;
import redcoder.texteditor.utils.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Objects;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;

public class FindDialog extends JDialog implements ActionListener {

    private final JTextArea textArea;
    private JTextField textField;
    private JCheckBox caseCheckBox;
    private JCheckBox wholeCheckBox;
    private JLabel countLabel;
    private String input = null;
    private int position = -1;
    private int cursor = 0;
    private int totalMatch = 0;

    private FindDialog(EditorFrame frame) {
        super(frame, "Find", false);
        this.textArea = frame.getTabPane().getSelectedTextPane().getTextArea();
    }

    public static void showDialog(EditorFrame frame) {
        FindDialog dialog = new FindDialog(frame);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(true);

        dialog.initDialog();

        dialog.pack();
        dialog.setVisible(true);
    }

    private void initDialog() {
        JLabel fwLabel = new JLabel("Find What:");
        textField = new JTextField();

        caseCheckBox = new JCheckBox("Match Case");
        caseCheckBox.addChangeListener(new CheckBoxSelectedStateChangeListener(caseCheckBox));

        wholeCheckBox = new JCheckBox("Whole Words");
        wholeCheckBox.addChangeListener(new CheckBoxSelectedStateChangeListener(wholeCheckBox));

        countLabel = new JLabel("0 results");
        countLabel.setForeground(Color.BLUE);

        JButton prevButton = createButton(UP_CMD);
        JButton nextButton = createButton(DOWN_CMD);

        // specify layout
        Container rootPane = getContentPane();
        GroupLayout layout = new GroupLayout(rootPane);
        rootPane.setLayout(layout);

        // enable auto gap
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        // layout child component
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(fwLabel)
                .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(textField)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(caseCheckBox)
                                .addComponent(wholeCheckBox)))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(countLabel)
                        .addComponent(prevButton, 20, 20, 20)
                        .addComponent(nextButton, 20, 20, 20)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(fwLabel)
                        .addComponent(textField)
                        .addComponent(countLabel)
                        .addComponent(prevButton, 20, 20, 20)
                        .addComponent(nextButton, 20, 20, 20))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(caseCheckBox)
                        .addComponent(wholeCheckBox)));

        // add window listener
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                removeOldHighlights(textArea);
            }
        });
    }

    private JButton createButton(String command) {
        JButton button = new JButton();
        if (Objects.equals(command, UP_CMD) && UP != null) {
            button.setIcon(UP);
        } else if (Objects.equals(command, DOWN_CMD) && DOWN != null) {
            button.setIcon(DOWN);
        } else {
            button.setText(command);
        }
        button.setActionCommand(command);
        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        try {
            if (!Objects.equals(textField.getText(), input)) {
                reset();
            }
            if (totalMatch == 0) {
                return;
            }

            int length = textArea.getDocument().getLength();
            String text = textArea.getDocument().getText(0, length);
            String pattern = input;
            if (!caseCheckBox.isSelected()) {
                text = text.toLowerCase();
                pattern = pattern.toLowerCase();
            }
            Highlighter highlighter = textArea.getHighlighter();
            if (Objects.equals(UP_CMD, command)) {
                findPrev(length, text, pattern, highlighter);
            } else if (Objects.equals(DOWN_CMD, command)) {
                findNext(length, text, pattern, highlighter);
            }
            countLabel.setText(String.format("%d/%d", cursor, totalMatch));
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    private void reset() throws BadLocationException {
        removeOldHighlights(textArea);
        input = textField.getText();
        position = -1;
        cursor = 0;
        totalMatch = countMatch();
        countLabel.setText("0 results");
    }

    private int countMatch() throws BadLocationException {
        if (StringUtils.isEmpty(textField.getText())) {
            return 0;
        }

        int length = textArea.getDocument().getLength();
        String text = textArea.getDocument().getText(0, length);
        String pattern = input;
        if (!caseCheckBox.isSelected()) {
            text = text.toLowerCase();
            pattern = pattern.toLowerCase();
        }

        int pos = 0;
        int count = 0;
        while ((pos = text.indexOf(pattern, pos)) >= 0) {
            if (wholeCheckBox.isSelected()) {
                if (isWord(pos, pattern, length, text)) {
                    count++;
                }
            } else {
                count++;
            }
            pos += pattern.length();
        }
        return count;
    }

    private void findPrev(int length, String text, String pattern, Highlighter highlighter) throws BadLocationException {
        while (true) {
            position--;
            if (position < 0) {
                position = length - 1;
                cursor = totalMatch + 1;
            }
            String subText = text.substring(0, position);
            int matchIndex = subText.lastIndexOf(pattern);
            if (matchIndex >= 0) {
                if (wholeCheckBox.isSelected()) {
                    if (isWord(matchIndex, pattern, length, text)) {
                        position = matchIndex;
                        cursor--;
                        removeOldHighlights(textArea);
                        highlighter.addHighlight(matchIndex, matchIndex + pattern.length(), new FindHighlightPainter());
                        return;
                    }
                } else {
                    position = matchIndex;
                    cursor--;
                    removeOldHighlights(textArea);
                    highlighter.addHighlight(matchIndex, matchIndex + pattern.length(), new FindHighlightPainter());
                    return;
                }
            }
        }
    }

    private void findNext(int length, String text, String pattern, Highlighter highlighter) throws BadLocationException {
        while (true) {
            position++;
            if (position >= length) {
                position = 0;
                cursor = 0;
            }
            int matchIndex = text.indexOf(pattern, position);
            if (matchIndex >= 0) {
                if (wholeCheckBox.isSelected()) {
                    if (isWord(position, pattern, length, text)) {
                        position = matchIndex;
                        cursor++;
                        removeOldHighlights(textArea);
                        highlighter.addHighlight(matchIndex, matchIndex + pattern.length(), new FindHighlightPainter());
                        return;
                    }
                } else {
                    position = matchIndex;
                    cursor++;
                    removeOldHighlights(textArea);
                    highlighter.addHighlight(matchIndex, matchIndex + pattern.length(), new FindHighlightPainter());
                    return;
                }
            }
        }
    }

    private void removeOldHighlights(JTextComponent textComp) {
        Highlighter highlighter = textComp.getHighlighter();
        Highlighter.Highlight[] highlights = highlighter.getHighlights();
        for (Highlighter.Highlight highlight : highlights) {
            if (highlight.getPainter() instanceof FindHighlightPainter) {
                highlighter.removeHighlight(highlight);
            }
        }
    }

    private boolean isWord(int startIndex, String pattern, int length, String text) {
        boolean isWord1, isWord2;
        if (startIndex > 0) {
            isWord1 = !Character.isLetterOrDigit(text.charAt(startIndex - 1));
        } else {
            isWord1 = true;
        }

        if ((startIndex + pattern.length()) < length) {
            isWord2 = !Character.isLetterOrDigit(text.charAt(startIndex + pattern.length()));
        } else {
            isWord2 = true;
        }

        return isWord1 && isWord2;
    }


    private class CheckBoxSelectedStateChangeListener implements ChangeListener {

        private final JCheckBox checkBox;
        private boolean selected;

        public CheckBoxSelectedStateChangeListener(JCheckBox checkBox) {
            this.checkBox = checkBox;
            selected = checkBox.isSelected();
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            Object source = e.getSource();
            if (source == checkBox) {
                if (selected != checkBox.isSelected()) {
                    try {
                        reset();
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                    selected = checkBox.isSelected();
                }
            }
        }
    }

    static final String UP_CMD = "UP";
    static final String DOWN_CMD = "DOWN";
    static final ImageIcon UP;
    static final ImageIcon DOWN;

    static {
        URL url = FindDialog.class.getClassLoader().getResource("images/find_up.png");
        if (url != null) {
            UP = new ImageIcon(url);
        } else {
            UP = null;
        }

        url = FindDialog.class.getClassLoader().getResource("images/find_down.png");
        if (url != null) {
            DOWN = new ImageIcon(url);
        } else {
            DOWN = null;
        }
    }

}
