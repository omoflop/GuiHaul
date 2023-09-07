package dev.omo.guihaul.api;

import com.google.gson.JsonObject;
import dev.omo.guihaul.GuiHaulMod;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;

public final class PropertyHolder {
    private final HashMap<Property<?>, Object> values;
    private final HashSet<Property<?>> requiredProperties;
    private final HashSet<Property<?>> allProperties;

    private PropertyHolder(HashSet<Property<?>> requiredProperties, HashMap<Property<?>, Object> propertyDefaults) {
        this.values = propertyDefaults;
        this.requiredProperties = requiredProperties;
        this.allProperties = new HashSet<>();
        allProperties.addAll(requiredProperties);
        allProperties.addAll(propertyDefaults.keySet());
    }

    private PropertyHolder(HashMap<Property<?>, Object> values, HashSet<Property<?>> requiredProperties, HashSet<Property<?>> allProperties) {
        this.values = values;
        this.requiredProperties = requiredProperties;
        this.allProperties = allProperties;
    }

    public PropertyHolder copy() {
        return new PropertyHolder((HashMap<Property<?>, Object>) values.clone(), (HashSet<Property<?>>) requiredProperties.clone(), (HashSet<Property<?>>) allProperties.clone());
    }

    public void read(JsonObject json) {
        allProperties.forEach(p -> {
            boolean optional = !requiredProperties.contains(p);
            boolean hasValue = json.has(p.name);
            if (!optional && !hasValue) {
                throw new RuntimeException("Required property " + p.name + " does not have a value!");
            } else {
                values.put(p, p.readValue(json.get(p.name)));
            }
        });
    }

    public @NotNull <T>T getProperty(Property<T> property) {
        if (!requiredProperties.contains(property)) {
            GuiHaulMod.LOGGER.error("Property " + property.name + " is required, but no value was found!");
        }
        return (T) values.get(property);
    }

    public static class Builder {
        private final HashMap<Property<?>, Object> propertyDefaults = new HashMap<>();
        private final HashSet<Property<?>> requiredProperties = new HashSet<>();

        public <T>void add(Property<T> property) {
            add(property, null);
        }

        public <T>void add(Property<T> property, T defaultValue) {
            if (propertyDefaults.containsKey(property) || requiredProperties.contains(property))
                throw new RuntimeException("Tried to register a property with an existing name! Name: " + property.name);
            if (defaultValue == null) requiredProperties.add(property);
            else propertyDefaults.put(property, defaultValue);
        }

        public PropertyHolder build() {
            return new PropertyHolder(requiredProperties, propertyDefaults);
        }
    }

}
