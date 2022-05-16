package redcoder.texteditor.theme;

import javax.swing.*;

public enum Theme {
    JAVA_METAL("Java Metal", "javax.swing.plaf.metal.MetalLookAndFeel"),
    FOLLOW_SYSTEM("Follow System", UIManager.getSystemLookAndFeelClassName()),
    ;
    public final String name;
    public final String clazz;

    Theme(String name, String clazz) {
        this.name = name;
        this.clazz = clazz;
    }
}
