package dev.omo.guihaul.api.impl;

import dev.omo.guihaul.api.GuiModifier;
import dev.omo.guihaul.data.CustomizationHolder;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;

public class BasicGuiModifier<T extends ScreenHandler> implements GuiModifier<HandledScreen<T>, ScreenHandler> {
    @Override
    public void modifyGui(CustomizationHolder c, HandledScreen<T> screen, @Nullable ScreenHandler handler, @Nullable ScreenHandlerType<ScreenHandler> type) {
        if (handler == null) return;
        GuiModifier.applySlotCustomizations(c, handler.slots);
    }

    @Override
    public void cleanupGui(CustomizationHolder c, HandledScreen<T> screen, @Nullable ScreenHandler handler, ScreenHandlerType<ScreenHandler> handlerType) {
        if (handler == null) return;
        GuiModifier.cleanupSlotCustomizations(c, handler.slots);
    }
}
