package dev.omo.guihaul.mixin.screens;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.omo.guihaul.data.CustomizationHolder;
import dev.omo.guihaul.duck.InventoryScreenAccessor;
import dev.omo.guihaul.duck.ScreenAccessor;
import dev.omo.guihaul.duck.TexturedButtonWidgetAccessor;
import dev.omo.guihaul.impl.VanillaGuiCustomizations;
import dev.omo.guihaul.impl.customizations.GuiTextureCustomization;
import dev.omo.guihaul.impl.customizations.PaperDollCustomization;
import dev.omo.guihaul.impl.customizations.RecipeBookCustomization;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Debug(export = true)
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements InventoryScreenAccessor {
    @Shadow @Final private RecipeBookWidget recipeBook;
    @Unique private TexturedButtonWidget guihaul$recipeBook;
    @Unique private static boolean guihaul$skipNextPaperdollRender;

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }


    @ModifyArgs(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIFFLnet/minecraft/entity/LivingEntity;)V"))
    private void applyPaperDollCustomization(Args args) {
        CustomizationHolder c = ScreenAccessor.getCustomizations(this);
        if (c == null) return;
        PaperDollCustomization pdc = c.get(VanillaGuiCustomizations.paperDoll);
        if (pdc == null) return;

        if (!pdc.enabled) {
            guihaul$skipNextPaperdollRender = true;
            return;
        }

        args.set(1, pdc.getX(args.get(1)));
        args.set(2, pdc.getY(args.get(2)));
        args.set(3, pdc.size);
    }

    @Inject(method = "drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIFFLnet/minecraft/entity/LivingEntity;)V", at = @At("HEAD"), cancellable = true)
    private static void possiblySkipPaperDollRender(DrawContext context, int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, CallbackInfo ci) {
        if (guihaul$skipNextPaperdollRender) {
            guihaul$skipNextPaperdollRender = false;
            ci.cancel();
        }
    }

    @ModifyArgs(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"))
    private void captureRecipeBook(Args args) {
        guihaul$recipeBook = args.get(0);
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;", shift = At.Shift.AFTER))
    private void changeRecipeBookButtonSize(CallbackInfo ci) {
        if (guihaul$recipeBook == null) return;
        CustomizationHolder c = ScreenAccessor.getCustomizations(this);
        if (c == null) return;
        RecipeBookCustomization rbc = c.get(VanillaGuiCustomizations.recipeBook);
        if (rbc == null) return;

        TexturedButtonWidgetAccessor tba = TexturedButtonWidgetAccessor.get(guihaul$recipeBook);

        tba.guihaul$setup(rbc.getX(guihaul$recipeBook.getX()), rbc.getY(guihaul$recipeBook.getY()), rbc.textureWidth, rbc.textureHeight, rbc.width, rbc.height, rbc.u, rbc.v, rbc.texture);
    }

    @ModifyArgs(method = "method_19891", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;setPosition(II)V"))
    private void changeRecipeBookUpdatePos(Args args) {
        CustomizationHolder c = ScreenAccessor.getCustomizations(this);
        if (c == null) return;
        RecipeBookCustomization rbc = c.get(VanillaGuiCustomizations.recipeBook);
        if (rbc == null) return;

        args.set(0, rbc.getX(args.get(0)));
        args.set(1, rbc.getY(args.get(1)));
    }

    @Redirect(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"))
    private void changeBackground(DrawContext ctx, Identifier texture, int x, int y, int u, int v, int width, int height) {
        CustomizationHolder c = ScreenAccessor.getCustomizations(this);
        if (c == null) return;
        GuiTextureCustomization gtc = c.get(VanillaGuiCustomizations.guiTexture);
        if (gtc == null || !gtc.enabled) return;

        ctx.drawTexture(texture, gtc.getX(x), gtc.getY(y), gtc.u, gtc.v, gtc.width, gtc.height, gtc.textureWidth, gtc.textureHeight);
    }

    @ModifyExpressionValue(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;findLeftEdge(II)I"))
    private int modifyRecipeBookRecenter(int original) {
        CustomizationHolder c = ScreenAccessor.getCustomizations(this);
        if (c == null) return original;
        GuiTextureCustomization gtc = c.get(VanillaGuiCustomizations.guiTexture);
        if (gtc == null || !gtc.enabled) return original;

        if (gtc.shiftForRecipeBook) {
            return recipeBook.findLeftEdge(gtc.width, gtc.textureWidth);
        } else {
            return this.x;
        }
    }

    @ModifyExpressionValue(method = "method_19891", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;findLeftEdge(II)I"))
    private int modifyRecipeBookRecenterAgain(int original) {
        CustomizationHolder c = ScreenAccessor.getCustomizations(this);
        if (c == null) return original;
        GuiTextureCustomization gtc = c.get(VanillaGuiCustomizations.guiTexture);
        if (gtc == null || !gtc.enabled) return original;

        if (gtc.shiftForRecipeBook) {
            return recipeBook.findLeftEdge(gtc.width, gtc.textureWidth);
        } else {
            return this.x;
        }
    }

    @ModifyArgs(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;initialize(IILnet/minecraft/client/MinecraftClient;ZLnet/minecraft/screen/AbstractRecipeScreenHandler;)V"))
    private void modifyRecipeBookInit(Args args) {
        CustomizationHolder c = ScreenAccessor.getCustomizations(this);
        if (c == null) return;
        GuiTextureCustomization gtc = c.get(VanillaGuiCustomizations.guiTexture);
        if (gtc == null || !gtc.enabled) return;
        args.set(0, gtc.width);
        args.set(1, gtc.height);
    }

    @Inject(method = "isClickOutsideBounds", at = @At("HEAD"), cancellable = true)
    private void modifyOOB(double mouseX, double mouseY, int left, int top, int button, CallbackInfoReturnable<Boolean> cir) {
        CustomizationHolder c = ScreenAccessor.getCustomizations(this);
        if (c == null) return;
        GuiTextureCustomization gtc = c.get(VanillaGuiCustomizations.guiTexture);
        if (gtc == null || !gtc.enabled) return;
        boolean bl = mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + gtc.textureWidth) || mouseY >= (double)(top + gtc.textureHeight);
        cir.setReturnValue(this.recipeBook.isClickOutsideBounds(mouseX, mouseY, this.x, this.y, gtc.textureWidth, gtc.textureHeight, button) && bl);
    }

    @Override
    public void guihaul$cleanup() {
        TexturedButtonWidgetAccessor.get(guihaul$recipeBook).guihaul$reset();
    }
}
