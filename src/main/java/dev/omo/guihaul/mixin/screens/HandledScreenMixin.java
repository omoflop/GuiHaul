package dev.omo.guihaul.mixin.screens;

import dev.omo.guihaul.access.HandledScreenAccessor;
import dev.omo.guihaul.builtin.indexers.SlotIndexer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T>, SlotIndexer, HandledScreenAccessor {
    @Shadow @Final protected T handler;
    @Unique private PlayerInventory guihaul$inventory;
    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void captureInventory(ScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo ci) {
        this.guihaul$inventory = inventory;
    }

    @Override
    public DefaultedList<Slot> guiHaul$getSlots() {
        return this.handler.slots;
    }

    @Override
    public PlayerInventory guihaul$getInventory() {
        return guihaul$inventory;
    }
}
