package dev.omo.guihaul.mixin.screens;

import dev.omo.guihaul.access.ScreenAccessor;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Screen.class)
public class ScreenMixin implements ScreenAccessor {

}
