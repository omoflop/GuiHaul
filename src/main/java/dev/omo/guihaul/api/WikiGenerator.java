package dev.omo.guihaul.api;

import com.google.common.collect.HashMultimap;
import dev.omo.guihaul.GuiHaulMod;
import dev.omo.guihaul.api.data.HaulCondition;
import dev.omo.guihaul.api.data.HaulModifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.function.BiConsumer;

public final class WikiGenerator {

    public static void start() {
        GuiHaulMod.LOGGER.info("Screen Types\n"+generateScreenTypes());
        GuiHaulMod.LOGGER.info("Modifiers\n"+generateModifiers());
        GuiHaulMod.LOGGER.info("Conditions\n"+generateConditionTypes());
    }

    private static void iterateIdentifiers(Collection<Identifier> collection, BiConsumer<String, Collection<Identifier>> consumer) {
        HashMultimap<String, Identifier> ids = HashMultimap.create();
        for (Identifier identifier : collection) {
            ids.put(identifier.getNamespace(), identifier);
        }

        ids.keySet().forEach(key -> consumer.accept(key, ids.get(key)));
    }

    private static String generateModifiers() {
        StringBuilder builder = new StringBuilder();

        iterateIdentifiers(HaulApi.modifierTypes.keySet(), (modName, ids) -> {
            insertTitle(builder, modName);

            for (Identifier modifierId : ids) {
                HaulModifier<?> modifier = HaulApi.getModifier(modifierId);
                insertEntryName(builder, modifierId);
                insertDescription(builder, modifier);
                insertId(builder, modifierId);
                insertArguments(builder, modifier);
            }
        });

        return builder.toString();
    }

    private static String generateScreenTypes() {
        StringBuilder builder = new StringBuilder();

        iterateIdentifiers(HaulApi.screenTypeMatchers.keySet(), (modName, ids) -> {
            insertTitle(builder, modName);

            for (Identifier screenId : ids) {
                ScreenMatcher matcher = HaulApi.screenTypeMatchers.get(screenId);
                insertEntryName(builder, screenId);
                insertDescription(builder, matcher);
                insertId(builder, screenId);

                builder.append("**Supported Modifiers**:\n\n");
                for (Identifier modifierId : HaulApi.modifierTypes.keySet()) {
                    HaulModifier<?> modifier = HaulApi.getModifier(modifierId);
                    if (Arrays.stream(matcher.getSupportedScreens()).anyMatch(modifier::isApplicableTo)) {
                        builder.append("* [[").append(modifierId).append("|Gui Modifiers#").append(makePretty(modifierId.getPath())).append("]]\n");
                    }
                }
            }
        });

        return builder.toString();
    }

    private static String generateConditionTypes() {
        StringBuilder builder = new StringBuilder();

        iterateIdentifiers(HaulApi.conditionTypes.keySet(), (modName, ids) -> {
            insertTitle(builder, modName);

            for (Identifier conditionId : ids) {
                HaulCondition condition = HaulApi.getCondition(conditionId);
                insertEntryName(builder, conditionId);
                insertDescription(builder, condition);
                insertId(builder, conditionId);
                insertArguments(builder, condition);
            }
        });

        return builder.toString();
    }

    private static void iterateProperties(PropertySupplier supplier, BiConsumer<Property<?>, Boolean> operation) {
        PropertyHolder holder = HaulApi.createPropertyHolder(supplier);
        Set<Property<?>> all = holder.getAllProperties();
        Set<Property<?>> required = holder.getRequiredProperties();
        all.stream().sorted(Comparator.comparing(a -> a.name)).forEach(c -> {
            operation.accept(c, required.contains(c));
        });
    }


    private static void insertTitle(StringBuilder builder, String titleText) {
        builder.append("# ").append(makePretty(titleText)).append('\n');
    }

    private static void insertEntryName(StringBuilder builder, Identifier entryId) {
        builder.append("## ").append(makePretty(entryId.getPath())).append("\n\n");
    }

    private static void insertId(StringBuilder builder, Identifier id) {
        builder.append("**Id**: `").append(id).append("`\n\n");
    }

    private static void insertArguments(StringBuilder builder, PropertySupplier propertySupplier) {
        builder.append("**Arguments**:\n\n");

        iterateProperties(propertySupplier, (curProperty, isRequired) -> {
            builder.append("* `").append(curProperty.valueClass.getSimpleName()).append("` ").append(curProperty.name).append(' ');
            if (isRequired) {
                builder.append("**(required)**");
            }
            builder.append('\n');

        });
    }

    private static void insertDescription(StringBuilder builder, Object obj) {
        WikiDesc desc = getWikiDesc(obj);
        if (desc != null) {
            builder.append("**Description**: ").append(desc.value()).append("\n\n");
        }
    }

    private static @Nullable WikiDesc getWikiDesc(@Nullable Object obj) {
        return obj == null ? null : obj.getClass().getAnnotation(WikiDesc.class);
    }

    private static String makePretty(String str) {
        StringBuilder prettyName = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i == 0 || str.charAt(i - 1) == '_') {
                c = Character.toUpperCase(c);
            }
            prettyName.append(c == '_' ? ' ' : c);
        }
        return prettyName.toString();
    }
}
