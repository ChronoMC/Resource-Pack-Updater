package cn.zbx1425.resourcepackupdater.gui;

import net.minecraft.network.chat.MutableComponent;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface Text {

	static MutableComponent translatable(String text, Object... objects) {
		return Component.translatable(text, objects);
	}

	static MutableComponent literal(String text) {
		return Component.literal(text);
	}
}
