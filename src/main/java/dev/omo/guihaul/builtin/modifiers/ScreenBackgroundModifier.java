package dev.omo.guihaul.builtin.modifiers;

import dev.omo.guihaul.api.PropertyHolder;
import dev.omo.guihaul.api.data.HaulModifier;
import dev.omo.guihaul.builtin.indexers.ScreenBackgroundIndexer;
import net.minecraft.util.Identifier;

import static dev.omo.guihaul.api.SharedProperties.*;

public class ScreenBackgroundModifier extends HaulModifier<ScreenBackgroundIndexer> {
    private static final Identifier EMPTY = Identifier.of("empty","empty");

    public ScreenBackgroundModifier() {
        super(ScreenBackgroundIndexer.class);
    }

    @Override
    public void appendProperties(PropertyHolder.Builder builder) {
        builder.add(TEXTURE, EMPTY);
        builder.add(U, -1);
        builder.add(V, -1);
        builder.add(X);
        builder.add(Y);
        builder.add(ABSOLUTE);
    }

    @Override
    protected void modifyScreen(PropertyHolder holder, ScreenBackgroundIndexer screen) {
        int x = holder.getProperty(X);
        int y = holder.getProperty(Y);

        if (!holder.getProperty(ABSOLUTE)) {
            x += screen.guiHaul$getBgX();
            y += screen.guiHaul$getBgY();
        }

        screen.guiHaul$setBgPos(x, y);

        int u = holder.getProperty(U);
        int v = holder.getProperty(V);
        if (u >= 0 && y >= 0) screen.guiHaul$setBgUv(u, v);

        Identifier texture = holder.getProperty(TEXTURE);
        screen.guiHaul$setBgTexture(texture.equals(EMPTY) ? null : texture);
    }

    @Override
    protected void cleanupScreen(PropertyHolder holder, ScreenBackgroundIndexer screen) {
        super.cleanupScreen(holder, screen);
    }
}
