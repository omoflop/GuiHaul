package dev.omo.guihaul.mixin.screens;

import dev.omo.guihaul.access.ScreenAccessor;
import dev.omo.guihaul.api.HaulApi;
import dev.omo.guihaul.builtin.indexers.WidgetIndexer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;

@Mixin(Screen.class)
public abstract class ScreenMixin implements ScreenAccessor, WidgetIndexer {

    @Shadow @Final private List<Drawable> drawables;

    @Shadow @Final private List<Element> children;

    @Shadow @Final private List<Selectable> selectables;

    @Inject(method = "initTabNavigation", at = @At("TAIL"))
    void modifyScreenWhenReset(CallbackInfo ci) {
        HaulApi.modifyScreenInit((Screen) (Object) this);
    }

    @Override
    public Collection<Drawable> guiHaul$getDrawables() {
        return this.drawables;
    }

    @Override
    public Collection<Element> guiHaul$getElements() {
        return this.children;
    }

    @Override
    public Collection<Selectable> guiHaul$getSelectables() {
        return this.selectables;
    }
}
