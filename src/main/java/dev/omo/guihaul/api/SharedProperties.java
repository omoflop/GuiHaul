package dev.omo.guihaul.api;

import dev.omo.guihaul.util.IntegerRange;
import net.minecraft.util.Identifier;

public final class SharedProperties {
    public static final Property<Integer> X = Property.of("x", int.class);
    public static final Property<Integer> Y = Property.of("y", int.class);
    public static final Property<Float> XF = Property.of("x", float.class);
    public static final Property<Float> YF = Property.of("y", float.class);
    public static final Property<Boolean> ABSOLUTE = Property.of("absolute", boolean.class);
    public static final Property<Boolean> VISIBLE = Property.of("visible", boolean.class);
    public static final Property<Identifier> TEXTURE = Property.of("texture", Identifier.class);
    public static final Property<Integer> WIDTH = Property.of("width", int.class);
    public static final Property<Integer> HEIGHT = Property.of("height", int.class);
    public static final Property<Integer> U = Property.of("u", int.class);
    public static final Property<Integer> V = Property.of("v", int.class);
    public static final Property<IntegerRange> RANGE = Property.of("range", IntegerRange.class);
    public static final Property<Float> SCALE_X = Property.of("scale_x", float.class);
    public static final Property<Float> SCALE_Y = Property.of("scale_y", float.class);
    public static final Property<Integer> SIZE = Property.of("size", int.class);

}
