package dev.omo.guihaul.builtin.modifiers;

import dev.omo.guihaul.api.PropertyHolder;
import dev.omo.guihaul.api.SharedProperties;
import dev.omo.guihaul.builtin.indexers.SlotIndexer;
import net.minecraft.screen.slot.Slot;

import java.util.function.Consumer;

public class SlotRangeModifier extends SlotModifier {

    public SlotRangeModifier() {
        super(SharedProperties.RANGE);
    }

    @Override
    public void appendProperties(PropertyHolder.Builder builder) {
        super.appendProperties(builder);
    }

    @Override
    public void modifyScreen(PropertyHolder holder, SlotIndexer screen) {
        forEachSlot(holder, screen, (slot) -> applySlot(holder, slot));
    }

    @Override
    public void cleanupScreen(PropertyHolder holder, SlotIndexer screen) {
        forEachSlot(holder, screen, SlotModifier::resetSlot);
    }

    public static void forEachSlot(PropertyHolder holder, SlotIndexer indexer, Consumer<Slot> consumer) {
        if (!holder.hasProperty(SharedProperties.RANGE)) return;
        holder.getProperty(SharedProperties.RANGE).forEach(id -> consumer.accept(indexer.getSlot(id)));
    }
}
