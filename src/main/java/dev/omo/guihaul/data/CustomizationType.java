package dev.omo.guihaul.data;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

public class CustomizationType implements Comparable<CustomizationType> {
    private final String name;
    private final Function<JsonElement, Object> parser;
    private final Class<?> clazz;

    public CustomizationType(String name, Class<?> clazz, Function<JsonElement, Object> parser) {
        this.name = name;
        this.parser = parser;
        this.clazz = clazz;
    }

    public Object parse(JsonElement element) { return parser.apply(element); }
    public String getName() { return name; }
    public Class<?> getResultClass() { return clazz; }

    @Override
    public String toString() {
        return "CustomizationType{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomizationType that = (CustomizationType) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(@NotNull CustomizationType other) {
        return name.compareTo(other.name);
    }
}
