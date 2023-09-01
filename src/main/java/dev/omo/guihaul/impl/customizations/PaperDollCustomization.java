package dev.omo.guihaul.impl.customizations;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.omo.guihaul.util.JsonUtils;

public class PaperDollCustomization extends AbstractPositionedCustomization {
    public final int size;

    public PaperDollCustomization(JsonElement json) {
        super(json);
        if (json.isJsonObject()) {
            JsonObject obj = json.getAsJsonObject();
            size = JsonUtils.optionalInt(obj, "size", 30);
        } else {
            throw new RuntimeException("Paper Doll customizations must be an object!");
        }
    }
}
