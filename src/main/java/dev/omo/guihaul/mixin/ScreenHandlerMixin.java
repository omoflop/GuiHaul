package dev.omo.guihaul.mixin;

import dev.omo.guihaul.duck.ScreenHandlerAccessor;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin implements ScreenHandlerAccessor {
    @Shadow @Final public DefaultedList<Slot> slots;

    @Override
    public DefaultedList<Slot> guihaul$getSlots() {
        return this.slots;
    }
}
