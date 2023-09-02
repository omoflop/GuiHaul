package dev.omo.guihaul.duck;

import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.util.Identifier;

public interface TexturedButtonWidgetAccessor {
    void guihaul$setup(int x, int y, int textureWidth, int textureHeight, int width, int height, int u, int v, Identifier texture);

    void guihaul$reset();

    static TexturedButtonWidgetAccessor get(TexturedButtonWidget obj) {
        return (TexturedButtonWidgetAccessor) obj;
    }
}
