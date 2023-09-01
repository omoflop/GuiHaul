package dev.omo.guihaul.duck;

import dev.omo.guihaul.data.CustomizationHolder;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;

public interface ScreenAccessor {
    void guihaul$setCustomizations(CustomizationHolder c);
    @Nullable CustomizationHolder guihaul$getCustomizations();

    static @Nullable CustomizationHolder getCustomizations(Screen screen) {
        return ((ScreenAccessor) screen).guihaul$getCustomizations();
    }

    static void setCustomizations(Screen screen, @Nullable CustomizationHolder c) {
        ((ScreenAccessor) screen).guihaul$setCustomizations(c);
    }
}
