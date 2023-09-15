package dev.omo.guihaul.mixin;

import dev.omo.guihaul.GuiHaulMod;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "processF3", at = @At("RETURN"))
    public void addDebugStuff(int key, final CallbackInfoReturnable<Boolean> cir) {
        if (key == 81) {
            this.client.inGameHud.getChatHud().addMessage(Text.literal(""));
            this.client.inGameHud.getChatHud().addMessage(Text.translatable("debug.guihaul.show_slot_ids.help"));
        }
    }

    @Inject(method = "processF3", at = @At("HEAD"), cancellable = true)
    public void processF3(int key, final CallbackInfoReturnable<Boolean> cir) {
        if (key == GLFW.GLFW_KEY_E) {
            GuiHaulMod.SHOW_SLOT_IDS = !GuiHaulMod.SHOW_SLOT_IDS;
            this.client.inGameHud.getChatHud().addMessage(Text.translatable("debug.guihaul.show_slot_ids.toggle"));
            cir.setReturnValue(true);
        }
    }
}
