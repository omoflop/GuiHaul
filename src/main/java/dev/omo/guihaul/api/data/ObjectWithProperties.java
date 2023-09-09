package dev.omo.guihaul.api.data;

import dev.omo.guihaul.api.PropertyHolder;

public class ObjectWithProperties<T> {
    public final T value;
    public final PropertyHolder holder;

    public ObjectWithProperties(T object, PropertyHolder properties) {
        this.value = object;
        this.holder = properties;
    }

    @Override
    public String toString() {
        return "{value=" + value + ", properties=" + holder + '}';
    }
}
