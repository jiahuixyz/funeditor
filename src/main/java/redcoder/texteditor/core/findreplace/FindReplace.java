package redcoder.texteditor.core.findreplace;

import redcoder.texteditor.core.EditorFrame;
import redcoder.texteditor.utils.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Objects;

import static javax.swing.GroupLayout.Alignment.*;

public class FindReplace implements ActionListener {

    private final JTextArea textArea;

    // replace dialog required component and filed
    private JTextField replaceTextField;
    private String replaceInput = null;
    private JButton replaceButton;
    private JButton replaceAllButton;
    private boolean replaceable = false;

    // shared component and filed
    private JTextField findTextField;
    private String findInput = null;
    private JButton prevButton;
    private JButton nextButton;
    private JCheckBox caseCheckBox;
    private JCheckBox wholeCheckBox;
    private JLabel countLabel;
    private int position = -1;
    private int cursor = 0;
    private int totalMatch = 0;

    public static void showFindDialog(EditorFrame frame) {
        if (frame.getFindDialog() != null) {
            return;
        }
        FindReplace findReplace = new FindReplace(frame, Type.FIND);
        findReplace.createDialog(frame, Type.FIND);
    }

    public static void showReplaceDialog(EditorFrame frame) {
        if (frame.getReplaceDialog() != null) {
            return;
        }
        FindReplace findReplace = new FindReplace(frame, Type.REPLACE);
        findReplace.createDialog(frame, Type.REPLACE);
    }

