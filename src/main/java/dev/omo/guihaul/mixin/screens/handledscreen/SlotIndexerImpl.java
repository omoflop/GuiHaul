package dev.omo.guihaul.mixin.screens.handledscreen;

import dev.omo.guihaul.builtin.indexers.SlotIndexer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HandledScreen.class)
public class SlotIndexerImpl<T extends ScreenHandler> implements SlotIndexer {
    @Shadow @Final protected T handler;

    @Override
    public DefaultedList<Slot> guiHaul$getSlots() {
        return this.handler.slots;
    }
}
