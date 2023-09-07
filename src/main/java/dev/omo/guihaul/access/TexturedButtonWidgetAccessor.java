package dev.omo.guihaul.access;

import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.util.Identifier;

public interface TexturedButtonWidgetAccessor {
    void guihaul$setX(int x);
    void guihaul$setY(int y);
    void guihaul$setTextureWidth(int textureWidth);
    void guihaul$setTextureHeight(int textureHeight);
    void guihaul$setWidth(int width);
    void guihaul$setHeight(int height);
    void guihaul$setU(int u);
    void guihaul$setV(int v);
    void guihaul$setTexture(Identifier texture);
    void guihaul$setAlpha(float alpha);
    void guihaul$setVisible(boolean visible);
    void guihaul$setHoveredVOffset(int vOffset);

    static TexturedButtonWidgetAccessor get(TexturedButtonWidget obj) {
        return (TexturedButtonWidgetAccessor) obj;
    }
}
