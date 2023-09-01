package dev.omo.guihaul.data;

import dev.omo.guihaul.api.HaulableGuis;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public final class AllCustomizations {
    private static final HashMap<Class<?>, CustomizationHolder> customizations = new HashMap<>();

    public static @Nullable CustomizationHolder get(Class<?> clazz) {
        return customizations.getOrDefault(clazz, null);
    }

    public static CustomizationHolder getOrCreate(Identifier id) {
        Class<?> handlerClass = HaulableGuis.getClassFromIdentifier(id);
        if (handlerClass == null) throw new RuntimeException("No screen modifier exists for id: " + id);
        CustomizationHolder customization = customizations.getOrDefault(handlerClass, null);
        if (customization == null) {
            customization = new CustomizationHolder();
            customizations.put(handlerClass, customization);
        }
        return customization;
    }

    public static void clearData() {
        customizations.clear();
    }
}
