package dev.omo.guihaul.mixin.screens.inventory;

import dev.omo.guihaul.builtin.indexers.ScreenBackgroundIndexer;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(InventoryScreen.class)
public abstract class ScreenBackgroundIndexerImpl extends AbstractInventoryScreen<PlayerScreenHandler> implements ScreenBackgroundIndexer {
    @Unique private Identifier textureOverride;
    @Unique private int uOverride;
    @Unique private int vOverride;
    @Unique private int xOverride;
    @Unique private int yOverride;
    @Unique private boolean posEdited;
    @Unique private boolean uvEdited;

    public ScreenBackgroundIndexerImpl(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @ModifyArgs(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"))
    private void modifyBackground(Args args) {
        if (textureOverride != null) args.set(0, textureOverride);
        if (posEdited) {
            args.set(1, xOverride);
            args.set(2, yOverride);
        }
        if (uvEdited) {
            args.set(3, uOverride);
            args.set(4, vOverride);
        }
    }

    @Override
    public void guiHaul$setBgTexture(Identifier texture) {
        textureOverride = texture;
    }

    @Override
    public void guiHaul$setBgUv(int u, int v) {
        uvEdited = true;
        uOverride = u;
        vOverride = v;
    }

    @Override
    public void guiHaul$setBgPos(int x, int y) {
        posEdited = true;
        xOverride = x;
        yOverride = y;
    }

    @Override
    public int guiHaul$getBgX() {
        return posEdited ? xOverride : this.x;
    }

    @Override
    public int guiHaul$getBgY() {
        return posEdited ? yOverride : this.y;
    }

    @Override
    public void guiHaul$reset() {
        xOverride = 0;
        yOverride = 0;
        uOverride = 0;
        vOverride = 0;
        textureOverride = null;
        posEdited = false;
        uvEdited = false;
    }
}
