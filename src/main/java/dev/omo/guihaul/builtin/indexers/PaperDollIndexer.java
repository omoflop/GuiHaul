package dev.omo.guihaul.builtin.indexers;

import dev.omo.guihaul.api.data.ScreenIndexer;

public interface PaperDollIndexer extends ScreenIndexer {
    void guiHaul$setPos(int x, int y);
    void guiHaul$setSize(int size);
    int guiHaul$getX();
    int guiHaul$getY();
    void guiHaul$setVisible(boolean enabled);
    void guiHaul$reset();
}
