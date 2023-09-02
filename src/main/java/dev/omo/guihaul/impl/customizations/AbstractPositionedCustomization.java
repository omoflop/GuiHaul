package dev.omo.guihaul.impl.customizations;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.omo.guihaul.api.GuiCustomization;
import dev.omo.guihaul.docs.WikiFieldDesc;
import dev.omo.guihaul.util.JsonUtils;

public abstract class AbstractPositionedCustomization extends GuiCustomization {
    @WikiFieldDesc(optional = true)
    public final int x;

    @WikiFieldDesc(optional = true)
    public final int y;

    @WikiFieldDesc(optional = true)
    public final boolean absolute;

    public AbstractPositionedCustomization(JsonElement json) {
        super(json);
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

    public int getX(int newX) {
        if (absolute) return newX;
        return this.x + newX;
    }

    public int getY(int newY) {
        if (absolute) return newY;
        return this.y + newY;
    }
}
