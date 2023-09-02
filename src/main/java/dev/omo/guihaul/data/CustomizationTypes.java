package dev.omo.guihaul.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;

public final class CustomizationTypes {
    private static final HashMap<String, CustomizationType> customizationTypes = new HashMap<>();

    public static CustomizationType register(String fieldName, Class<?> clazz, Function<JsonElement, Object> loader) {
        CustomizationType type = new CustomizationType(fieldName, clazz, loader);
        customizationTypes.put(fieldName, type);
        return type;
    }

    public static void tryLoad(String key, JsonObject obj, CustomizationHolder gui) {
        if (!customizationTypes.containsKey(key)) return;
        CustomizationType cType = customizationTypes.get(key);
        gui.putCustomization(cType, cType.parse(obj.get(key)));
    }

    public static Collection<CustomizationType> all() {
        return customizationTypes.values();
    }
}
