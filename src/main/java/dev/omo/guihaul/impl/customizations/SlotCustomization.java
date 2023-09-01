package dev.omo.guihaul.impl.customizations;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.omo.guihaul.util.JsonUtils;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class SlotCustomization extends AbstractPositionedCustomization {
    public final boolean enabled;
    public final int size;
    @Nullable public final Identifier backgroundSprite;
    @Nullable public final Identifier highlightSprite;
    public final int highlightColor;

    public SlotCustomization(JsonElement json) {
        super(json);
        if (json.isJsonObject()) {
            JsonObject obj = json.getAsJsonObject();
            enabled = JsonUtils.optionalBoolean(obj, "enabled", true);
            size = JsonUtils.optionalInt(obj, "size", 16);
            backgroundSprite = JsonUtils.optionalIdentifier(obj, "bg_texture", null);
            highlightSprite = JsonUtils.optionalIdentifier(obj, "highlight_texture", null);
            highlightColor = JsonUtils.optionalInt(obj, "highlight_color", -2130706433);
        } else {
            throw new RuntimeException("Slot customizations must be an object!");
        }
    }
}
