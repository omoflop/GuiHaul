package dev.omo.guihaul.ref;

import net.minecraft.util.Identifier;

public class BuiltInConditionTypes {
    public static final Identifier MOD_LOADED = id("mod_loaded");
    public static final Identifier CONTAINER_NAME = id("container_name");
    public static final Identifier CONTAINER_BLOCK = id("container_block");
    public static final Identifier IS_CONTAINER = id("is_container");
    public static final Identifier SCREEN_NAME = id("screen_name");

    private static Identifier id(String path) {
        return new Identifier("builtin", path);
    }

}
