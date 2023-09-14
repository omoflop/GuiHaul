package dev.omo.guihaul.api;

import com.google.gson.JsonElement;
import dev.omo.guihaul.util.IntegerRange;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.function.Function;

public class Property<T> {
    public final String name;
    public final Class<T> valueClass;

    private Property(String name, Class<T> valueClass) {
        this.name = name;
        this.valueClass = valueClass;
    }

    @Override
    public String toString() {
        return name;
    }

    public T readValue(JsonElement json) {
        if (!parsers.containsKey(valueClass))
            throw new RuntimeException("Tried to read property value with no parser for the assigned class! Class: " + valueClass.getName());
        return (T) parsers.get(valueClass).apply(json);
    }

    public static <T>Property<T> of(String name, Class<T> clazz) {
        return new Property<>(name, clazz);
    }

    private static final HashMap<Class<?>, Function<JsonElement, ?>> parsers = new HashMap<>();

    static <T>void registerClass(Class<T> clazz, Function<JsonElement, T> parser) {
        if (parsers.containsKey(clazz))
            throw new RuntimeException("Tried to register a property class parser for an existing class! Class: " + clazz.getName());
        parsers.put(clazz, parser);
    }

    static {
        registerClass(int.class, JsonElement::getAsInt);
        registerClass(boolean.class, JsonElement::getAsBoolean);
        registerClass(byte.class, JsonElement::getAsByte);
        registerClass(double.class, JsonElement::getAsDouble);
        registerClass(float.class, JsonElement::getAsFloat);
        registerClass(Integer.class, JsonElement::getAsInt);
        registerClass(Boolean.class, JsonElement::getAsBoolean);
        registerClass(Byte.class, JsonElement::getAsByte);
        registerClass(Double.class, JsonElement::getAsDouble);
        registerClass(Float.class, JsonElement::getAsFloat);
        registerClass(String.class, JsonElement::getAsString);
    }
}
