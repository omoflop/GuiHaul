package dev.omo.guihaul.api;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dev.omo.guihaul.GuiHaulMod;
import dev.omo.guihaul.api.data.HaulCondition;
import dev.omo.guihaul.api.data.HaulModifier;
import dev.omo.guihaul.api.data.ScreenCustomizationHolder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public final class HaulApi {
    private static final HashBiMap<Identifier, HaulModifier<?>> modifierTypes = HashBiMap.create();
    private static final HashBiMap<Identifier, HaulCondition> conditionTypes = HashBiMap.create();
    private static final HashMap<PropertySupplier, PropertyHolder> propertyHolderTemplates = new HashMap<>();
    private static final HashMap<Identifier, ScreenCustomizationHolder> customizations = new HashMap<>();
    private static final HashMap<Identifier, Predicate<Screen>> screenTypeMatchers = new HashMap<>();

    public static void addCustomization(Identifier id, ScreenCustomizationHolder c) {
        customizations.put(id, c);
    }

    public static @Nullable HaulModifier<?> getModifier(Identifier id) {
        return modifierTypes.getOrDefault(id, null);
    }

    public static @Nullable HaulCondition getCondition(Identifier id) {
        return conditionTypes.getOrDefault(id, null);
    }

    public static Identifier getModifierId(HaulModifier<?> modifier) {
        return modifierTypes.inverse().get(modifier);
    }

    public static Identifier getConditionId(HaulCondition condition) {
        return conditionTypes.inverse().get(condition);
    }

    public static @Nullable ScreenCustomizationHolder getCustomizations(Screen screen) {
        for (Identifier screenId : screenTypeMatchers.keySet()) {
            if (screenTypeMatchers.get(screenId).test(screen)) {
                return customizations.getOrDefault(screenId, null);
            }
        }
        return null;
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

    public static void printApiDebug() {
        StringBuilder str = new StringBuilder();
        str.append("==== GuiHaul API Debug ====\n");
        str.append("### Modifier Types\n");
        modifierTypes.forEach((id, m) -> str.append("- ").append(id).append('\n'));
        str.append("### Condition Types\n");
        conditionTypes.forEach((id, m) -> str.append("- ").append(id).append('\n'));
        str.append("### Screen Types\n");
        screenTypeMatchers.keySet().forEach(id -> str.append("- ").append(id).append('\n'));
        GuiHaulMod.LOGGER.info(str.toString());
    }

    public static void printResourcePackDebug() {
        StringBuilder str = new StringBuilder();
        str.append("==== GuiHaul ResourcePack Debug ====\n");
        str.append("### Customizations\n");
        customizations.forEach((id, c) -> str.append("- ").append(id).append('\n').append(c).append('\n'));

        GuiHaulMod.LOGGER.info(str.toString());
    }

    public static void modifyScreen(Screen screen, boolean apply) {
        if (screen == null) return;
        ScreenCustomizationHolder c = HaulApi.getCustomizations(screen);
        if (c != null) {
            HaulCondition.Context ctx = HaulCondition.Context.fromScreen(screen);
            if (apply)   c.modifyScreen(ctx, screen);
            else        c.cleanupScreen(ctx, screen);
        }
    }

    public static class Builder {
        private static RuntimeException error(String name, Object id) {
            return new RuntimeException(String.format("Failed to register a %s under an existing identifier! For id: %s", name, id));
        }

        public <T extends HaulModifier<?>>void addModifier(Identifier id, T modifierInstance) {
            tryPut(modifierTypes, "element type", id, modifierInstance);
        }

        public <T extends HaulCondition>void addCondition(Identifier id, T conditionInstance) {
            tryPut(conditionTypes, "condition type", id, conditionInstance);
        }

        public void addScreenMatcher(Identifier screenId, Predicate<Screen> matcher) {
            tryPut(screenTypeMatchers, "screen matcher type", screenId, matcher);
        }

        private <A,B>void tryPut(Map<A, B> map, String name, A id, B obj) {
            if (map.containsKey(id))
                throw error(name, id);

            map.put(id, obj);
        }
    }

}
