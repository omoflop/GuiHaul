package dev.omo.guihaul.api;

public final class SharedProperties {
    public static final Property<Integer> X = Property.of("x", int.class);
    public static final Property<Integer> Y = Property.of("y", int.class);
    public static final Property<Boolean> ABSOLUTE = Property.of("absolute", boolean.class);

}
