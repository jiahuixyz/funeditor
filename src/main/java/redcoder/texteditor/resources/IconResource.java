package redcoder.texteditor.resources;

import javax.swing.*;
import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class IconResource {

    private static final Map<String, ImageIcon> ICONS = new HashMap<>();

    static {
        loadDefaultIcon();
    }

    /**
     * 获取指定名称的图标资源
     *
     * @param iconName 图标名称
     * @return 图标资源
     */
    public static ImageIcon getImageIcon(String iconName) {
        ImageIcon imageIcon = ICONS.get(iconName);
        if (imageIcon == null) {
            URL url = IconResource.class.getResource(iconName);
            if (url == null) {
                url = IconResource.class.getClassLoader().getResource(iconName);
            }
            if (url != null) {
                imageIcon = new ImageIcon(url);
                ICONS.put(iconName, imageIcon);
            }
        }
        return imageIcon;
    }

    private static void loadDefaultIcon() {
        try {
            loadImageIcon("images/icons");
            loadImageIcon("toolbarButtonGraphics/general");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadImageIcon(String path) throws Exception {
        ClassLoader classLoader = IconResource.class.getClassLoader();
        URL url = classLoader.getResource(path);
        if (url == null) {
            return;
        }
        String str = url.toString();
        if (str.startsWith("file")) {
            loadLocalFile(url);
        } else if (str.startsWith("jar")) {
            loadJarFile(classLoader, url);
        } else {
            System.err.println("Unknown URL[ " + str + "], we can't handle it.");
        }
    }

    private static void loadLocalFile(URL url) throws Exception {
        File file = new File(url.toURI());
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isFile()) {
                    String iconName = f.getName();
                    String path = f.getAbsolutePath();
                    ICONS.put(iconName, new ImageIcon(path));
                }
            }
        }
    }

    private static void loadJarFile(ClassLoader classLoader, URL url) throws Exception {
        JarURLConnection connection = (JarURLConnection) url.openConnection();
        JarFile jarFile = connection.getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            if (name.endsWith(".gif") || name.endsWith(".png")) {
                URL resource = classLoader.getResource(name);
                if (resource != null) {
                    String iconName = name.substring(name.lastIndexOf("/") + 1);
                    ICONS.put(iconName, new ImageIcon(resource));
                }
            }
        }
    }
}
