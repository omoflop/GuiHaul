package dev.omo.guihaul.api.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.omo.guihaul.api.HaulApi;
import dev.omo.guihaul.api.PropertyHolder;
import dev.omo.guihaul.api.PropertySupplier;
import dev.omo.guihaul.util.JsonUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public abstract class HaulCondition implements PropertySupplier {
    protected void init(PropertyHolder holder) { }
    protected abstract boolean passesCondition(PropertyHolder properties, Context ctx);

    public static ObjectWithProperties<HaulCondition> fromJson(JsonElement element) {
        JsonObject obj = element.getAsJsonObject();
        Identifier type = JsonUtils.requireIdentifier(obj, "type");

        HaulCondition ct = HaulApi.getCondition(type);
        if (ct == null) throw new RuntimeException("Missing condition type: " + type);

        PropertyHolder holder = HaulApi.createPropertyHolder(ct);
        holder.read(JsonUtils.requireObject(obj, "args"));
        return new ObjectWithProperties<>(ct, holder);
    }


    public static class Context {
        public String containerName;
        public @Nullable BlockState block;
        public @Nullable ClientWorld world;
        public @Nullable String screenName;
    }
}
