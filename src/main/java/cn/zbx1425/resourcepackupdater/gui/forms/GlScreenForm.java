package cn.zbx1425.resourcepackupdater.gui.forms;

import cn.zbx1425.resourcepackupdater.gui.gl.GlHelper;
import cn.zbx1425.resourcepackupdater.io.ProgressReceiver;

public interface GlScreenForm extends ProgressReceiver {

    void render();

    boolean shouldStopPausing();

    void reset();

    int FONT_SIZE = 24;
    int LINE_HEIGHT = 30;

    static void drawShadowRect(float width, float height, int color) {
        GlHelper.blitShadow(0, 0, width, height, 8, 8, 0x66000000);
        GlHelper.blitRounded(0, 0, width, height, 8, color);
    }
}
