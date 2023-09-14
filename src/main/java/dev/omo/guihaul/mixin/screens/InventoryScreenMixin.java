package dev.omo.guihaul.mixin.screens;

import dev.omo.guihaul.builtin.indexers.PaperDollIndexer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements PaperDollIndexer {
    @Unique private int xOverride;
    @Unique private int yOverride;
    @Unique private int size;
    @Unique private boolean visible;
    @Unique private boolean isPositionOverridden;
    @Unique private static boolean skipNextPaperDollRender;

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @ModifyArgs(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIFFLnet/minecraft/entity/LivingEntity;)V"))
    private void applyPaperDollCustomization(Args args) {
        if (isPositionOverridden) {
            args.set(1, xOverride);
            args.set(2, yOverride);
        }
        args.set(3, size);

        if (!visible) {
            skipNextPaperDollRender = true;
        }
    }

    @Inject(method = "drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIFFLnet/minecraft/entity/LivingEntity;)V", at = @At("HEAD"), cancellable = true)
    private static void possiblySkipPaperDollRender(DrawContext context, int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, CallbackInfo ci) {
        if (skipNextPaperDollRender) {
            skipNextPaperDollRender = false;
            ci.cancel();
        }
    }

    @Override
    public void guiHaul$setPos(int x, int y) {
        isPositionOverridden = true;
        xOverride = x;
        yOverride = y;
    }

    @Override
    public void guiHaul$setSize(int size) {
        this.size = size;
    }

    @Override
    public int guiHaul$getX() {
        if (!isPositionOverridden) return x + 51;
        return xOverride;
    }

    @Override
    public int guiHaul$getY() {
        if (!isPositionOverridden) return y + 75;
        return yOverride;
    }

    @Override
    public void guiHaul$setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void guiHaul$reset() {
        isPositionOverridden = false;
        xOverride = 0;
        yOverride = 0;
        size = 30;
        visible = true;
    }
}
