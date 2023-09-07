package dev.omo.guihaul.access;

import net.minecraft.screen.slot.Slot;

public interface SlotAccessor {
    void guihaul$setX(int x);
    void guihaul$setY(int y);

    static SlotAccessor get(Slot obj) {
        return (SlotAccessor) obj;
    }
}
