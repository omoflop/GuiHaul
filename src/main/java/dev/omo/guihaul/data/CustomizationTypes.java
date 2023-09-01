package dev.omo.guihaul.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.function.Function;

public final class CustomizationTypes {
    private static final HashMap<String, CustomizationType> customizationTypes = new HashMap<>();

    public static CustomizationType register(String fieldName, Function<JsonElement, Object> loader) {
        CustomizationType type = new CustomizationType(fieldName, loader);
        customizationTypes.put(fieldName, type);
        return type;
    }

    public static void tryLoad(String key, JsonObject obj, CustomizationHolder gui) {
        if (!customizationTypes.containsKey(key)) return;
        CustomizationType cType = customizationTypes.get(key);
        gui.putCustomization(cType, cType.parse(obj.get(key)));
    }
}
