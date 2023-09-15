package dev.omo.guihaul.builtin.indexers;

import dev.omo.guihaul.api.data.ScreenIndexer;

public interface PaperDollIndexer extends ScreenIndexer {
    void guiHaul$setPaperDollPos(int x, int y);
    void guiHaul$setPaperDollSize(int size);
    int guiHaul$getPaperDollX();
    int guiHaul$getPaperDollY();
    void guiHaul$setPaperDollVisible(boolean enabled);
    void guiHaul$paperDollReset();
}
