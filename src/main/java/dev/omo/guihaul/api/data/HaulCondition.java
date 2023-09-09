package dev.omo.guihaul.api.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.omo.guihaul.access.HandledScreenAccessor;
import dev.omo.guihaul.api.HaulApi;
import dev.omo.guihaul.api.PropertyHolder;
import dev.omo.guihaul.api.PropertySupplier;
import dev.omo.guihaul.util.JsonUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
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

    @Override
    public String toString() {
        return '\'' + HaulApi.getConditionId(this).toString() + '\'';
    }

    public record Context(
            String className,
            @Nullable String containerName,
            @Nullable BlockState state,
            @Nullable ClientWorld world,
            @Nullable ClientPlayerEntity player,
            @Nullable PlayerInventory playerInventory,
            Screen screen,
            @Nullable ScreenHandler screenHandler
            ) {

        public static Context fromScreen(Screen screen) {
            String containerName = null;
            BlockState state = null;
            ClientWorld world = null;
            ClientPlayerEntity player = null;
            PlayerInventory playerInventory = null;
            ScreenHandler screenHandler = null;

            if (screen instanceof HandledScreen<?> handledScreen) {
                containerName = handledScreen.getTitle().getString();
                playerInventory = HandledScreenAccessor.get(handledScreen).guihaul$getInventory();
                if (playerInventory.player instanceof ClientPlayerEntity clientPlayerEntity) {
                    player = clientPlayerEntity;
                    world = clientPlayerEntity.clientWorld;
                }
                screenHandler = handledScreen.getScreenHandler();
            }

            return new Context(screen.getClass().getName(), containerName, state, world, player, playerInventory, screen, screenHandler);
        }
    }
}
