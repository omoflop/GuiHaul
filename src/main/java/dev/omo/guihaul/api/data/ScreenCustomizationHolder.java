package dev.omo.guihaul.api.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class ScreenCustomizationHolder {
    private final ScreenCustomization[] customizations;

    public ScreenCustomizationHolder(ScreenCustomization[] customizations) {
        this.customizations = customizations;
    }

    public <T extends ScreenIndexer>void apply(HaulCondition.Context ctx, T screenIndexer) {
        for (ScreenCustomization sc : customizations) {
            if (sc.passesConditions(ctx))
                sc.apply(screenIndexer);
        }
    }

    public static ScreenCustomizationHolder fromJson(JsonElement json) {
        if (json.isJsonArray()) {
            JsonArray arr = json.getAsJsonArray();
            ScreenCustomization[] customizations = new ScreenCustomization[arr.size()];
            for (int i = 0; i < customizations.length; i++) {
                customizations[i] = ScreenCustomization.fromJson(arr.get(i).getAsJsonObject());
            }
            return new ScreenCustomizationHolder(customizations);
        } else {
            return new ScreenCustomizationHolder(new ScreenCustomization[]{ ScreenCustomization.fromJson(json.getAsJsonObject()) });
        }
    }
}
