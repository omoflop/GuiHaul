package dev.omo.guihaul.builtin.modifiers;

import dev.omo.guihaul.access.SlotAccessor;
import dev.omo.guihaul.api.data.HaulModifier;
import dev.omo.guihaul.api.Property;
import dev.omo.guihaul.api.PropertyHolder;
import dev.omo.guihaul.builtin.indexers.SlotIndexer;
import dev.omo.guihaul.util.IntegerRange;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

import static dev.omo.guihaul.api.SharedProperties.*;

public class SlotRangeModifier extends HaulModifier<SlotIndexer> {
    public static final Property<IntegerRange> RANGE = Property.of("range", IntegerRange.class);
    public static final Property<Integer> SPACING_X = Property.of("spacing_x", int.class);
    public static final Property<Integer> SPACING_Y = Property.of("spacing_y", int.class);

    public SlotRangeModifier() {
        super(SlotIndexer.class);
    }

    @Override
    public void appendProperties(PropertyHolder.Builder builder) {
        builder.add(X, 0);
        builder.add(Y, 0);
        builder.add(SlotModifier.WIDTH, 16);
        builder.add(SlotModifier.HEIGHT, 16);
        builder.add(SPACING_X, 1);
        builder.add(SPACING_Y, 1);
        builder.add(ABSOLUTE, false);
        builder.add(RANGE);
    }

    @Override
    protected void modifyScreen(PropertyHolder holder, SlotIndexer screen) {
        IntegerRange range = holder.getProperty(RANGE);
        DefaultedList<Slot> slots = screen.guiHaul$getSlots();

        int minX = 0;
        int minY = 0;
        for (int slotIndex : range) {
            if (slotIndex < 0 || slotIndex >= slots.size()) continue;
            Slot slot = slots.get(slotIndex);
            minX = Math.min(minX, slot.x);
            minY = Math.min(minY, slot.y);
        }

        // TODO implement spacing logic
        int index = 0;
        for (int slotIndex : range) {
            if (slotIndex < 0 || slotIndex >= slots.size()) continue;
            Slot slot = slots.get(slotIndex);
            SlotAccessor sa = SlotAccessor.get(slot);

            int x = holder.getProperty(X);
            int y = holder.getProperty(Y);

            if (!holder.getProperty(ABSOLUTE)) {
                x += slot.x;
                y += slot.y;
            }

            sa.guihaul$setX(x);
            sa.guihaul$setY(y);

            index++;
        }
    }
}
