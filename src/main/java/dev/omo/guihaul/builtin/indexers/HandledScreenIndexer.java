package dev.omo.guihaul.builtin.indexers;

import dev.omo.guihaul.api.data.ScreenIndexer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;

public interface HandledScreenIndexer extends ScreenIndexer {
    void guiHaul$setPos(int x, int y);
    int guiHaul$getX();
    int guiHaul$getY();

    void guiHaul$setBgWidth(int bgWidth);
    void guiHaul$setBgHeight(int bgHeight);
    int guiHaul$getBgWidth();
    int guiHaul$getBgHeight();

    PlayerInventory guihaul$getInventory();
    ScreenHandlerType<?> guihaul$getType();
}
