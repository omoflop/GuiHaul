package dev.omo.guihaul.data;

import dev.omo.guihaul.impl.VanillaGuiCustomizations;
import dev.omo.guihaul.impl.customizations.SlotCustomization;
import net.minecraft.screen.slot.Slot;

import java.util.HashMap;

public class CustomizationHolder {
    private final HashMap<CustomizationType, Object> all = new HashMap<>();

    public SlotCustomization getForSlot(Slot slot) {
        HashMap<Integer, SlotCustomization> slots = get(VanillaGuiCustomizations.slots);
        if (slots == null) return null;

        return slots.getOrDefault(slot.id, null);
    }

    public <T>T get(CustomizationType cType) {
        if (!all.containsKey(cType)) return null;
        return (T) all.get(cType);
    }

    public void putCustomization(CustomizationType name, Object value) {
        all.put(name, value);
    }

    public CustomizationHolder() { }
}
