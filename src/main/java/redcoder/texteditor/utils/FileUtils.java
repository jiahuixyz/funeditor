package redcoder.texteditor.utils;

import java.io.*;

public class FileUtils {

    public static String readFile(File file) {
        if (file.isDirectory()) {
            throw new IllegalArgumentException("Reading directory contents is not supported");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                content.append(line);
                line = reader.readLine();
                if (line != null) {
                    content.append("\n");
                }
            }
            return content.toString();
        } catch (Exception e) {
            throw new RuntimeException("Reading file contents failed");
        }
    }

    public static void writeFile(String content, File file) {
        writeFile(content, file, false);
    }

    public static void writeFile(String content, File file, boolean append) {
        if (file.isDirectory()) {
            throw new IllegalArgumentException("Writing to directory is not supported");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, append))) {
            writer.write(content);
        } catch (Exception e) {
            throw new RuntimeException("Writing to file failed");
        }
    }
}
