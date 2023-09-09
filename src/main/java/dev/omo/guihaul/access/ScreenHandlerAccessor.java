package dev.omo.guihaul.access;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

public interface ScreenHandlerAccessor {
    DefaultedList<Slot> guihaul$getSlots();
    ScreenHandlerType<?> guihaul$getType();
    void guihaul$setType(ScreenHandlerType<?> type);

    static ScreenHandlerAccessor get(ScreenHandler obj) {
        return (ScreenHandlerAccessor) obj;
    }
}
