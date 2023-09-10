package dev.omo.guihaul.api.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.Arrays;

public class ScreenCustomizationHolder {
    private final ScreenCustomization[] customizations;

    public ScreenCustomizationHolder(ScreenCustomization[] customizations) {
        this.customizations = customizations;
    }

    public void modifyScreen(HaulCondition.Context ctx, Object screenIndexer) {
        for (ScreenCustomization sc : customizations) {
            if (sc.passesConditions(ctx))
                sc.modifyScreen(screenIndexer);
        }
    }

    public void cleanupScreen(HaulCondition.Context ctx, Object screenIndexer) {
        for (ScreenCustomization sc : customizations) {
            if (sc.passesConditions(ctx))
                sc.cleanupScreen(screenIndexer);
        }
    }

    public void modifyScreenInit(HaulCondition.Context ctx, Object screenIndexer) {
        for (ScreenCustomization sc : customizations) {
            if (sc.passesConditions(ctx))
                sc.modifyScreenInit(screenIndexer);
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < customizations.length; i++) {
            builder.append(i).append(": ").append(customizations[i]);
        }
        return builder.toString();
    }
}
