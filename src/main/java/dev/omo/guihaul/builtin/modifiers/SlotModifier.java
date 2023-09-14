package dev.omo.guihaul.builtin.modifiers;

import dev.omo.guihaul.access.SlotAccessor;
import dev.omo.guihaul.api.Property;
import dev.omo.guihaul.api.PropertyHolder;
import dev.omo.guihaul.api.data.HaulModifier;
import dev.omo.guihaul.builtin.indexers.SlotIndexer;
import net.minecraft.screen.slot.Slot;

import static dev.omo.guihaul.api.SharedProperties.*;

public class SlotModifier extends HaulModifier<SlotIndexer> {
    public static final Property<Integer> SLOT_ID = Property.of("slot_id", int.class);

    private final Property<?> slotIndexerProperty;
    public SlotModifier() {
        this(SLOT_ID);
    }

    public SlotModifier(Property<?> slotIndexerProperty) {
        super(SlotIndexer.class);
        this.slotIndexerProperty = slotIndexerProperty;
    }

    @Override
    public void appendProperties(PropertyHolder.Builder builder) {
        builder.add(X, 0);
        builder.add(Y, 0);
        builder.add(WIDTH, 16);
        builder.add(HEIGHT, 16);
        builder.add(ABSOLUTE, false);
        builder.add(slotIndexerProperty);
    }

    @Override
    public void modifyScreen(PropertyHolder holder, SlotIndexer screen) {
        Slot slot = screen.getSlot(holder.getProperty(SLOT_ID));
        applySlot(holder, slot);
    }

    @Override
    public void cleanupScreen(PropertyHolder holder, SlotIndexer screen) {
        Slot slot = screen.getSlot(holder.getProperty(SLOT_ID));
        resetSlot(slot);
    }

    public static void applySlot(PropertyHolder holder, Slot slot) {
        SlotAccessor sa = SlotAccessor.get(slot);
        sa.guihaul$storeState();

        if (holder.hasProperty(X, Y)) {
            int x = holder.getProperty(X);
            int y = holder.getProperty(Y);

            if (holder.hasProperty(ABSOLUTE) && !holder.getProperty(ABSOLUTE)) {
                x += slot.x;
                y += slot.y;
            }

            sa.guihaul$setX(x);
            sa.guihaul$setY(y);
        }
    }


    public static void resetSlot(Slot slot) {
        SlotAccessor sa = SlotAccessor.get(slot);
        sa.guihaul$restoreState();
    }
}
