package dev.omo.guihaul.duck;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

public interface ScreenHandlerAccessor {
    DefaultedList<Slot> guihaul$getSlots();

    static ScreenHandlerAccessor get(ScreenHandler obj) {
        return (ScreenHandlerAccessor) obj;
    }
}
