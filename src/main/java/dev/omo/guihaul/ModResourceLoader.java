package dev.omo.guihaul;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import dev.omo.guihaul.GuiHaulMod;
import dev.omo.guihaul.api.HaulApi;
import dev.omo.guihaul.api.data.ScreenCustomization;
import dev.omo.guihaul.api.data.ScreenCustomizationHolder;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.Map;

public class ModResourceLoader implements SimpleSynchronousResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return GuiHaulMod.getId("resource_loader");
    }

    @Override
    public void reload(ResourceManager manager) {
        try {
            Map<Identifier, Resource> resources = manager.findResources("guihaul", id -> id.getPath().endsWith(".json"));
            for (Identifier id : resources.keySet()) {
                try {
                    JsonElement e = JsonParser.parseReader(new JsonReader(resources.get(id).getReader()));
                    Identifier actualId = new Identifier(id.getNamespace(), id.getPath().substring(8, id.getPath().length()-5));
                    HaulApi.addCustomization(actualId, ScreenCustomizationHolder.fromJson(e));
                } catch (Exception any) {
                    GuiHaulMod.LOGGER.error("Encountered an error when reading file " + id, any);
                }
            }
        } catch (Exception any) {
            GuiHaulMod.LOGGER.error("Failed to load gui customizations!", any);
        }

        if (GuiHaulMod.DEBUG) {
            HaulApi.printResourcePackDebug();
        }

    }
}
