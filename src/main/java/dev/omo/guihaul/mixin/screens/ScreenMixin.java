package dev.omo.guihaul.mixin.screens;

import dev.omo.guihaul.data.CustomizationHolder;
import dev.omo.guihaul.duck.ScreenAccessor;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Screen.class)
public class ScreenMixin implements ScreenAccessor {

    @Unique
    private @Nullable CustomizationHolder guihaul$customizations;

    @Override
    public void guihaul$setCustomizations(CustomizationHolder c) {
        guihaul$customizations = c;
    }

    @Override
    public @Nullable CustomizationHolder guihaul$getCustomizations() {
        return guihaul$customizations;
    }
}
