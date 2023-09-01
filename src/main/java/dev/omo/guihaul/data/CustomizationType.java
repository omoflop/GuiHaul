package dev.omo.guihaul.data;

import com.google.gson.JsonElement;

import java.util.Objects;
import java.util.function.Function;

public class CustomizationType {
    private final String name;
    private final Function<JsonElement, Object> parser;
    public CustomizationType(String name, Function<JsonElement, Object> parser) {
        this.name = name;
        this.parser = parser;
    }

    public Object parse(JsonElement element) { return parser.apply(element); }
    public String getName() { return name; }

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
}
