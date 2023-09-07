package dev.omo.guihaul.mixin.screens;

import dev.omo.guihaul.access.InventoryScreenAccessor;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin implements InventoryScreenAccessor {

}
