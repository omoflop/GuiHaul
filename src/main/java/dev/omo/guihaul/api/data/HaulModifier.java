package dev.omo.guihaul.api.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.omo.guihaul.api.HaulApi;
import dev.omo.guihaul.api.PropertyHolder;
import dev.omo.guihaul.api.PropertySupplier;
import dev.omo.guihaul.util.JsonUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;

public abstract class HaulModifier<T extends ScreenIndexer> implements PropertySupplier {
    protected abstract void modifyScreen(PropertyHolder holder, T screen);

    @Deprecated
    public void modifyScreenInternal(PropertyHolder holder, ScreenIndexer screenIndexer) {
        modifyScreen(holder, (T) screenIndexer);
    }

    public static ObjectWithProperties<HaulModifier<?>> fromJson(JsonElement element) {
        if (!element.isJsonObject()) throw new RuntimeException("Modifiers must be objects");
        JsonObject obj = element.getAsJsonObject();
        Identifier type = JsonUtils.requireIdentifier(obj, "type");
        HaulModifier<?> m = HaulApi.getModifier(type);
        if (m == null) throw new RuntimeException("Missing modifier type: " + type);

        PropertyHolder holder = HaulApi.createPropertyHolder(m);
        holder.read(JsonUtils.requireObject(obj, "args"));
        return new ObjectWithProperties<>(m, holder);
    }
}
