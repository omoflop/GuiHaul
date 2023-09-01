package dev.omo.guihaul.api;

import dev.omo.guihaul.data.AllCustomizations;
import dev.omo.guihaul.data.CustomizationHolder;
import dev.omo.guihaul.duck.ScreenAccessor;
import dev.omo.guihaul.impl.customizations.SlotCustomization;
import dev.omo.guihaul.duck.ScreenHandlerAccessor;
import dev.omo.guihaul.duck.SlotAccessor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;

public interface GuiModifier<TScreen extends Screen, TScreenHandler extends ScreenHandler> {
    void modifyGui(CustomizationHolder c, TScreen screen, @Nullable TScreenHandler handler, @Nullable ScreenHandlerType<TScreenHandler> handlerType);

    void cleanupGui(CustomizationHolder c, TScreen screen, @Nullable TScreenHandler handler, @Nullable ScreenHandlerType<TScreenHandler> handlerType);

    // forgive me for I have sinned
    // if you need to call this ever just reach out to me IDK what you did
    @Deprecated()
    @SuppressWarnings("unchecked")
    default void modifyGuiRaw(CustomizationHolder c, Object screen, @Nullable Object handler, @Nullable Object handlerType) {
        ScreenAccessor.setCustomizations((TScreen)screen, c);
        modifyGui(c, (TScreen) screen, (TScreenHandler) handler, (ScreenHandlerType<TScreenHandler>) handlerType);
    }

    @Deprecated()
    @SuppressWarnings("unchecked")
    default void cleanupGuiRaw(CustomizationHolder c, Object screen, @Nullable Object handler, @Nullable Object handlerType) {
        cleanupGui(c, (TScreen) screen, (TScreenHandler) handler, (ScreenHandlerType<TScreenHandler>) handlerType);
        ScreenAccessor.setCustomizations((TScreen)screen, null);
    }

    static void setupScreen(Screen screen) {
        if (screen == null) return;
        var data = getModifierData(screen);
        if (data == null) return;
        Pair<ScreenHandler, ScreenHandlerType<?>> r = data.getRight();
        data.getLeft().modifyGuiRaw(data.getMiddle(), screen, r.getLeft(), r.getRight());
    }

    static void cleanupScreen(Screen screen) {
        if (screen == null) return;
        var data = getModifierData(screen);
        if (data == null) return;
        Pair<ScreenHandler, ScreenHandlerType<?>> r = data.getRight();
        data.getLeft().cleanupGuiRaw(data.getMiddle(), screen, r.getLeft(), r.getRight());
    }

    static @Nullable Triple<GuiModifier<?, ?>, CustomizationHolder, Pair<ScreenHandler, ScreenHandlerType<?>>> getModifierData(Screen screen) {
        if (screen == null) return null;

        // Check if this screen has a modifier, if not, return
        GuiModifier<?, ?> modifier = HaulableGuis.getModifier(screen.getClass());
        if (modifier == null) return null;

        // Check if this screen has a customization, if not, return
        CustomizationHolder customization = AllCustomizations.get(screen.getClass());
        if (customization == null) return null;

        ScreenHandler handler = null;
        ScreenHandlerType<?> type = null;
        if (screen instanceof HandledScreen<?> handledScreen) {
            handler = handledScreen.getScreenHandler();
            if (handler != null) {
                type = ScreenHandlerAccessor.get(handler).guihaul$getType();
            }
        }

        return Triple.of(modifier, customization, Pair.of(handler, type));
    }

    static void applySlotCustomizations(CustomizationHolder c, DefaultedList<Slot> slots) {
        slots.forEach(slot -> {
            SlotCustomization sc = c.getForSlot(slot);
            if (sc != null) applySlotCustomization(sc, slot);
        });
    }

    static void cleanupSlotCustomizations(CustomizationHolder c, DefaultedList<Slot> slots) {
        slots.forEach(slot -> SlotAccessor.get(slot).guihaul$reset());
    }

    static void applySlotCustomization(SlotCustomization sc, Slot slot) {
        // Get all the necessary data for modifying this slot
        SlotAccessor sa = SlotAccessor.get(slot);

        // Set the customization
        sa.guihaul$setCustomization(sc);

        // Reposition said slot
        if (sc.absolute) {
            sa.guihaul$setX(sc.x);
            sa.guihaul$setY(sc.y);
        } else {
            sa.guihaul$setX(slot.x + sc.x);
            sa.guihaul$setY(slot.y + sc.y);
        }
    }
}
