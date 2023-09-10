package dev.omo.guihaul.api;

import net.minecraft.util.Identifier;

public final class SharedProperties {
    public static final Property<Integer> X = Property.of("x", int.class);
    public static final Property<Integer> Y = Property.of("y", int.class);
    public static final Property<Boolean> ABSOLUTE = Property.of("absolute", boolean.class);
    public static final Property<Boolean> VISIBLE = Property.of("visible", boolean.class);
    public static final Property<Identifier> TEXTURE = Property.of("texture", Identifier.class);

    public static final Property<Integer> WIDTH = Property.of("width", int.class);
    public static final Property<Integer> HEIGHT = Property.of("height", int.class);
    public static final Property<Integer> U = Property.of("u", int.class);
    public static final Property<Integer> V = Property.of("v", int.class);
}
