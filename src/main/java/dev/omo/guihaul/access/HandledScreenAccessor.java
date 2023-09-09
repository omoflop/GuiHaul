package dev.omo.guihaul.access;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;

public interface HandledScreenAccessor {
    PlayerInventory guihaul$getInventory();

    static HandledScreenAccessor get(HandledScreen<?> screen) { return ((HandledScreenAccessor) screen); }
}
