package dev.omo.guihaul.mixin;

import dev.omo.guihaul.access.ScreenHandlerAccessor;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin implements ScreenHandlerAccessor {
    @Shadow @Final public DefaultedList<Slot> slots;

    @Mutable @Shadow @Final @Nullable private ScreenHandlerType<?> type;

    @Override
    public DefaultedList<Slot> guihaul$getSlots() {
        return this.slots;
    }

    @Override
    public ScreenHandlerType<?> guihaul$getType() {
        return type;
    }

    @Override
    public void guihaul$setType(ScreenHandlerType<?> type) {
        this.type = type;
    }
}
