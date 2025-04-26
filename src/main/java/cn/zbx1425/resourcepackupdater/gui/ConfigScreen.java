package cn.zbx1425.resourcepackupdater.gui;

import cn.zbx1425.resourcepackupdater.Config;
import cn.zbx1425.resourcepackupdater.ResourcePackUpdater;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

public class ConfigScreen extends Screen {

    private boolean isShowingLog = false;
    private final HashMap<Config.SourceProperty, Button> sourceButtons = new HashMap<>();

    public ConfigScreen() {
        super(Component.translatable("资源包更新器基础设置"));
    }

    @Override
    protected void init() {
        final int PADDING = 10;
        int btnWidthOuter = (width - PADDING * 2) / 2;
        int btnWidthInner = btnWidthOuter - PADDING * 2;

        // 添加按钮
        addRenderableWidget(new ModernButton(PADDING, 40, btnWidthInner, 20,
                Component.translatable("显示程序日志（调试专用）"), (btn) -> isShowingLog = true));
        addRenderableWidget(new ModernButton(PADDING + btnWidthOuter, 40, btnWidthInner, 20,
                Component.translatable("重新下载并更新资源"), (btn) -> {
            assert minecraft != null;
            minecraft.reloadResourcePacks();
        }));
        addRenderableWidget(new ModernButton(PADDING + btnWidthOuter, height - 40, btnWidthInner, 20,
                Component.translatable("返回"), (btn) -> {
            assert minecraft != null;
            minecraft.setScreen(null);
        }));

        // 添加资源包选择按钮
        int btnY = 90;
        for (Config.SourceProperty source : ResourcePackUpdater.CONFIG.sourceList.value) {
            Button btnUseSource = new ModernButton(PADDING, btnY, btnWidthInner, 20,
                    Component.translatable(source.name), (btn) -> {
                ResourcePackUpdater.CONFIG.selectedSource.value = source;
                try {
                    ResourcePackUpdater.CONFIG.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateBtnEnable();
            });
            sourceButtons.put(source, btnUseSource);
            btnY += 30;
            addRenderableWidget(btnUseSource);
        }
        updateBtnEnable();
    }

    private void updateBtnEnable() {
        for (var entry : sourceButtons.entrySet()) {
            entry.getValue().active = !ResourcePackUpdater.CONFIG.selectedSource.value.equals(entry.getKey());
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        // 绘制背景
        guiGraphics.blit(new ResourceLocation("resourcepackupdater", "textures/gui/background.png"),
                0, 0, 0, 0, this.width, this.height, this.width, this.height);

        // 绘制标题
        guiGraphics.drawCenteredString(font, "资源包更新器", this.width / 2, 20, 0xFFFFFF);

        // 绘制分割线
        guiGraphics.fill(this.width / 2 - 100, 35, this.width / 2 + 100, 37, 0xFFFFFFFF);

        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    // 自定义按钮类
    private static class ModernButton extends Button {

        public ModernButton(int x, int y, int width, int height, Component message, OnPress onPress) {
            super(x, y, width, height, message, onPress, Button.DEFAULT_NARRATION);
        }

        @Override
        public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
            int bgColor = this.active ? (this.isHoveredOrFocused() ? 0xff5dade2 : 0xff3498db) : 0xff7f8c8d;
            guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, bgColor);

            int textColor = this.active ? 0xffffffff : 0xffbbbbbb;
            guiGraphics.drawString(Minecraft.getInstance().font, this.getMessage(),
                    this.getX() + this.width / 2 - Minecraft.getInstance().font.width(this.getMessage()) / 2,
                    this.getY() + (this.height - 8) / 2, textColor);
        }
    }
}