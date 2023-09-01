package dev.omo.guihaul.mixin;

import dev.omo.guihaul.duck.ScreenHandlerAccessor;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin implements ScreenHandlerAccessor {
    @Shadow @Final public DefaultedList<Slot> slots;

    @Shadow @Final @Nullable private ScreenHandlerType<?> type;

    @Override
    public DefaultedList<Slot> guihaul$getSlots() {
        return this.slots;
    }

    @Override
    public ScreenHandlerType<?> guihaul$getType() {
        return type;
    }
}
