package dev.omo.guihaul.mixin;

import dev.omo.guihaul.access.SlotAccessor;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.*;

@Mixin(Slot.class)
public class SlotMixin implements SlotAccessor {
    @Mutable @Shadow @Final public int x;
    @Mutable @Shadow @Final public int y;
    @Unique private int xStart;
    @Unique private int yStart;
    @Unique private boolean stateStored;

    @Override
    public void guihaul$setX(int x) {
        this.x = x;
    }

    @Override
    public void guihaul$setY(int y) {
        this.y = y;
    }

    @Override
    public void guihaul$storeState() {
        if (stateStored) return;
        xStart = x;
        yStart = y;
        stateStored = true;
    }

    @Override
    public void guihaul$restoreState() {
        if (!stateStored) return;
        x = xStart;
        y = yStart;
        stateStored = false;
    }
}
