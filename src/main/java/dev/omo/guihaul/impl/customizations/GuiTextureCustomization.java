package dev.omo.guihaul.impl.customizations;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.omo.guihaul.util.GuiAlignment;
import dev.omo.guihaul.util.JsonUtils;
import net.minecraft.util.Identifier;

public class GuiTextureCustomization extends AbstractTextureCustomization {
    public final GuiAlignment alignment;
    public final boolean shiftForRecipeBook;

    public GuiTextureCustomization(JsonElement json, int width, int height, int texWidth, int texHeight, int u, int v, Identifier texture) {
        super(json, width, height, texWidth, texHeight, u, v, texture);
        if (json.isJsonObject()) {
            JsonObject obj = json.getAsJsonObject();
            alignment = JsonUtils.optionalEnum(obj, GuiAlignment.class, "alignment", GuiAlignment.none);
            shiftForRecipeBook = JsonUtils.optionalBoolean(obj, "recipe_book_shift", true);
        } else {
            throw new RuntimeException("Gui texture customizations must be an object!");
        }
    }
}
