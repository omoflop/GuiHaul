package dev.omo.guihaul.docs;

import dev.omo.guihaul.GuiHaulMod;
import dev.omo.guihaul.api.GuiCustomization;
import dev.omo.guihaul.api.HaulableGuis;
import dev.omo.guihaul.data.CustomizationType;
import dev.omo.guihaul.data.CustomizationTypes;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class WikiGenerator {
    public static void start() {
        GuiHaulMod.LOGGER.info(HaulableGuis.getSupportedContainers().stream().map(WikiGenerator::createContainerEntry).collect(Collectors.joining()));
        GuiHaulMod.LOGGER.info(CustomizationTypes.all().stream().map(WikiGenerator::createModifierEntry).collect(Collectors.joining()));
    }

    private static String createModifierEntry(CustomizationType type) {
        StringBuilder output = new StringBuilder();
        output.append("# ");            output.append(makePretty(type.getName()));  output.append("\n\n");
        output.append("**Id**: `");     output.append(type.getName());              output.append("`\n");
        output.append("```\n");
        modifierClassToString(output, type.getResultClass(), 1);
        output.append("\n```\n\n");
        return output.toString();
    }

    private static void appendTypeValue(Class<?> type, WikiFieldDesc annotation, StringBuilder builder, int depth) {
        if (!annotation.optional()) {
            builder.append("(required) ");
        }

        if (type.isPrimitive() || type == String.class) {
            builder.append(type.getTypeName());
        } else if (type == Identifier.class) {
            builder.append("String");
        } else if (GuiCustomization.class.isAssignableFrom(type)) {
            modifierClassToString(builder, type, depth + 1);
        } else if (type.getGenericSuperclass() instanceof ParameterizedType pt) {
            Type[] typeArgs = pt.getActualTypeArguments();
            builder.append(Arrays.stream(typeArgs).map(Type::getTypeName).collect(Collectors.joining(", ")));
        } else if (type.isEnum()) {
            Class<? extends Enum<?>> enumType = (Class<? extends Enum<?>>) type;
            Enum<?>[] values = enumType.getEnumConstants();
            builder.append("any of: ");
            builder.append(Arrays.stream(values).map(Enum::name).collect(Collectors.joining(", ")));
        }
    }

    private static void modifierClassToString(StringBuilder builder, Class<?> clazz, int depth) {
        builder.append("{\n");
        String indent = "  ".repeat(depth);
        Field[] fields = clazz.getFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (!field.isAnnotationPresent(WikiFieldDesc.class)) continue;;
            WikiFieldDesc annotation = field.getAnnotation(WikiFieldDesc.class);

            builder.append(indent);

            if (annotation.bodyIsMap()) {
                builder.append('"');
                builder.append(annotation.mapKeyExample());
                builder.append("\" = ");
                appendTypeValue(annotation.mapValueClass(), annotation, builder, depth);
            } else {
                builder.append('"');
                builder.append(field.getName());
                builder.append("\" = ");
                appendTypeValue(field.getType(), annotation, builder, depth);
            }

            if (i < fields.length - 1) {
                builder.append(',');
            }
            builder.append('\n');
        }

        if (depth > 1) {
            builder.append("  ".repeat(depth-1));
        }
        builder.append('}');
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

    private static String createContainerEntry(Identifier containerId) {
        StringBuilder output = new StringBuilder();
        output.append("# ");        output.append(makePretty(containerId.getPath()));  output.append("\n");
        output.append("**Id**: `"); output.append(containerId); output.append("`\n\n");
        output.append("**Source**: `"); output.append(containerId.getNamespace()); output.append("`\n\n");
        output.append("**Supported modifiers**:\n");
        HaulableGuis.getSupportedCustomizations(containerId).stream().sorted().forEach(ct -> {
            output.append("* ");
            output.append(ct.getName());
            output.append('\n');
        });

        output.append('\n');
        return output.toString();
    }

}
