package dev.omo.guihaul.ref;

import net.minecraft.util.Identifier;

public final class BuiltinElementTypes {
    public static final Identifier SLOT = id("slot");
    public static final Identifier SLOT_RANGE = id("slot_range");
    public static final Identifier BUTTON = id("button");
    public static final Identifier RECIPE_BOOK = id("recipe_book");
    public static final Identifier RECIPE_BOOK_TABS = id("recipe_book/tabs");

    private static Identifier id(String path) {
        return new Identifier("builtin", path);
    }

}
