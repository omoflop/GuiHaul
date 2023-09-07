package dev.omo.guihaul.entry;

import dev.omo.guihaul.api.HaulApi;
import net.minecraft.util.Identifier;

public interface HaulApiEntrypoint {
    void onInitializeApi(HaulApi.Builder builder);
    Identifier getApiProviderName();
}