    private FindReplace(EditorFrame frame, Type type) {
        textArea = frame.getTabPane().getSelectedTextPane().getTextArea();
        findTextField = new JTextField();

        caseCheckBox = new JCheckBox("Match Case");
        caseCheckBox.addChangeListener(new CheckBoxSelectedStateChangeListener(caseCheckBox));
        wholeCheckBox = new JCheckBox("Whole Words");
        wholeCheckBox.addChangeListener(new CheckBoxSelectedStateChangeListener(wholeCheckBox));

        countLabel = new JLabel("0 results", SwingConstants.CENTER);
        countLabel.setForeground(Color.BLUE);

        prevButton = createButton(UP_CMD);
        nextButton = createButton(DOWN_CMD);

        if (type == Type.REPLACE) {
            replaceTextField = new JTextField();
            replaceButton = createButton(REPLACE_CMD);
            replaceAllButton = createButton(REPLACE_ALL_CMD);
        }
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

    private void createDialog(EditorFrame frame, Type type) {
        JDialog dialog = new JDialog(frame, type.title);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(true);

        // init dialog
        if (type == Type.FIND) {
            initFindDialog(dialog);
            frame.setFindDialog(dialog);
        } else {
            initReplaceDialog(dialog);
            frame.setReplaceDialog(dialog);
        }

        // add window listener
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                removeOldHighlights(textArea);
                if (type == Type.FIND) {
                    frame.setFindDialog(null);
                } else {
                    frame.setReplaceDialog(null);
                }
            }
        });

        dialog.pack();
        dialog.setVisible(true);
    }

    private void initFindDialog(JDialog dialog) {
        JLabel findLabel = new JLabel("Find What:");

        // specify layout
        Container rootPane = dialog.getContentPane();
        GroupLayout layout = new GroupLayout(rootPane);
        rootPane.setLayout(layout);

        // enable auto gap
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        // layout child component
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(findLabel)
                .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(findTextField)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(caseCheckBox)
                                .addComponent(wholeCheckBox)))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(countLabel, 60, 60, 60)
                        .addComponent(prevButton, 20, 20, GroupLayout.DEFAULT_SIZE)
                        .addComponent(nextButton, 20, 20, GroupLayout.DEFAULT_SIZE)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(findLabel)
                        .addComponent(findTextField)
                        .addComponent(countLabel, 30, 30, 30)
                        .addComponent(prevButton, 20, 20, GroupLayout.DEFAULT_SIZE)
                        .addComponent(nextButton, 20, 20, GroupLayout.DEFAULT_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(caseCheckBox)
                        .addComponent(wholeCheckBox)));
    }

    private void initReplaceDialog(JDialog dialog) {
        JLabel findLabel = new JLabel("Find What:");
        JLabel replaceLabel = new JLabel("Replace With:");

        // specify layout
        Container rootPane = dialog.getContentPane();
        GroupLayout layout = new GroupLayout(rootPane);
        rootPane.setLayout(layout);

        // enable auto gap
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        // layout child component
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(findLabel)
                        .addComponent(replaceLabel))
                .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(findTextField)
                        .addComponent(replaceTextField)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(caseCheckBox)
                                .addComponent(wholeCheckBox)))
                .addGroup(layout.createParallelGroup(TRAILING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(countLabel)
                                .addComponent(prevButton, 20, 20, GroupLayout.DEFAULT_SIZE)
                                .addComponent(nextButton, 20, 20, GroupLayout.DEFAULT_SIZE))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(replaceButton)
                                .addComponent(replaceAllButton))));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(findLabel)
                        .addComponent(findTextField)
                        .addComponent(countLabel)
                        .addComponent(prevButton, 20, 20, GroupLayout.DEFAULT_SIZE)
                        .addComponent(nextButton, 20, 20, GroupLayout.DEFAULT_SIZE))
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(replaceLabel)
                        .addComponent(replaceTextField)
                        .addComponent(replaceButton)
                        .addComponent(replaceAllButton))
                .addGroup(layout.createParallelGroup(CENTER)
                        .addComponent(caseCheckBox)
                        .addComponent(wholeCheckBox)));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        try {
            if (!Objects.equals(findTextField.getText(), findInput)) {
                reset();
            }
            if (totalMatch == 0) {
                return;
            }

            int length = textArea.getDocument().getLength();
            String text = textArea.getDocument().getText(0, length);
            String pattern = findInput;
            if (!caseCheckBox.isSelected()) {
                text = text.toLowerCase();
                pattern = pattern.toLowerCase();
            }
            Highlighter highlighter = textArea.getHighlighter();
            if (Objects.equals(UP_CMD, command)) {
                findPrev(length, text, pattern, highlighter);
            } else if (Objects.equals(DOWN_CMD, command)) {
                findNext(length, text, pattern, highlighter);
            } else if (Objects.equals(REPLACE_CMD, command)) {
                updateReplaceInput();
                replace(length, text, pattern, highlighter);
            } else if (Objects.equals(REPLACE_ALL_CMD, command)) {
                updateReplaceInput();
                replaceAll(length, text, pattern, highlighter);
            }
            countLabel.setText(String.format("%d/%d", cursor, totalMatch));
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    private void reset() throws BadLocationException {
        removeOldHighlights(textArea);
        findInput = findTextField.getText();
        position = -1;
        cursor = 0;
        totalMatch = countMatch();
        countLabel.setText("0 results");
    }

    private int countMatch() throws BadLocationException {
        if (StringUtils.isEmpty(findTextField.getText())) {
            return 0;
        }

        int length = textArea.getDocument().getLength();
        String text = textArea.getDocument().getText(0, length);
        String pattern = findInput;
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
                        replaceable = true;
                        removeOldHighlights(textArea);
                        highlighter.addHighlight(matchIndex, matchIndex + pattern.length(), new FindHighlightPainter());
                        return;
                    }
                } else {
                    position = matchIndex;
                    cursor--;
                    replaceable = true;
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
                        replaceable = true;
                        removeOldHighlights(textArea);
                        highlighter.addHighlight(matchIndex, matchIndex + pattern.length(), new FindHighlightPainter());
                        return;
                    }
                } else {
                    position = matchIndex;
                    cursor++;
                    replaceable = true;
                    removeOldHighlights(textArea);
                    highlighter.addHighlight(matchIndex, matchIndex + pattern.length(), new FindHighlightPainter());
                    return;
                }
            }
        }
    }

    private void updateReplaceInput() {
        replaceInput = replaceTextField.getText();
        if (replaceInput == null) {
            replaceInput = "";
        }
    }

    private void replace(int length, String text, String pattern, Highlighter highlighter) throws BadLocationException {
        if (!replaceable) {
            findNext(length, text, pattern, highlighter);
        }
        Document document = textArea.getDocument();
        document.remove(position, pattern.length());
        document.insertString(position, replaceInput, null);
        totalMatch--;
        cursor--;
        replaceable = false;
        if (totalMatch > 0) {
            findNext(length, text, pattern, highlighter);
        }
    }

    private void replaceAll(int length, String text, String pattern, Highlighter highlighter) throws BadLocationException {
        while (totalMatch > 0) {
            replace(length, text, pattern, highlighter);
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

    static final String UP_CMD = "Up";
    static final String DOWN_CMD = "Down";
    static final String REPLACE_CMD = "Replace";
    static final String REPLACE_ALL_CMD = "Replace All";
    static final ImageIcon UP;
    static final ImageIcon DOWN;

    static {
        URL url = FindReplace.class.getClassLoader().getResource("images/find_up.png");
        if (url != null) {
            UP = new ImageIcon(url);
        } else {
            UP = null;
        }

        url = FindReplace.class.getClassLoader().getResource("images/find_down.png");
        if (url != null) {
            DOWN = new ImageIcon(url);
        } else {
            DOWN = null;
        }
    }

}
