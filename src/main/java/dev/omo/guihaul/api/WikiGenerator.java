package dev.omo.guihaul.api;

import com.google.common.collect.HashMultimap;
import dev.omo.guihaul.GuiHaulMod;
import dev.omo.guihaul.api.data.HaulModifier;
import net.minecraft.util.Identifier;

import javax.xml.crypto.dsig.spec.HMACParameterSpec;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

public final class WikiGenerator {

    public static void start() {
        GuiHaulMod.LOGGER.info("Screen Types\n"+generateScreenTypes());
        GuiHaulMod.LOGGER.info("Modifiers\n"+generateModifiers());
        //GuiHaulMod.LOGGER.info("Conditions\n"+generateConditions());
    }

    private static HashMultimap<String, Identifier> getSortedIdentifiers(Collection<Identifier> collection) {
        HashMultimap<String, Identifier> ids = HashMultimap.create();
        for (Identifier identifier : collection) {
            ids.put(identifier.getNamespace(), identifier);
        }
        return ids;
    }

    private static String generateModifiers() {
        final boolean useMarkdownTable = false;
        StringBuilder builder = new StringBuilder();
        HashMultimap<String, Identifier> ids = getSortedIdentifiers(HaulApi.modifierTypes.keySet());

        ids.keySet().stream().sorted().forEach(modName -> {
            builder.append("# ").append(makePretty(modName)).append('\n');
            for (Identifier modifierId : ids.get(modName)) {
                builder.append("## ").append(makePretty(modifierId.getPath())).append("\n\n");
                builder.append("**Id**: `").append(modifierId).append("`\n\n");

                builder.append("**Arguments**:\n\n");
                PropertyHolder holder = HaulApi.createPropertyHolder(HaulApi.getModifier(modifierId));
                Set<Property<?>> all = holder.getAllProperties();
                Set<Property<?>> required = holder.getRequiredProperties();
                if (useMarkdownTable) {
                    builder.append("|Type|Name||\n");
                    builder.append("|-|-|-|\n");
                }
                all.stream().sorted(Comparator.comparing(a -> a.name)).forEach(curProperty -> {
                    if (useMarkdownTable) {
                        builder.append('|').append(curProperty.valueClass.getSimpleName()).append('|');
                        builder.append(curProperty.name).append('|');
                        if (required.contains(curProperty)) {
                            builder.append("Required");
                        }
                        builder.append("|\n");

                    } else {
                        builder.append("* `").append(curProperty.valueClass.getSimpleName()).append("` ").append(curProperty.name).append(' ');
                        if (required.contains(curProperty)) {
                            builder.append("**(required)**");
                        } else {
                            //builder.append("**(default: ").append(holder.getProperty(curProperty)).append(")**");
                        }
                        builder.append('\n');
                    }
                });
            }
        });

        return builder.toString();
    }

    private static String generateScreenTypes() {
        StringBuilder builder = new StringBuilder();
        HashMultimap<String, Identifier> ids = getSortedIdentifiers(HaulApi.screenTypeMatchers.keySet());

        ids.keySet().stream().sorted().forEach(modName -> {
            builder.append("# ").append(makePretty(modName)).append('\n');
            for (Identifier screenId : ids.get(modName)) {
                builder.append("## ").append(makePretty(screenId.getPath())).append("\n\n");
                builder.append("**Id**: `").append(screenId).append("`\n\n");

                ScreenMatcher matcher = HaulApi.screenTypeMatchers.get(screenId);
                builder.append("**Supported Modifiers**:\n\n");
                for (Identifier modifierId : HaulApi.modifierTypes.keySet()) {
                    HaulModifier<?> modifier = HaulApi.getModifier(modifierId);
                    if (Arrays.stream(matcher.getSupportedScreens()).anyMatch(modifier::isApplicableTo)) {
                        builder.append("* `").append(modifierId).append("`\n");
                    }
                }
            }
        });

        return builder.toString();
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
