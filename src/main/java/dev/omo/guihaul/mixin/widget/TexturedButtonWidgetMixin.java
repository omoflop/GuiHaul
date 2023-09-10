package dev.omo.guihaul.mixin.widget;

import dev.omo.guihaul.access.TexturedButtonWidgetAccessor;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TexturedButtonWidget.class)
public class TexturedButtonWidgetMixin extends ButtonWidget implements TexturedButtonWidgetAccessor {
    @Mutable @Shadow @Final protected int textureWidth;
    @Mutable @Shadow @Final protected int textureHeight;
    @Mutable @Shadow @Final protected int u;
    @Mutable @Shadow @Final protected int v;
    @Mutable @Shadow @Final protected Identifier texture;
    @Mutable @Shadow @Final protected int hoveredVOffset;

    protected TexturedButtonWidgetMixin(int x, int y, int width, int height, Text message, PressAction onPress, NarrationSupplier narrationSupplier) {
        super(x, y, width, height, message, onPress, narrationSupplier);
    }

    @Override
    public void guihaul$setX(int x) {
        setX(x);
    }

    @Override
    public void guihaul$setY(int y) {
        setY(y);
    }

    @Override
    public void guihaul$setTextureWidth(int textureWidth) {
        this.textureWidth = textureWidth;
    }

    @Override
    public void guihaul$setTextureHeight(int textureHeight) {
        this.textureHeight = textureHeight;
    }

    @Override
    public void guihaul$setWidth(int width) {
        this.width = width;
    }

    @Override
    public void guihaul$setHeight(int height) {
        this.height = height;
    }

    @Override
    public void guihaul$setU(int u) {
        this.u = u;
    }

    @Override
    public void guihaul$setV(int v) {
        this.v = v;
    }

    @Override
    public void guihaul$setTexture(Identifier texture) {
        this.texture = texture;
    }

    @Override
    public Identifier guihaul$getTexture() {
        return this.texture;
    }

    @Override
    public void guihaul$setAlpha(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public void guihaul$setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void guihaul$setHoveredVOffset(int vOffset) {
        this.hoveredVOffset = vOffset;
    }
}
