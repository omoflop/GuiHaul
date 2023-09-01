package dev.omo.guihaul.duck;

import net.minecraft.client.gui.DrawContext;

public interface DrawContextAccessor {
    void guihaul$setNextSlotSize(int size);

    static void setNextSlotSize(DrawContext ctx, int size) {
        ((DrawContextAccessor) ctx).guihaul$setNextSlotSize(size);
    }
}
