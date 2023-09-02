package dev.omo.guihaul.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.omo.guihaul.data.CustomizationType;
import dev.omo.guihaul.impl.customizations.*;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static dev.omo.guihaul.data.CustomizationTypes.register;

public final class VanillaGuiModifiers {
    public static final Identifier INVENTORY_BACKGROUND_TEXTURE = new Identifier("textures/gui/container/inventory.png");

    public static final Predicate<String> intPattern = Pattern.compile("-?[0-9]+").asMatchPredicate();
    public static CustomizationType slots;
    public static CustomizationType paperDoll;
    public static CustomizationType recipeBook;
    public static CustomizationType guiTexture;

    public static void load() {
        slots = register("slots", SlotsCustomization.class, SlotsCustomization::new);

        paperDoll = register("paper_doll", PaperDollCustomization.class, PaperDollCustomization::new);

        recipeBook = register("recipe_book", RecipeBookCustomization.class, RecipeBookCustomization::new);

        guiTexture = register("gui_texture", GuiTextureCustomization.class, data -> new GuiTextureCustomization(data, 176, 166, 256, 256, 0, 0, INVENTORY_BACKGROUND_TEXTURE));
    }

}
