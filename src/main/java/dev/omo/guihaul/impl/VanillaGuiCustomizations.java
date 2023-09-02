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

public final class VanillaGuiCustomizations {
    public static final Identifier INVENTORY_BACKGROUND_TEXTURE = new Identifier("textures/gui/container/inventory.png");

    private static final Predicate<String> intPattern = Pattern.compile("-?[0-9]+").asMatchPredicate();
    public static CustomizationType slots;
    public static CustomizationType paperDoll;
    public static CustomizationType recipeBook;
    public static CustomizationType guiTexture;

    public static void load() {
        slots = register("slots", a -> {
            JsonObject obj = a.getAsJsonObject();
            HashMap<Integer, SlotCustomization> slots = new HashMap<>();

            // Iterate through each key and parse slots accordingly
            for (String key : obj.keySet()) {
                JsonElement value = obj.get(key);

                // Is a single slot
                if (intPattern.test(key)) {
                    slots.put(Integer.parseInt(key), new SlotCustomization(value));
                }
                // Is a range of slots
                else {
                    int sep = key.indexOf('-');
                    int fromSlot = Integer.parseInt(key.substring(0, sep));
                    int toSlot = Integer.parseInt(key.substring(sep+1));
                    SlotCustomization slotCustomization = new SlotCustomization(value);
                    for (int i = fromSlot; i <= toSlot; i++) {
                        slots.put(i, slotCustomization);
                    }
                }
            }

            return slots;
        });

        paperDoll = register("paper_doll", PaperDollCustomization::new);

        recipeBook = register("recipe_book", RecipeBookCustomization::new);

        guiTexture = register("gui_texture", data -> new GuiTextureCustomization(data, 176, 166, 256, 256, 0, 0, INVENTORY_BACKGROUND_TEXTURE));
    }

}
