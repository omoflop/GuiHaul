package dev.omo.guihaul.impl.customizations;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.omo.guihaul.util.JsonUtils;

public class BackgroundTextureCustomization extends AbstractPositionedCustomization {
    public final int width;
    public final int height;
    public final boolean enabled;

    public BackgroundTextureCustomization(JsonElement json) {
        super(json);
        if (json.isJsonObject()) {
            JsonObject obj = json.getAsJsonObject();
            width = JsonUtils.optionalInt(obj, "width", -1);
            height = JsonUtils.optionalInt(obj, "height", -1);
            enabled = JsonUtils.optionalBoolean(obj, "enabled", true);
        } else {
            throw new RuntimeException("Paper Doll customizations must be an object!");
        }
    }
}
