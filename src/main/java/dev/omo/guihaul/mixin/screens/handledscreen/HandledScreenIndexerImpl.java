package dev.omo.guihaul.mixin.screens.handledscreen;

import dev.omo.guihaul.builtin.indexers.HandledScreenIndexer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class HandledScreenIndexerImpl<T extends ScreenHandler> implements HandledScreenIndexer {
    @Shadow protected int x;

    @Shadow protected int y;
    @Shadow protected int backgroundWidth;

    @Shadow protected int backgroundHeight;

    @Shadow @Final protected T handler;
    @Unique
    private PlayerInventory guihaul$inventory;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void captureInventory(ScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo ci) {
        this.guihaul$inventory = inventory;
    }

    @Override
    public void guiHaul$setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int guiHaul$getX() {
        return this.x;
    }

    @Override
    public int guiHaul$getY() {
        return this.y;
    }

    @Override
    public void guiHaul$setBgWidth(int bgWidth) {
        this.backgroundWidth = bgWidth;
    }

    @Override
    public void guiHaul$setBgHeight(int bgHeight) {
        this.backgroundHeight = bgHeight;
    }

    @Override
    public int guiHaul$getBgWidth() {
        return this.backgroundWidth;
    }

    @Override
    public int guiHaul$getBgHeight() {
        return this.backgroundHeight;
    }

    @Override
    public PlayerInventory guihaul$getInventory() {
        return guihaul$inventory;
    }

    @Override
    public ScreenHandlerType<?> guihaul$getType() {
        return handler.getType();
    }
}
