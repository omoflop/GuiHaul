package dev.omo.guihaul.impl.customizations;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.omo.guihaul.util.JsonUtils;

public abstract class AbstractPositionedCustomization {
    public final int x;
    public final int y;
    public final boolean absolute;

    public AbstractPositionedCustomization(JsonElement json) {
        if (json.isJsonObject()) {
            JsonObject obj = json.getAsJsonObject();
            x = JsonUtils.optionalInt(obj, "x", 0);
            y = JsonUtils.optionalInt(obj, "y", 0);
            absolute = JsonUtils.optionalBoolean(obj, "absolute", false);
        } else {
            x = 0;
            y = 0;
            absolute = false;
        }
    }
}
