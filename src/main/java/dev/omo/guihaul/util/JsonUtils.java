package dev.omo.guihaul.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public final class JsonUtils {
    private static <T>T nothing(T value) { return value; }
    private static Identifier getAsIdentifier(JsonElement element) { return new Identifier(element.getAsString()); }

    public static int requireInt(JsonObject object, String key) {
        return getValue(object, key, JsonElement::getAsInt, "int");
    }
    public static int optionalInt(JsonObject object, String key, int fallback) {
        return getValueOr(object, key, JsonElement::getAsInt, fallback);
    }

    public static boolean requireBoolean(JsonObject object, String key) {
        return getValue(object, key, JsonElement::getAsBoolean, "boolean");
    }
    public static boolean optionalBoolean(JsonObject object, String key, boolean fallback) {
        return getValueOr(object, key, JsonElement::getAsBoolean, fallback);
    }

    public static String requireString(JsonObject object, String key) {
        return getValue(object, key, JsonElement::getAsString, "string");
    }
    public static String optionalString(JsonObject object, String key, String fallback) {
        return getValueOr(object, key, JsonElement::getAsString, fallback);
    }

    public static Identifier requireIdentifier(JsonObject object, String key) {
        return getValue(object, key, JsonUtils::getAsIdentifier, "identifier");
    }
    public static Identifier optionalIdentifier(JsonObject object, String key, Identifier fallback) {
        return getValueOr(object, key, JsonUtils::getAsIdentifier, fallback);
    }

    public static JsonObject requireObject(JsonObject object, String key) {
        return getValue(object, key, JsonElement::getAsJsonObject, "object");
    }
    public static JsonObject optionalObject(JsonObject object, String key, JsonObject fallback) {
        return getValueOr(object, key, JsonElement::getAsJsonObject, fallback);
    }

    public static JsonElement require(JsonObject object, String key) {
        return getValue(object, key, JsonUtils::nothing, "any");
    }
    public static JsonElement optional(JsonObject object, String key, JsonElement fallback) {
        return getValueOr(object, key, JsonUtils::nothing, fallback);
    }

    private static <T>T getValue(JsonObject object, String key, Function<JsonElement, T> getter, String errDesc) {
        if (!object.has(key)) throw new RuntimeException("Object must contain the key \"" + key + "\"");
        JsonElement element = object.get(key);
        try {
            return getter.apply(element);
        } catch (Exception ignored) { }

        throw new RuntimeException(String.format("Element at object key %s must be a %s!", key, errDesc));
    }

    private static <T>T getValueOr(JsonObject object, String key, Function<JsonElement, T> getter, T fallback) {
        if (!object.has(key)) return fallback;
        JsonElement element = object.get(key);
        try {
            return getter.apply(element);
        } catch (Exception ignored) { }

        return fallback;
    }
}
