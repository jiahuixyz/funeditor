package redcoder.texteditor.theme;

import javax.swing.*;

public enum Theme {
    FLAT_LIGHT_LAF("FlatLightLaf", "com.formdev.flatlaf.FlatLightLaf"),
    FLAT_DARK_LAF("FlatDarkLaf", "com.formdev.flatlaf.FlatDarkLaf"),
    FLAT_INTELLIJ_LAF("FlatIntelliJLaf", "com.formdev.flatlaf.FlatIntelliJLaf"),
    FLAT_DARCULA_LAF("FlatDarculaLaf", "com.formdev.flatlaf.FlatDarculaLaf"),
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
