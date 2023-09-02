package dev.omo.guihaul.mixin.widget;

import dev.omo.guihaul.duck.TexturedButtonWidgetAccessor;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;

@Mixin(TexturedButtonWidget.class)
public abstract class TexturedButtonWidgetMixin extends ButtonWidget implements TexturedButtonWidgetAccessor {
    @Mutable @Shadow @Final protected int textureWidth;
    @Mutable @Shadow @Final protected int textureHeight;
    @Mutable @Shadow @Final protected int u;
    @Mutable @Shadow @Final protected int v;

    @Mutable @Shadow @Final protected Identifier texture;

    @Unique private int startWidth;
    @Unique private int startHeight;
    @Unique private int startTextureWidth;
    @Unique private int startTextureHeight;
    @Unique private int startU;
    @Unique private int startV;
    @Unique private Identifier startTexture;

    @Unique private boolean capturedInitialValues;

    protected TexturedButtonWidgetMixin(int x, int y, int width, int height, Text message, PressAction onPress, NarrationSupplier narrationSupplier) {
        super(x, y, width, height, message, onPress, narrationSupplier);
    }

    @Unique private void guihaul$captureInitialValues() {
        if (capturedInitialValues) return;
        capturedInitialValues = true;
        startWidth = this.width;
        startHeight = this.height;
        startTextureWidth = this.textureWidth;
        startTextureHeight = this.textureHeight;
        startU = this.u;
        startV = this.v;
        startTexture = this.texture;
    }

    @Override
    public void guihaul$setup(int x, int y, int textureWidth, int textureHeight, int width, int height, int u, int v, Identifier texture) {
        guihaul$captureInitialValues();
        setX(x);
        setY(y);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
        this.texture = texture;
    }

    @Override
    public void guihaul$reset() {
        capturedInitialValues = false;

        this.textureWidth = startWidth;
        this.textureHeight = startHeight;
        this.u = startU;
        this.v = startV;
        this.texture = startTexture;
    }
}
