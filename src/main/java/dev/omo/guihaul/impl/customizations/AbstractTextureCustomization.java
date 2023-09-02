package dev.omo.guihaul.impl.customizations;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.omo.guihaul.docs.WikiFieldDesc;
import dev.omo.guihaul.util.JsonUtils;
import net.minecraft.util.Identifier;

public abstract class AbstractTextureCustomization extends AbstractPositionedCustomization {
    @WikiFieldDesc(optional = true)
    public final int width;

    @WikiFieldDesc(optional = true)
    public final int height;

    @WikiFieldDesc(optional = true)
    public final int textureWidth;

    @WikiFieldDesc(optional = true)
    public final int textureHeight;

    @WikiFieldDesc(optional = true)
    public final int u;

    @WikiFieldDesc(optional = true)
    public final int v;

    @WikiFieldDesc(optional = true)
    public final Identifier texture;

    @WikiFieldDesc(optional = true)
    public final boolean enabled;

    protected AbstractTextureCustomization(JsonElement json, int width, int height, int texWidth, int texHeight, int u, int v, Identifier texture) {
        super(json);
        if (json.isJsonObject()) {
            JsonObject obj = json.getAsJsonObject();
            this.width = JsonUtils.optionalInt(obj, "width", width);
            this.height = JsonUtils.optionalInt(obj, "height", height);
            this.textureWidth = JsonUtils.optionalInt(obj, "textureWidth", texWidth);
            this.textureHeight = JsonUtils.optionalInt(obj, "textureHeight", texHeight);
            this.u = JsonUtils.optionalInt(obj, "u", u);
            this.v = JsonUtils.optionalInt(obj, "v", v);
            this.texture = JsonUtils.optionalIdentifier(obj, "texture", texture);
            this.enabled = JsonUtils.optionalBoolean(obj, "enabled", true);
        } else {
            this.width = 0;
            this.height = 0;
            this.textureWidth = 0;
            this.textureHeight = 0;
            this.u = 0;
            this.v = 0;
            this.texture = null;
            this.enabled = false;
        }
    }
}
