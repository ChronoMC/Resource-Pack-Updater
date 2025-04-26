package cn.zbx1425.resourcepackupdater.gui.forms;

import cn.zbx1425.resourcepackupdater.ResourcePackUpdater;
import cn.zbx1425.resourcepackupdater.gui.gl.GlHelper;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;

public class ProgressForm implements GlScreenForm {

    private String primaryInfo = "";
    private String auxiliaryInfo = "";
    private float primaryProgress;
    private float displayProgress;
    private String secondaryProgress = "";
    private long lastUpdateTime;

    private static final float PROGRESS_FORM_WIDTH = 600;
    private static final float PROGRESS_FORM_HEIGHT = 250;
    private static final float ANIMATION_SPEED = 2.0f;

    @Override
    public void render() {
        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastUpdateTime) / 1000.0f;
        lastUpdateTime = currentTime;

        if (Math.abs(displayProgress - primaryProgress) > 0.001f) {
            displayProgress += (primaryProgress - displayProgress) * Math.min(1.0f, deltaTime * ANIMATION_SPEED);
        }

        GlHelper.setMatCenterForm(PROGRESS_FORM_WIDTH, PROGRESS_FORM_HEIGHT, 0.75f);
        GlHelper.begin(GlHelper.PRELOAD_FONT_TEXTURE);

        // 绘制主窗体
        GlHelper.blitShadow(0, 0, PROGRESS_FORM_WIDTH, PROGRESS_FORM_HEIGHT, 8, 8, 0x66000000);
        GlHelper.blitRounded(0, 0, PROGRESS_FORM_WIDTH, PROGRESS_FORM_HEIGHT, 8, 0xf5ffffff);

        float barBegin = 20;
        float barHeight = 36;
        float barRadius = barHeight / 2;
        float usableBarWidth = PROGRESS_FORM_WIDTH - barBegin * 2;

        GlHelper.blitRounded(barBegin, 40, usableBarWidth, barHeight, barRadius, 0x22000000);

        // 在ProgressForm中修改绘制逻辑：
        if (displayProgress > 0.001f) {
            float progressWidth = usableBarWidth * displayProgress;
            float effectiveWidth = Math.min(progressWidth, usableBarWidth);

            // 绘制左半圆（始终显示）
            float leftCircleWidth = Math.min(effectiveWidth, barRadius);
            if (leftCircleWidth > 0) {
                // 左半圆剪裁
                GlHelper.enableScissor(
                        barBegin,
                        40,
                        leftCircleWidth,
                        barHeight
                );
                // 绘制完整左半圆
                GlHelper.blitHalfCircle(
                        barBegin,
                        40,
                        barRadius * 2,
                        barHeight,
                        barRadius,
                        GlHelper.GRADIENT_COLORS[0],
                        true // 左侧
                );
                GlHelper.disableScissor();
            }

            // 中间矩形部分
            if (effectiveWidth > barRadius) {
                float rectWidth = effectiveWidth - barRadius;
                GlHelper.blit(
                        barBegin + barRadius,
                        40,
                        rectWidth,
                        barHeight,
                        GlHelper.GRADIENT_COLORS[0]
                );
            }

            // 右半圆（仅当进度满时显示）
            if (effectiveWidth >= usableBarWidth) {
                GlHelper.blitHalfCircle(
                        barBegin + usableBarWidth - barRadius * 2,
                        40,
                        barRadius * 2,
                        barHeight,
                        barRadius,
                        GlHelper.GRADIENT_COLORS[0],
                        false // 右侧
                );
            }
        }


        // 绘制进度文本和其他内容
        String progressText = String.format("%d%%", Math.round(displayProgress * 100));
        float textX = PROGRESS_FORM_WIDTH / 2 - GlHelper.getStringWidth(progressText, 20) / 2;
        GlHelper.drawString(textX, 48, 100, 30, 20, progressText, 0xffffffff, false, true);

        // 绘制主要信息
        GlHelper.drawString(30, 100, PROGRESS_FORM_WIDTH - 60, 40, 18,
                primaryInfo, 0xff2c3e50, false, false);

        // 绘制次要信息
        if (!secondaryProgress.isEmpty()) {
            GlHelper.blitRounded(20, 140, PROGRESS_FORM_WIDTH - 40, 50, 4, 0x11000000);
            GlHelper.drawString(30, 150, PROGRESS_FORM_WIDTH - 60, 30, 16,
                    secondaryProgress, 0xff34495e, false, true);
        }

        // 绘制辅助信息
        boolean monospace = !auxiliaryInfo.isEmpty() && auxiliaryInfo.charAt(0) == ':';
        GlHelper.drawString(30, PROGRESS_FORM_HEIGHT - 60, PROGRESS_FORM_WIDTH - 60, 30, 16,
                monospace ? auxiliaryInfo.substring(1) : auxiliaryInfo, 0xff7f8c8d, monospace, false);

        // 绘制提示文本
        String escBtnHint = ResourcePackUpdater.CONFIG.sourceList.value.size() > 1 ?
                "按下 ESC 取消或使用其他源" : "按下 ESC 取消";
        GlHelper.drawString(30, PROGRESS_FORM_HEIGHT - 30, PROGRESS_FORM_WIDTH - 60, 20, 14,
                escBtnHint, 0xff95a5a6, false, true);

        GlHelper.end();
    }

    @Override
    public boolean shouldStopPausing() {
        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_ESCAPE)) {
            throw new GlHelper.MinecraftStoppingException();
        }
        return true;
    }

    @Override
    public void reset() {
        primaryInfo = "";
        auxiliaryInfo = "";
        primaryProgress = 0;
        displayProgress = 0;
        secondaryProgress = "";
        lastUpdateTime = System.currentTimeMillis();
    }

    @Override
    public void printLog(String line) throws GlHelper.MinecraftStoppingException {
        primaryInfo = line;
    }

    @Override
    public void amendLastLog(String postfix) throws GlHelper.MinecraftStoppingException {
    }

    @Override
    public void setProgress(float primary, float secondary) throws GlHelper.MinecraftStoppingException {
        this.primaryProgress = primary;
    }

    @Override
    public void setInfo(String secondary, String textValue) throws GlHelper.MinecraftStoppingException {
        this.secondaryProgress = secondary;
        this.auxiliaryInfo = textValue;
    }

    @Override
    public void setException(Exception exception) throws GlHelper.MinecraftStoppingException {
    }
}