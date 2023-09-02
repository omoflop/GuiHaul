package dev.omo.guihaul.api;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import dev.omo.guihaul.api.impl.BasicGuiModifier;
import dev.omo.guihaul.data.CustomizationType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public final class HaulableGuis {
    private static final BiMap<Identifier, Class<?>> lookup = HashBiMap.create();
    private static final HashMap<Class<?>, GuiModifier<?, ?>> modifierLookups = new HashMap<>();
    private static final HashMultimap<Identifier, CustomizationType> supportedCustomizationTypesLookup = HashMultimap.create();

    public static <T extends Screen>void register(Identifier id, Class<T> screenClass, CustomizationType... supportedCustomizationTypes) {
        register(id, screenClass, new BasicGuiModifier<>(), supportedCustomizationTypes);
    }

    public static <T extends Screen>void register(Identifier id, Class<T> screenClass, GuiModifier<?, ?> modifier, CustomizationType... supportedCustomizationTypes) {
        lookup.put(id, screenClass);
        modifierLookups.put(screenClass, modifier);
        addCustomizations(id, supportedCustomizationTypes);
    }

    public static void addCustomizations(Identifier id, CustomizationType... supportedCustomizationTypes) {
        for (CustomizationType cType : supportedCustomizationTypes) {
            supportedCustomizationTypesLookup.put(id, cType);
        }

    }

    public static @Nullable Identifier getIdentifierFromClass(Class<?> clazz) {
        return lookup.inverse().getOrDefault(clazz, null);
    }

    public static @Nullable GuiModifier<?, ?> getModifier(Class<?> clazz) {
        return modifierLookups.getOrDefault(clazz, null);
    }

    public static @Nullable Class<?> getClassFromIdentifier(Identifier id) {
        return lookup.getOrDefault(id, null);
    }

    public static boolean guiSupportsCustomization(Identifier guiId, String customizationName) {
        if (!supportedCustomizationTypesLookup.containsKey(guiId)) throw new RuntimeException("Gui " + guiId + " doesn't support ANY customization types! Please contact the (addon) mod author!");
        Set<CustomizationType> types = supportedCustomizationTypesLookup.get(guiId);
        for (CustomizationType type : types) {
            if (type.getName().equals(customizationName)) return true;
        }
        return false;
    }

    public static Set<Identifier> getSupportedContainers() {
        return lookup.keySet();
    }

    public static Set<CustomizationType> getSupportedCustomizations(Identifier id) {
        return supportedCustomizationTypesLookup.get(id);
    }
}
