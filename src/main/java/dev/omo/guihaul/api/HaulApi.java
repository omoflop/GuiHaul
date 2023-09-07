package dev.omo.guihaul.api;

import dev.omo.guihaul.GuiHaulMod;
import dev.omo.guihaul.api.data.HaulCondition;
import dev.omo.guihaul.api.data.HaulModifier;
import dev.omo.guihaul.api.data.ScreenCustomizationHolder;
import dev.omo.guihaul.api.data.ScreenIndexer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public final class HaulApi {
    private static final HashMap<Identifier, HaulModifier<?>> modifierTypes = new HashMap<>();
    private static final HashMap<Identifier, ScreenIndexer> screenIndexerTypes = new HashMap<>();
    private static final HashMap<Identifier, HaulCondition> conditionTypes = new HashMap<>();
    private static final HashMap<PropertySupplier, PropertyHolder> propertyHolderTemplates = new HashMap<>();
    private static final HashMap<Identifier, ScreenCustomizationHolder> customizations = new HashMap<>();

    public static void addCustomization(Identifier id, ScreenCustomizationHolder c) {
        customizations.put(id, c);
    }

    public static @Nullable HaulModifier<?> getModifier(Identifier id) {
        return modifierTypes.getOrDefault(id, null);
    }

    public static @Nullable ScreenIndexer getScreenIndexer(Identifier id) {
        return screenIndexerTypes.getOrDefault(id, null);
    }

    public static @Nullable HaulCondition getCondition(Identifier id) {
        return conditionTypes.getOrDefault(id, null);
    }

    public static PropertyHolder createPropertyHolder(PropertySupplier supplier) {
        if (propertyHolderTemplates.containsKey(supplier)) {
            return propertyHolderTemplates.get(supplier).copy();
        }

        PropertyHolder.Builder builder = new PropertyHolder.Builder();
        supplier.appendProperties(builder);
        propertyHolderTemplates.put(supplier, builder.build());
        return propertyHolderTemplates.get(supplier).copy();
    }

    private static void build(PropertySupplier... suppliers) {
        for (PropertySupplier supplier : suppliers) {
            PropertyHolder.Builder builder = new PropertyHolder.Builder();
            supplier.appendProperties(builder);
            propertyHolderTemplates.put(supplier, builder.build());
        }
    }

    public static void printDebug() {
        StringBuilder str = new StringBuilder();
        str.append("==== GuiHaul Debug ====\n");
        str.append("### Modifier Types\n");
        modifierTypes.forEach((id, m) -> str.append("- ").append(id).append('\n'));
        str.append("### Condition Types\n");
        conditionTypes.forEach((id, m) -> str.append("- ").append(id).append('\n'));
        GuiHaulMod.LOGGER.info(str.toString());
    }

    public static class Builder {
        private static RuntimeException error(String name, Identifier id) {
            return new RuntimeException(String.format("Failed to register a %s under an existing identifier! For id: %s", name, id));
        }

        public <T extends HaulModifier<?>>void addModifier(Identifier id, T modifierInstance) {
            tryPut(modifierTypes, "element type", id, modifierInstance);
        }

        public <T extends ScreenIndexer>void putScreenIndexerType(Identifier id, T indexerInstance) {
            tryPut(screenIndexerTypes, "screen indexer type", id, indexerInstance);
        }

        public <T extends HaulCondition>void putConditionType(Identifier id, T conditionInstance) {
            tryPut(conditionTypes, "condition type", id, conditionInstance);
        }

        private <T>void tryPut(HashMap<Identifier, T> map, String name, Identifier id, T obj) {
            if (map.containsKey(id))
                throw error(name, id);

            map.put(id, obj);
        }
    }

}
