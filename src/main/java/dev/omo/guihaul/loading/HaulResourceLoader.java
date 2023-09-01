package dev.omo.guihaul.loading;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.omo.guihaul.api.HaulableGuis;
import dev.omo.guihaul.data.AllCustomizations;
import dev.omo.guihaul.data.CustomizationHolder;
import dev.omo.guihaul.data.CustomizationTypes;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static dev.omo.guihaul.GuiHaulMod.getId;

public class HaulResourceLoader implements SimpleSynchronousResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return getId("resourceloader");
    }


    @Override
    public void reload(ResourceManager manager) {
        AllCustomizations.clearData();
        Map<Identifier, Resource> customizations = manager.findResources("guihaul", id -> id.getPath().endsWith(".json"));
        for (Identifier id : customizations.keySet()) {
            try {
                String path = id.getPath();

                Identifier guiId = new Identifier(id.getNamespace() + ":" + path.substring(8, path.length()-5));
                CustomizationHolder gui = AllCustomizations.getOrCreate(guiId);

                JsonObject json = JsonParser.parseReader(customizations.get(id).getReader()).getAsJsonObject();
                for (String key : json.keySet()) {
                    if (HaulableGuis.guiSupportsCustomization(guiId, key)) {
                        CustomizationTypes.tryLoad(key, json, gui);
                    } else {
                        throw new RuntimeException("Gui " + guiId + " doesn't support the customization type: " + key);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
