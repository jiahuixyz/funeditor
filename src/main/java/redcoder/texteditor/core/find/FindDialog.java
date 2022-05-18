package redcoder.texteditor.core.find;

import redcoder.texteditor.core.EditorFrame;
import redcoder.texteditor.utils.StringUtils;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
        dialog.setPreferredSize(new Dimension(320, 160));

        dialog.initDialog();

        dialog.pack();
        dialog.setVisible(true);
    }

    private void initDialog() {
        textField = new JTextField();

        caseCheckBox = new JCheckBox("Match Case");
        caseCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        caseCheckBox.addChangeListener(e -> {
            try {
                reset();
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });

        wholeCheckBox = new JCheckBox("Whole Words");
        wholeCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        wholeCheckBox.addChangeListener(e -> {
            try {
                reset();
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });

        countLabel = new JLabel("0 result");
        countLabel.setForeground(Color.BLUE);
        countLabel.setBorder(BorderFactory.createEmptyBorder(3, 2, 3, 2));

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        initPanel1(panel1);
        initPanel2(panel2);

        add(panel1, BorderLayout.CENTER);
        add(panel2, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                removeOldHighlights(textArea);
            }
        });
    }

    private void initPanel1(JPanel panel1) {
        JLabel label = new JLabel("Find What:");
        JButton prevButton = new JButton("Find Prev");
        JButton nextButton = new JButton("Find Next");
        prevButton.addActionListener(this);
        nextButton.addActionListener(this);

        GroupLayout layout = new GroupLayout(panel1);
        panel1.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        // horizontal group
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(textField)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(LEADING)
                                        .addComponent(caseCheckBox)
                                        .addComponent(wholeCheckBox))))
                .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(prevButton)
                        .addComponent(nextButton))
        );
        // fixed size
        layout.linkSize(SwingConstants.HORIZONTAL, prevButton, nextButton);
        // vertical group
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(label)
                        .addComponent(textField)
                        .addComponent(prevButton))
                .addGroup(layout.createParallelGroup(LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(caseCheckBox))
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(wholeCheckBox)))
                        .addComponent(nextButton))
        );
    }

    private void initPanel2(JPanel panel2) {
        panel2.setLayout(new BorderLayout());
        panel2.add(countLabel, BorderLayout.LINE_START);
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
            if (Objects.equals("Find Prev", command)) {
                findPrev(length, text, pattern, highlighter);
            } else if (Objects.equals("Find Next", command)) {
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
}
