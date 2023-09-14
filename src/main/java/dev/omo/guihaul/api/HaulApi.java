package dev.omo.guihaul.api;

import com.google.common.collect.HashBiMap;
import com.google.gson.JsonElement;
import dev.omo.guihaul.GuiHaulMod;
import dev.omo.guihaul.api.data.HaulCondition;
import dev.omo.guihaul.api.data.HaulModifier;
import dev.omo.guihaul.api.data.ScreenCustomizationHolder;
import dev.omo.guihaul.entry.HaulApiEntrypoint;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public final class HaulApi {
    static final HashBiMap<Identifier, HaulModifier<?>> modifierTypes = HashBiMap.create();
    static final HashBiMap<Identifier, HaulCondition> conditionTypes = HashBiMap.create();
    static final HashMap<PropertySupplier, PropertyHolder> propertyHolderTemplates = new HashMap<>();
    static final HashMap<Identifier, ScreenCustomizationHolder> customizations = new HashMap<>();
    static final HashMap<Identifier, ScreenMatcher> screenTypeMatchers = new HashMap<>();

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
            if (screenTypeMatchers.get(screenId).applicableTo(screen)) {
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

    public static void modifyScreen(Screen screen) {
        if (screen == null) return;
        ScreenCustomizationHolder c = HaulApi.getCustomizations(screen);
        if (c != null) {
            HaulCondition.Context ctx = HaulCondition.Context.fromScreen(screen);
            c.modifyScreen(ctx, screen);
        }
    }

    public static void modifyScreenInit(Screen screen) {
        if (screen == null) return;
        ScreenCustomizationHolder c = HaulApi.getCustomizations(screen);
        if (c != null) {
            HaulCondition.Context ctx = HaulCondition.Context.fromScreen(screen);
            c.modifyScreenInit(ctx, screen);
        }
    }

    public static void cleanupScreen(Screen screen) {
        if (screen == null) return;
        ScreenCustomizationHolder c = HaulApi.getCustomizations(screen);
        if (c != null) {
            HaulCondition.Context ctx = HaulCondition.Context.fromScreen(screen);
            c.cleanupScreen(ctx, screen);
        }
    }

    public static class Builder {

        final HashMap<Class<?>, WikiGenerator.PropertyDesc> propertyDescriptions = new HashMap<>();

        private static RuntimeException error(String name, Object id) {
            return new RuntimeException(String.format("Failed to register a %s under an existing identifier! For id: %s", name, id));
        }

        public <T extends HaulModifier<?>>void addModifier(Identifier id, T modifierInstance) {
            addModifier(id, modifierInstance, null);
        }

        public <T extends HaulCondition>void addCondition(Identifier id, T conditionInstance) {
            addCondition(id, conditionInstance, null);
        }

        public void addScreenMatcher(ScreenMatcher matcher) {
            addScreenMatcher(matcher, null);
        }

        public <T>void addPropertyTypeHandler(Class<T> type, Function<JsonElement, T> parser) {
            addPropertyTypeHandler(type, parser, null);
        }

        public <T extends HaulModifier<?>>void addModifier(Identifier id, T modifierInstance, @Nullable String wikiDesc) {
            tryPut(modifierTypes, "element type", id, modifierInstance);
            if (GuiHaulMod.GENERATE_WIKI && wikiDesc != null) {
                WikiGenerator.modifierDescriptions.put(id, wikiDesc);
            }
        }

        public <T extends HaulCondition>void addCondition(Identifier id, T conditionInstance, @Nullable String wikiDesc) {
            tryPut(conditionTypes, "condition type", id, conditionInstance);
            if (GuiHaulMod.GENERATE_WIKI && wikiDesc != null) {
                WikiGenerator.conditionDescriptions.put(id, wikiDesc);
            }
        }

        public void addScreenMatcher(ScreenMatcher matcher, @Nullable String wikiDesc) {
            tryPut(screenTypeMatchers, "screen matcher type", matcher.getScreenId(), matcher);
            if (GuiHaulMod.GENERATE_WIKI && wikiDesc != null) {
                WikiGenerator.screenDescriptions.put(matcher.getScreenId(), wikiDesc);
            }
        }

        public <T>void addPropertyTypeHandler(Class<T> type, Function<JsonElement, T> parser, @Nullable String wikiDesc, @Nullable String... examples) {
            Property.registerClass(type, parser);
            if (GuiHaulMod.GENERATE_WIKI && wikiDesc != null) {
                propertyDescriptions.put(type, new WikiGenerator.PropertyDesc(wikiDesc, examples));
            }
        }

        private <A,B>void tryPut(Map<A, B> map, String name, A id, B obj) {
            if (map.containsKey(id))
                throw error(name, id);

            map.put(id, obj);
        }

        // Don't call this!
        @Deprecated
        public void build(HaulApiEntrypoint entrypoint) {
            if (GuiHaulMod.GENERATE_WIKI) {
                WikiGenerator.propertyDescriptions.put(entrypoint.getApiProviderName(), propertyDescriptions);
            }
        }
    }

}
