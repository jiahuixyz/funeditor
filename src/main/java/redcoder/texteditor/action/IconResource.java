package redcoder.texteditor.action;

import javax.swing.*;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class IconResource {

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
    static ImageIcon getImageIcon(String iconName) {
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
            ClassLoader classLoader = IconResource.class.getClassLoader();
            URL url = classLoader.getResource("toolbarButtonGraphics");
            if (url == null) {
                return;
            }
            JarURLConnection connection = (JarURLConnection) url.openConnection();
            JarFile jarFile = connection.getJarFile();
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.endsWith(".gif")) {
                    Optional.ofNullable(classLoader.getResource(name))
                            .ifPresent(icon -> {
                                String iconName = name.substring(name.lastIndexOf("/") + 1);
                                ICONS.put(iconName, new ImageIcon(icon));
                            });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
