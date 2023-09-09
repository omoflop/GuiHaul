package dev.omo.guihaul.builtin.modifiers;

import dev.omo.guihaul.access.SlotAccessor;
import dev.omo.guihaul.api.data.HaulModifier;
import dev.omo.guihaul.api.Property;
import dev.omo.guihaul.api.PropertyHolder;
import dev.omo.guihaul.builtin.indexers.SlotIndexer;
import net.minecraft.screen.slot.Slot;

import static dev.omo.guihaul.api.SharedProperties.*;

public class SlotModifier extends HaulModifier<SlotIndexer> {
    public static final Property<Integer> SLOT_ID = Property.of("slot_id", int.class);
    public static final Property<Integer> WIDTH = Property.of("width", int.class);
    public static final Property<Integer> HEIGHT = Property.of("height", int.class);

    public SlotModifier() {
        super(SlotIndexer.class);
    }

    @Override
    public void appendProperties(PropertyHolder.Builder builder) {
        builder.add(X, 0);
        builder.add(Y, 0);
        builder.add(WIDTH, 16);
        builder.add(HEIGHT, 16);
        builder.add(ABSOLUTE, false);
        builder.add(SLOT_ID);
    }

    @Override
    protected void modifyScreen(PropertyHolder holder, SlotIndexer screen) {
        Slot slot = screen.guiHaul$getSlots().get(holder.getProperty(SLOT_ID));
        SlotAccessor sa = SlotAccessor.get(slot);
        sa.guihaul$storeState();

        int x = holder.getProperty(X);
        int y = holder.getProperty(Y);

        if (!holder.getProperty(ABSOLUTE)) {
            x += slot.x;
            y += slot.y;
        }

        sa.guihaul$setX(x);
        sa.guihaul$setY(y);
    }

    @Override
    protected void cleanupScreen(PropertyHolder holder, SlotIndexer screen) {
        Slot slot = screen.guiHaul$getSlots().get(holder.getProperty(SLOT_ID));
        SlotAccessor sa = SlotAccessor.get(slot);
        sa.guihaul$restoreState();
    }
}
