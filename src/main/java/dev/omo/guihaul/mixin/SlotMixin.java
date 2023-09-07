package dev.omo.guihaul.mixin;

import dev.omo.guihaul.access.SlotAccessor;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.*;

@Mixin(Slot.class)
public class SlotMixin implements SlotAccessor {
    @Mutable @Shadow @Final public int x;
    @Mutable @Shadow @Final public int y;

    @Override
    public void guihaul$setX(int x) {
        this.x = x;
    }

    @Override
    public void guihaul$setY(int y) {
        this.y = y;
    }
}
