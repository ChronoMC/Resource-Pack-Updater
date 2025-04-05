package com.mrpdev.gui.forms;

import com.mrpdev.gui.gl.GlHelper;
import com.mrpdev.io.ProgressReceiver;

public interface GlScreenForm extends ProgressReceiver {

    void render();

    boolean shouldStopPausing();

    void reset();

    int FONT_SIZE = 24;
    int LINE_HEIGHT = 30;

    static void drawShadowRect(float width, float height, int color) {
        GlHelper.blit(8, 8, width, height, 0x66000000);
        GlHelper.blit(0, 0, width, height, color);
    }
}
