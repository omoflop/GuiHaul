package dev.omo.guihaul.api.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.omo.guihaul.util.JsonUtils;

import java.util.Arrays;

public class ScreenCustomization {
    public final ObjectWithProperties<HaulCondition>[] conditions;
    public final ObjectWithProperties<HaulModifier<?>>[] modifiers;

    public ScreenCustomization(ObjectWithProperties<HaulCondition>[] conditions, ObjectWithProperties<HaulModifier<?>>[] modifiers) {
        this.conditions = conditions;
        this.modifiers = modifiers;
    }

    public boolean passesConditions(HaulCondition.Context ctx) {
        for (ObjectWithProperties<HaulCondition> c : conditions) {
            if (!c.value.passesCondition(c.holder, ctx))
                return false;
        }
        return true;
    }

    public void modifyScreen(Object screenIndexer) {
        for (ObjectWithProperties<HaulModifier<?>> m : modifiers) {
            m.value.modifyScreenInternal(m.holder, screenIndexer);
        }
    }

    public void cleanupScreen(Object screenIndexer) {
        for (ObjectWithProperties<HaulModifier<?>> m : modifiers) {
            m.value.cleanupScreenInternal(m.holder, screenIndexer);
        }
    }

    public static ScreenCustomization fromJson(JsonObject json) {
        if (!json.has("conditions")) return null;

        ObjectWithProperties<HaulCondition>[] conditions = null;
        if (json.has("conditions")) {
            JsonArray jsonConditions = json.getAsJsonArray("conditions");
            conditions = JsonUtils.toArray(jsonConditions, new ObjectWithProperties[jsonConditions.size()], HaulCondition::fromJson);
        }

        JsonArray jsonModifiers = json.getAsJsonArray("modifiers");
        ObjectWithProperties<HaulModifier<?>>[] modifiers = JsonUtils.toArray(jsonModifiers, new ObjectWithProperties[jsonModifiers.size()], HaulModifier::fromJson);

        return new ScreenCustomization(conditions, modifiers);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Conditions: ").append(conditions.length);
        builder.append(", Modifiers: ").append(modifiers.length);

        if (conditions.length > 0) {
            builder.append("\n\tConditions");
            for (int i = 0; i < conditions.length; i++) {
                builder.append("\n\t").append(i).append(": ");
                builder.append(conditions[i]);
            }
        }

        if (modifiers.length > 0) {
            builder.append("\n\tModifiers");
            for (int i = 0; i < modifiers.length; i++) {
                builder.append("\n\t").append(i).append(": ");
                builder.append(modifiers[i]);
            }
        }

        return builder.toString();
    }
}
