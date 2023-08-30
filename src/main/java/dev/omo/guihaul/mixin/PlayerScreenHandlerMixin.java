package dev.omo.guihaul.mixin;

import dev.omo.guihaul.api.HaulableGuis;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin extends AbstractRecipeScreenHandler<RecipeInputInventory> {

    public PlayerScreenHandlerMixin(ScreenHandlerType<?> screenHandlerType, int i) {
        super(screenHandlerType, i);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {
        HaulableGuis.applySlots("minecraft:player_inventory", this.slots);
    }
}