package dev.omo.guihaul.mixin.screens;

import com.llamalad7.mixinextras.sugar.Local;
import dev.omo.guihaul.duck.DrawContextAccessor;
import dev.omo.guihaul.duck.SlotAccessor;
import dev.omo.guihaul.impl.customizations.SlotCustomization;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Debug(export = true)
@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T> {
    @Shadow @Nullable protected Slot focusedSlot;

    @Shadow protected abstract boolean isPointWithinBounds(int x, int y, int width, int height, double pointX, double pointY);

    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "drawSlot", at = @At("HEAD"), cancellable = true)
    void hideSlot(DrawContext context, Slot slot, CallbackInfo ci) {
        SlotCustomization sc = SlotAccessor.getCustomization(slot);
        if (sc == null) return;

        if (!sc.enabled) ci.cancel();
    }

    @ModifyArgs(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawSprite(IIIIILnet/minecraft/client/texture/Sprite;)V"))
    void changeSlotSpriteSize(Args args, @Local Slot slot) {
        SlotCustomization sc = SlotAccessor.getCustomization(slot);
        if (sc == null) return;
        args.set(3, sc.size);
        args.set(4, sc.size);
    }

    @ModifyArgs(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
    void changeBackgroundSizeAndColor(Args args, @Local Slot slot) {
        SlotCustomization sc = SlotAccessor.getCustomization(slot);
        if (sc == null) return;
        args.set(2, sc.size);
        args.set(3, sc.size);
        args.set(4, sc.highlightColor);
    }

    @Inject(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawItem(Lnet/minecraft/item/ItemStack;III)V", shift = At.Shift.BEFORE))
    void changeItemSize(DrawContext context, Slot slot, CallbackInfo ci) {
        SlotCustomization sc = SlotAccessor.getCustomization(slot);
        if (sc == null) return;

        if (sc.size != 16) {
            DrawContextAccessor.setNextSlotSize(context, sc.size);
        }
    }

    @Inject(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", shift = At.Shift.AFTER))
    void postChangeItemSize(DrawContext context, Slot slot, CallbackInfo ci) {
        DrawContextAccessor.setNextSlotSize(context, 0);
    }

    @Unique private static SlotCustomization guihaul$slotHighlightCtx;
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlotHighlight(Lnet/minecraft/client/gui/DrawContext;III)V"))
    void captureSlotHighlightContext(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        SlotCustomization sc = SlotAccessor.getCustomization(this.focusedSlot);
        if (sc == null) return;
        guihaul$slotHighlightCtx = sc;
    }

    @Inject(method = "drawSlotHighlight", at = @At("HEAD"), cancellable = true)
    private static void changeSlotHighlight(DrawContext context, int x, int y, int z, CallbackInfo ci) {
        if (guihaul$slotHighlightCtx == null) return;
        context.fillGradient(RenderLayer.getGuiOverlay(), x, y, x + guihaul$slotHighlightCtx.size, y + guihaul$slotHighlightCtx.size, guihaul$slotHighlightCtx.highlightColor, guihaul$slotHighlightCtx.highlightColor, z);
        guihaul$slotHighlightCtx = null;
        ci.cancel();
    }

    @Inject(method = "isPointOverSlot", at = @At("HEAD"), cancellable = true)
    void changeSlotSizeDetection(Slot slot, double pointX, double pointY, CallbackInfoReturnable<Boolean> cir) {
        SlotCustomization sc = SlotAccessor.getCustomization(slot);
        if (sc == null) return;
        if (!sc.enabled) {
            cir.setReturnValue(false);
            return;
        }
        cir.setReturnValue(this.isPointWithinBounds(slot.x, slot.y, sc.size, sc.size, pointX, pointY));
    }
}
