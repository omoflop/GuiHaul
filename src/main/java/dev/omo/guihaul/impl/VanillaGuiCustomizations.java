package dev.omo.guihaul.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.omo.guihaul.data.CustomizationType;
import dev.omo.guihaul.impl.customizations.BackgroundTextureCustomization;
import dev.omo.guihaul.impl.customizations.PaperDollCustomization;
import dev.omo.guihaul.impl.customizations.RecipeBookCustomization;
import dev.omo.guihaul.impl.customizations.SlotCustomization;

import java.util.HashMap;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static dev.omo.guihaul.data.CustomizationTypes.register;

public final class VanillaGuiCustomizations {
    private static final Predicate<String> intPattern = Pattern.compile("-?[0-9]+").asMatchPredicate();
    public static CustomizationType slots;
    public static CustomizationType paperdoll;
    public static CustomizationType recipebook;
    public static CustomizationType backgroundtexture;

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

        paperdoll = register("paperdoll", PaperDollCustomization::new);

        recipebook = register("recipebook", RecipeBookCustomization::new);

        backgroundtexture = register("backgroundtexture", BackgroundTextureCustomization::new);
    }

}
