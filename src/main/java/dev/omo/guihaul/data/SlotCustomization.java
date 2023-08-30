package dev.omo.guihaul.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.omo.guihaul.util.JsonUtils;

public class SlotCustomization {
    public final int x;
    public final int y;
    public final boolean relative;

    public SlotCustomization(JsonElement json) {
        if (json.isJsonObject()) {
            JsonObject obj = json.getAsJsonObject();
            x = JsonUtils.getInt(obj, "x");
            y = JsonUtils.getInt(obj, "y");
            relative = JsonUtils.getBooleanOrDefault(obj, "relative", false);
        } else if (json.isJsonArray()) {
            JsonArray arr = json.getAsJsonArray();
            x = JsonUtils.getIntFromArray(arr, 0);
            y = JsonUtils.getIntFromArray(arr, 1);
            relative = JsonUtils.getBooleanFromArray(arr, 2);
        } else {
            throw new RuntimeException("Slot customizations must be an object or an array!");
        }
    }
}
