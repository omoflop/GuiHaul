package dev.omo.guihaul.mixin;

import dev.omo.guihaul.api.HaulApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public Screen currentScreen;

    @Inject(method = "setScreen", at = @At("HEAD"))
    void cleanupOldScreen(Screen screen, CallbackInfo ci) {
        HaulApi.cleanupScreen(currentScreen);
    }

    @Inject(method = "setScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;init(Lnet/minecraft/client/MinecraftClient;II)V", shift = At.Shift.AFTER))
    void modifyNewScreenInit(Screen screen, CallbackInfo ci) {
        HaulApi.modifyScreenInit(screen);
    }

    @Inject(method = "setScreen", at = @At("TAIL"))
    void modifyNewScreen(Screen screen, CallbackInfo ci) {
        HaulApi.modifyScreen(screen);
    }
}
