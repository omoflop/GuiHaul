package dev.omo.guihaul.api;

import dev.omo.guihaul.GuiHaulMod;
import dev.omo.guihaul.data.GuiCustomization;
import dev.omo.guihaul.data.SlotCustomization;
import dev.omo.guihaul.duck.SlotAccessor;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public final class HaulableGuis {
    private static final HashMap<String, GuiCustomization> customizations = new HashMap<>();

    public static @Nullable GuiCustomization get(String id) {
        return customizations.getOrDefault(id, null);
    }

    public static GuiCustomization getOrCreate(String id) {
        GuiCustomization customization = customizations.getOrDefault(id, null);
        if (customization == null) {
            customization = new GuiCustomization();
            customizations.put(id, customization);
        }
        return customization;
    }

    public static void applySlots(String id, DefaultedList<Slot> slots) {
        GuiCustomization gui = get(id);
        if (gui == null) return;
        for (Slot slot : slots) {
            if (gui.slotCustomizations.containsKey(slot.id)) {
                SlotCustomization cus = gui.slotCustomizations.get(slot.id);
                SlotAccessor slotAccess = SlotAccessor.get(slot);
                if (cus.relative) {
                    slotAccess.guihaul$setX(slot.x + cus.x);
                    slotAccess.guihaul$setY(slot.y + cus.y);
                } else {
                    slotAccess.guihaul$setX(cus.x);
                    slotAccess.guihaul$setY(cus.y);
                }

            }
        }
    }

    public static void clearData() {
        customizations.clear();
    }
}
