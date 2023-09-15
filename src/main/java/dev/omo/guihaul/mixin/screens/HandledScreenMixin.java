package dev.omo.guihaul.mixin.screens;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.omo.guihaul.GuiHaulMod;
import dev.omo.guihaul.access.SlotAccessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T> {
    @Shadow protected abstract boolean isPointOverSlot(Slot slot, double pointX, double pointY);

    @Shadow protected int x;
    @Shadow protected int y;
    @Shadow protected int backgroundWidth;
    @Shadow protected int backgroundHeight;
    @Unique private int mouseX;
    @Unique private int mouseY;

    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void captureMousePos(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlotHighlight(Lnet/minecraft/client/gui/DrawContext;III)V"))
    private boolean cancelSlotHoverOverlay(DrawContext ctx, int a, int b, int c) {
        return !GuiHaulMod.SHOW_SLOT_IDS;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V", shift = At.Shift.AFTER))
    private void drawDebugBounds(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (GuiHaulMod.SHOW_SLOT_IDS) {
            context.drawHorizontalLine(x, x + backgroundWidth, y, 0xFF00FF00);
            context.drawHorizontalLine(x, x + backgroundWidth, y + backgroundHeight, 0xFF00FF00);
            context.drawVerticalLine(x, y, y + backgroundHeight, 0xFF00FF00);
            context.drawVerticalLine(x + backgroundWidth, y, y + backgroundHeight, 0xFF00FF00);

            context.drawText(this.textRenderer, "Mouse abs: " + mouseX + ", " + mouseY, 0, 0, 0xFFFFFFFF, false);
            context.drawText(this.textRenderer, "Mouse pos: " + (mouseX-x) + ", " + (mouseY-y), 0, this.textRenderer.fontHeight+2, 0xFFFFFFFF, false);
        }
    }

    @Inject(method = "drawSlot", at = @At("TAIL"))
    private void showSlotOverlay(DrawContext context, Slot slot, CallbackInfo ci) {
        if (GuiHaulMod.SHOW_SLOT_IDS) {
            context.getMatrices().push();
            context.getMatrices().translate(0, 0, 1000);

            int i = slot.x;
            int j = slot.y;

            boolean hovered = isPointOverSlot(slot, this.mouseX, this.mouseY);

            context.fill(i, j, i + 16, j + 16, hovered ? 0xA0FF2323 : 0xA0E8E803);
            context.drawText(this.textRenderer, String.valueOf(slot.id), i, j, 0xFFFFFFFF, false);

            if (hovered) {
                context.drawTooltip(this.textRenderer, getSlotDebug(slot), mouseX - x, mouseY - y);
            }

            context.getMatrices().pop();
        }
    }

    @Unique
    private List<Text> getSlotDebug(Slot slot) {
        List<Text> list = new ArrayList<>();

        SlotAccessor sa = SlotAccessor.get(slot);
        sa.guihaul$storeState();
        list.add(Text.of("Slot " + slot.id));
        list.add(Text.of("x: " + slot.x));
        list.add(Text.of("y: " + slot.y));

        return list;
    }
}
