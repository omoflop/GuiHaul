package dev.omo.guihaul.impl.customizations;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.omo.guihaul.api.GuiCustomization;
import dev.omo.guihaul.docs.WikiFieldDesc;

import java.util.HashMap;

import static dev.omo.guihaul.impl.VanillaGuiModifiers.intPattern;

public class SlotsCustomization extends GuiCustomization {

    @WikiFieldDesc(optional = false, bodyIsMap = true, mapValueClass = SlotCustomization.class, mapKeyExample = "any integer (1, 2, 3, etc...) also ranges (2-50, 4-16 etc..)")
    public HashMap<Integer, SlotCustomization> slots = new HashMap<>();

    public SlotsCustomization(JsonElement a) {
        super(a);
        JsonObject obj = a.getAsJsonObject();

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
    }
}
