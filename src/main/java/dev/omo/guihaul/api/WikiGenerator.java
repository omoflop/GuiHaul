package dev.omo.guihaul.api;

import com.google.common.collect.HashMultimap;
import dev.omo.guihaul.GuiHaulMod;
import dev.omo.guihaul.api.data.HaulCondition;
import dev.omo.guihaul.api.data.HaulModifier;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ObjectShare;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.*;
import java.util.function.BiConsumer;

public final class WikiGenerator {

    static final HashMap<Identifier, String> screenDescriptions = new HashMap<>();
    static final HashMap<Identifier, String> modifierDescriptions = new HashMap<>();
    static final HashMap<Identifier, String> conditionDescriptions = new HashMap<>();

    static final HashMap<Identifier, HashMap<Class<?>, PropertyDesc>> propertyDescriptions = new HashMap<>();

    record PropertyDesc(String desc, @Nullable String[] examples) {}

    public static void start() {
        Path dir = FabricLoader.getInstance().getGameDir();
        Path wikiOut = Paths.get(dir.toString(), ".guihaul");
        try {
            if (!Files.exists(wikiOut)) {
                Files.createDirectory(wikiOut);
            }


            Files.writeString(Paths.get(wikiOut.toString(), "screen_types.md"), generateScreenTypes(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
            Files.writeString(Paths.get(wikiOut.toString(), "gui_modifiers.md"), generateModifiers(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
            Files.writeString(Paths.get(wikiOut.toString(), "condition_types.md"), generateConditionTypes(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
            Files.writeString(Paths.get(wikiOut.toString(), "property_types.md"), generatePropertyTypes(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private static String generateModifiers() {
        StringBuilder builder = new StringBuilder();

        iterateIdentifiers(HaulApi.modifierTypes.keySet(), (modName, ids) -> {
            insertTitle(builder, modName);

            for (Identifier modifierId : ids) {
                HaulModifier<?> modifier = HaulApi.getModifier(modifierId);
                insertEntryName(builder, modifierId);
                insertDescription(builder, modifierDescriptions.getOrDefault(modifierId, null));
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
                insertDescription(builder, screenDescriptions.getOrDefault(screenId, null));
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
                insertDescription(builder, conditionDescriptions.getOrDefault(conditionId, null));
                insertId(builder, conditionId);
                insertArguments(builder, condition);
            }
        });

        return builder.toString();
    }

    private static String generatePropertyTypes() {
        StringBuilder builder = new StringBuilder();

        propertyDescriptions.forEach((modId, map) -> {
            insertTitle(builder, modId.getNamespace());

            for (Class<?> type : map.keySet()) {
                PropertyDesc desc = map.get(type);
                insertEntryName(builder, type.getSimpleName());
                insertDescription(builder, desc.desc);
                insert(builder, "Class", type.getCanonicalName());
                if (desc.examples != null && desc.examples.length > 0) {
                    builder.append("**Examples**:\n");
                    for (String curExample : desc.examples) {
                        builder.append("- ").append(curExample).append("\n");
                    }
                    builder.append("\n");
                }
            }
        });

        return builder.toString();
    }

    private static void iterateIdentifiers(Collection<Identifier> collection, BiConsumer<String, Collection<Identifier>> consumer) {
        HashMultimap<String, Identifier> ids = HashMultimap.create();
        for (Identifier identifier : collection) {
            ids.put(identifier.getNamespace(), identifier);
        }

        ids.keySet().forEach(key -> consumer.accept(key, ids.get(key)));
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

    private static void insertEntryName(StringBuilder builder, String str) {
        builder.append("## ").append(makePretty(str)).append("\n\n");
    }

    private static void insertId(StringBuilder builder, Identifier id) {
        insert(builder, "Id", id.toString());
    }

    private static void insert(StringBuilder builder, String label, String text) {
        builder.append("**").append(label).append("**: `").append(text).append("`\n\n");
    }

    private static void insertArguments(StringBuilder builder, PropertySupplier propertySupplier) {
        builder.append("**Arguments**:\n\n");

        iterateProperties(propertySupplier, (curProperty, isRequired) -> {
            builder.append("* `").append(curProperty.valueClass.getSimpleName()).append("` ");

            for (HashMap<Class<?>, PropertyDesc> map : propertyDescriptions.values()) {
                if (!map.containsKey(curProperty.valueClass)) continue;

                builder.append("*[[?|Property Types#").append(curProperty.valueClass.getSimpleName()).append("]]* ");

                break;
            }

            builder.append(curProperty.name).append(' ');
            if (isRequired) {
                builder.append("**(required)**");
            }
            builder.append('\n');

        });
    }

    private static void insertDescription(StringBuilder builder, @Nullable String desc) {
        if (desc != null) {
            builder.append("**Description**: ").append(desc).append("\n\n");
        }
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
