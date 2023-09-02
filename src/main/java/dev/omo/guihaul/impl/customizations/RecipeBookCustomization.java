package dev.omo.guihaul.impl.customizations;

import com.google.gson.JsonElement;
import net.minecraft.util.Identifier;

public class RecipeBookCustomization extends AbstractTextureCustomization {
    private static final Identifier RECIPE_BUTTON_TEXTURE = new Identifier("textures/gui/recipe_button.png");

    public RecipeBookCustomization(JsonElement json) {
        super(json, 20, 18, 256, 256, 0, 0, RECIPE_BUTTON_TEXTURE);
        if (!json.isJsonObject()) {
            throw new RuntimeException("Recipe Book customizations must be an object!");
        }
    }
}
