package dev.omo.guihaul.builtin.indexers;

import dev.omo.guihaul.api.data.ScreenIndexer;
import net.minecraft.util.Identifier;

public interface ScreenBackgroundIndexer extends ScreenIndexer {
    void guiHaul$setBgTexture(Identifier texture);
    void guiHaul$setBgUv(int u, int v);
    void guiHaul$setBgPos(int x, int y);
    int guiHaul$getBgX();
    int guiHaul$getBgY();
    void guiHaul$reset();
}
