package dev.omo.guihaul.impl.modifier;

import dev.omo.guihaul.api.GuiModifier;
import dev.omo.guihaul.data.CustomizationHolder;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;

public class SurvivalInventoryGuiModifier implements GuiModifier<InventoryScreen, PlayerScreenHandler> {
    @Override
    public void modifyGui(CustomizationHolder c, InventoryScreen screen, @Nullable PlayerScreenHandler handler, @Nullable ScreenHandlerType<PlayerScreenHandler> type) {
        if (handler == null) return;
        GuiModifier.applySlotCustomizations(c, handler.slots);
    }

    @Override
    public void cleanupGui(CustomizationHolder c, InventoryScreen screen, @Nullable PlayerScreenHandler handler, @Nullable ScreenHandlerType<PlayerScreenHandler> type) {
        if (handler == null) return;
        GuiModifier.cleanupSlotCustomizations(c, handler.slots);
    }
}
