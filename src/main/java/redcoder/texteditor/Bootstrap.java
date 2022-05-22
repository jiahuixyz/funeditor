package redcoder.texteditor;

import com.formdev.flatlaf.FlatLightLaf;
import redcoder.texteditor.core.Framework;
import redcoder.texteditor.log.LoggingUtils;

public class Bootstrap {

    public static void main(String[] args) {
        LoggingUtils.resetLogManager();
        FlatLightLaf.setup();
        Framework.makeNewWindow();
    }

}
