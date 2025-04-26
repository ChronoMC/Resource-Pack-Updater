package cn.zbx1425.resourcepackupdater.util;

import cn.zbx1425.resourcepackupdater.ResourcePackUpdater;

public class MismatchingVersionException extends Exception {

    public MismatchingVersionException(String requested, String current) {
        super("\n\n" +
            String.format("资源同步程序版本不合：请您不要尝试更改本模组以破解资源 ( 您现有 %s )", requested, current) + "\n" +
            "\n\n"
        );
    }

    public MismatchingVersionException(String message) {
        super(message);
    }
}
