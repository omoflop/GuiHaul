package dev.omo.guihaul.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.omo.guihaul.GuiHaulMod;

import java.util.function.Function;

public final class JsonUtils {
    public static boolean validate(JsonObject object, String fieldName, Function<JsonElement, Boolean> validator) {
        return (object.has(fieldName) && validator.apply(object.get(fieldName)));
    }

    public static boolean validate(JsonObject object, String fieldName, Function<JsonElement, Boolean> validator, String warningMessage, Object... params) {
        if (!validate(object, fieldName, validator)) {
            GuiHaulMod.LOGGER.warn(warningMessage, params);
            return false;
        }
        return true;
    }

    public static int getInt(JsonObject object, String fieldName) {
        try {
            if (validate(object, fieldName, JsonElement::isJsonPrimitive)) {
                return object.get(fieldName).getAsInt();
            }
        } catch (Exception ignored) {

        }
        throw new RuntimeException(String.format("Field %s must be an integer!", fieldName));
    }

    public static String getString(JsonObject object, String fieldName) {
        try {
            if (validate(object, fieldName, JsonElement::isJsonPrimitive)) {
                return object.get(fieldName).getAsString();
            }
        } catch (Exception ignored) {

        }
        throw new RuntimeException(String.format("Field %s must be a string!", fieldName));
    }

    public static boolean getBoolean(JsonObject object, String fieldName) {
        try {
            if (validate(object, fieldName, JsonElement::isJsonPrimitive)) {
                return object.get(fieldName).getAsBoolean();
            }
        } catch (Exception ignored) {

        }
        throw new RuntimeException(String.format("Field %s must be a boolean!", fieldName));
    }

    public static int getIntOrDefault(JsonObject object, String fieldName, int fallback) {
        if (validate(object, fieldName, JsonElement::isJsonPrimitive)) {
            return object.get(fieldName).getAsInt();
        }
        return fallback;
    }

    public static boolean getBooleanOrDefault(JsonObject object, String fieldName, boolean fallback) {
        if (validate(object, fieldName, JsonElement::isJsonPrimitive)) {
            return object.get(fieldName).getAsBoolean();
        }
        return fallback;
    }

    public static JsonElement getFromArray(JsonArray array, int index) {
        if (array.size() <= index) throw new RuntimeException("Array must contain at least " + array.size() + " entries!");
        return array.get(index);
    }

    public static int getIntFromArray(JsonArray array, int index) {
        return (int) getValueFromArray(array, index, "int", JsonElement::getAsInt);

    }

    public static boolean getBooleanFromArray(JsonArray array, int index) {
        return (boolean) getValueFromArray(array, index, "boolean", JsonElement::getAsBoolean);
    }

    private static Object getValueFromArray(JsonArray array, int index, String errDesc, Function<JsonElement, Object> getter) {
        JsonElement element = getFromArray(array, index);
        try {
            return getter.apply(element);
        } catch (Exception ignored) { }

        throw new RuntimeException(String.format("Element at array index %s must be a %s!", index, errDesc));
    }
}
