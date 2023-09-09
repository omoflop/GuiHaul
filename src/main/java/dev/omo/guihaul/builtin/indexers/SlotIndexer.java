package dev.omo.guihaul.builtin.indexers;

import dev.omo.guihaul.api.data.ScreenIndexer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

public interface SlotIndexer extends ScreenIndexer {
    DefaultedList<Slot> guiHaul$getSlots();
}
