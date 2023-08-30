package dev.omo.guihaul.loading;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.omo.guihaul.GuiHaulMod;
import dev.omo.guihaul.api.HaulableGuis;
import dev.omo.guihaul.data.GuiCustomization;
import dev.omo.guihaul.data.SlotCustomization;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dev.omo.guihaul.GuiHaulMod.getId;

public class HaulResourceLoader implements SimpleSynchronousResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return getId("resourceloader");
    }

    public static Predicate<String> intPattern = Pattern.compile("-?[0-9]+").asMatchPredicate();

    @Override
    public void reload(ResourceManager manager) {
        HaulableGuis.clearData();
        Map<Identifier, Resource> customizations = manager.findResources("guihaul", id -> id.getPath().endsWith(".json"));
        for (Identifier id : customizations.keySet()) {
            try {
                String path = id.getPath();
                String guiId = id.getNamespace() + ":" + path.substring(8, path.length()-5);

                GuiCustomization gui = HaulableGuis.getOrCreate(guiId);

                JsonObject json = JsonParser.parseReader(customizations.get(id).getReader()).getAsJsonObject();
                if (json.has("slots")) {
                    try {
                        JsonObject slotsObj = json.get("slots").getAsJsonObject();
                        for (String key : slotsObj.keySet()) {
                            if (intPattern.test(key)) {
                                int slotId = Integer.parseInt(key);
                                gui.slotCustomizations.put(slotId, new SlotCustomization(slotsObj.get(key)));
                            } else {
                                int sep = key.indexOf('-');
                                int fromSlot = Integer.parseInt(key.substring(0, sep));
                                int toSlot = Integer.parseInt(key.substring(sep+1));
                                SlotCustomization slotCustomization = new SlotCustomization(slotsObj.get(key));
                                for (int i = fromSlot; i < toSlot; i++) {
                                    gui.slotCustomizations.put(i, slotCustomization);
                                }
                            }

                        }
                    } catch (Exception e) {
                        throw new RuntimeException(String.format("Failed to parse slots in file %s, reason: %s", id, e));
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
