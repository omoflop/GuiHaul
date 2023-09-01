package dev.omo.guihaul.mixin;

import dev.omo.guihaul.impl.customizations.SlotCustomization;
import dev.omo.guihaul.duck.SlotAccessor;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.*;

@Mixin(Slot.class)
            public class SlotMixin implements SlotAccessor {
    @Mutable @Shadow @Final public int x;
    @Mutable @Shadow @Final public int y;

    @Unique private int guihaul$startX;
    @Unique private int guihaul$startY;
    @Unique private boolean guihaul$gottenStarts;

    @Unique private void guihaul$saveStarts() {
        if (guihaul$gottenStarts) return;
        guihaul$startX = x;
        guihaul$startY = y;
        guihaul$gottenStarts = true;
    }

    @Unique public SlotCustomization guihaul$customization;

    @Override
    public void guihaul$setX(int x) {
        guihaul$saveStarts();
        this.x = x;
    }

    @Override
    public void guihaul$setY(int y) {
        guihaul$saveStarts();
        this.y = y;
    }

    @Override
    public void guihaul$setCustomization(SlotCustomization customization) {
        guihaul$customization = customization;
    }

    @Override
    public SlotCustomization guihaul$getCustomization() {
        return guihaul$customization;
    }

    @Override
    public void guihaul$reset() {
        guihaul$customization = null;

        if (guihaul$gottenStarts) {
            x = guihaul$startX;
            y = guihaul$startY;
            guihaul$gottenStarts = false;
        }
    }
}
