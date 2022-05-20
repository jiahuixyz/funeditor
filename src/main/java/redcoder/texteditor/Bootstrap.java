package redcoder.texteditor;

import com.formdev.flatlaf.FlatLightLaf;
import redcoder.texteditor.core.Framework;

public class Bootstrap {

    public static void main(String[] args) {
        FlatLightLaf.setup();
        Framework.makeNewWindow();
    }

}
