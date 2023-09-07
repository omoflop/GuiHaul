package dev.omo.guihaul.mixin;

import dev.omo.guihaul.access.DrawContextAccessor;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(DrawContext.class)
public class DrawContextMixin implements DrawContextAccessor {
    @Shadow @Final private MatrixStack matrices;
    @Unique
    private int guihaul$nextSlotSize = 0;

    @ModifyArgs(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V"))
    void changeItemPos(Args args) {
        if (guihaul$nextSlotSize == 0) return;
        args.set(0, (float)args.get(0) - 8 + guihaul$nextSlotSize/2f);
        args.set(1, (float)args.get(1) - 8 + guihaul$nextSlotSize/2f);
    }

    @ModifyArgs(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V"))
    void changeItemDrawScale(Args args) {
        if (guihaul$nextSlotSize == 0) return;
        float s = (float)guihaul$nextSlotSize;
        args.setAll(s, s, s);
    }

    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I", shift = At.Shift.BEFORE))
    void modifyStackCountText(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        if (guihaul$nextSlotSize == 0) return;
        matrices.push();
        matrices.scale(guihaul$nextSlotSize/16f, guihaul$nextSlotSize/16f, guihaul$nextSlotSize/16f);
    }

    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I", shift = At.Shift.AFTER))
    void resetStackCountText(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        if (guihaul$nextSlotSize == 0) return;
        matrices.pop();
    }

    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIII)V", shift = At.Shift.BEFORE))
    void modifyItemBar(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        if (guihaul$nextSlotSize == 0) return;
        matrices.push();
        matrices.scale(guihaul$nextSlotSize/16f, guihaul$nextSlotSize/16f, guihaul$nextSlotSize/16f);
    }


    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIII)V", ordinal = 1, shift = At.Shift.AFTER))
    void resetItemBar(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        if (guihaul$nextSlotSize == 0) return;
        matrices.pop();
    }
}
