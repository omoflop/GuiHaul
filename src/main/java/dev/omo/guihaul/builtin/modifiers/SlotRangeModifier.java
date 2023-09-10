package dev.omo.guihaul.builtin.modifiers;

import dev.omo.guihaul.api.Property;
import dev.omo.guihaul.api.PropertyHolder;
import dev.omo.guihaul.api.WikiDesc;
import dev.omo.guihaul.builtin.indexers.SlotIndexer;
import dev.omo.guihaul.util.IntegerRange;
import net.minecraft.screen.slot.Slot;

import java.util.function.Consumer;

@WikiDesc("Similar to [[Slot|Gui Modifiers#Slot]], but applies to a range of slots instead")
public class SlotRangeModifier extends SlotModifier {
    public static final Property<IntegerRange> RANGE = Property.of("range", IntegerRange.class);

    public SlotRangeModifier() {
        super(RANGE);
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
        forEachSlot(holder, screen, this::resetSlot);
    }

    protected void forEachSlot(PropertyHolder holder, SlotIndexer indexer, Consumer<Slot> consumer) {
        holder.getProperty(RANGE).forEach(id -> consumer.accept(indexer.getSlot(id)));
    }
}
