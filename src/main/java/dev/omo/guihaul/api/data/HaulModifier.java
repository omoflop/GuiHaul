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
    private final Class<T> indexerClass;
    public HaulModifier(Class<T> clazz) {
        this.indexerClass = clazz;
    }

    protected void modifyScreen(PropertyHolder holder, T screen) { }
    protected void cleanupScreen(PropertyHolder holder, T screen) { }
    protected void modifyScreenInit(PropertyHolder holder, T screen) { }
    @Deprecated
    public void modifyScreenInternal(PropertyHolder holder, Object screenIndexer) {
        if (isApplicableTo(screenIndexer))
            modifyScreen(holder, (T) screenIndexer);
    }
    @Deprecated
    public void cleanupScreenInternal(PropertyHolder holder, Object screenIndexer) {
        if (isApplicableTo(screenIndexer))
            cleanupScreen(holder, (T) screenIndexer);
    }
    @Deprecated
    public void modifyScreenInitInternal(PropertyHolder holder, Object screenIndexer) {
        if (isApplicableTo(screenIndexer))
            modifyScreenInit(holder, (T) screenIndexer);
    }

    public boolean isApplicableTo(Object screen) {
        Class<?> c;
        if (screen instanceof Class<?> clazz) {
            c = clazz;
        } else {
            c = screen.getClass();
        }
        return indexerClass.isAssignableFrom(c);
    }

    @Override
    public String toString() {
        return '\'' + HaulApi.getModifierId(this).toString() + '\'';
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
