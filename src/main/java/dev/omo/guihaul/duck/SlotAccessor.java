package dev.omo.guihaul.duck;

import dev.omo.guihaul.impl.customizations.SlotCustomization;
import net.minecraft.screen.slot.Slot;

public interface SlotAccessor {
    void guihaul$setX(int x);
    void guihaul$setY(int y);
    void guihaul$setCustomization(SlotCustomization customization);
    SlotCustomization guihaul$getCustomization();

    void guihaul$reset();

    static SlotAccessor get(Slot obj) {
        return (SlotAccessor) obj;
    }
    static SlotCustomization getCustomization(Slot obj) {
        return get(obj).guihaul$getCustomization();
    }
}
