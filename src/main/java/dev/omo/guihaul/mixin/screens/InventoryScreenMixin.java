package dev.omo.guihaul.mixin.screens;

import dev.omo.guihaul.data.CustomizationHolder;
import dev.omo.guihaul.duck.ScreenAccessor;
import dev.omo.guihaul.impl.VanillaGuiCustomizations;
import dev.omo.guihaul.impl.customizations.PaperDollCustomization;
import dev.omo.guihaul.impl.customizations.RecipeBookCustomization;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends Screen {

    protected InventoryScreenMixin(Text title) {
        super(title);
    }

    @ModifyArgs(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIFFLnet/minecraft/entity/LivingEntity;)V"))
    private void applyPaperDollCustomization(Args args) {
        CustomizationHolder c = ScreenAccessor.getCustomizations(this);
        if (c == null) return;
        PaperDollCustomization pdc = c.get(VanillaGuiCustomizations.paperdoll);
        if (pdc == null) return;
        int x = args.get(1);
        int y = args.get(2);

        if (pdc.absolute) {
            x = pdc.x;
            y = pdc.y;
        } else {
            x += pdc.x;
            y += pdc.y;
        }

        args.set(1, x);
        args.set(2, y);
        args.set(3, pdc.size);
    }

    @ModifyArgs(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TexturedButtonWidget;<init>(IIIIIIILnet/minecraft/util/Identifier;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)V"))
    private void changeRecipeBookButtonSize(Args args) {
        CustomizationHolder c = ScreenAccessor.getCustomizations(this);
        if (c == null) return;
        RecipeBookCustomization rbc = c.get(VanillaGuiCustomizations.recipebook);
        if (rbc == null) return;

        int x = args.get(0);
        int y = args.get(1);

        if (rbc.absolute) {
            x = rbc.x;
            y = rbc.y;
        } else {
            x += rbc.x;
            y += rbc.y;
        }

        args.set(0, x);
        args.set(1, y);
        args.set(2, rbc.width);
        args.set(3, rbc.height);
        args.set(5, rbc.height + 1);
    }

    @ModifyArgs(method = "method_19891", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;setPosition(II)V"))
    private void changeRecipeBookUpdatePos(Args args) {
        CustomizationHolder c = ScreenAccessor.getCustomizations(this);
        if (c == null) return;
        RecipeBookCustomization rbc = c.get(VanillaGuiCustomizations.recipebook);
        if (rbc == null) return;

        int x = args.get(0);
        int y = args.get(1);

        if (rbc.absolute) {
            x = rbc.x;
            y = rbc.y;
        } else {
            x += rbc.x;
            y += rbc.y;
        }

        args.set(0, x);
        args.set(1, y);
    }

    @ModifyArgs(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"))
    private void changeBackgroundTextureProperties(Args args) {
        CustomizationHolder c = ScreenAccessor.getCustomizations(this);
        if (c == null) return;
        RecipeBookCustomization rbc = c.get(VanillaGuiCustomizations.recipebook);
        if (rbc == null) return;

        if (!rbc.enabled) {
            // :)
            args.set(1, 100000);
            return;
        }

        int x = args.get(1);
        int y = args.get(2);

        if (rbc.absolute) {
            x = rbc.x;
            y = rbc.y;
        } else {
            x += rbc.x;
            y += rbc.y;
        }

        args.set(1, x);
        args.set(2, y);

        if (rbc.width > 0) args.set(5, rbc.width);
        if (rbc.height > 0) args.set(6, rbc.height);
    }
}
