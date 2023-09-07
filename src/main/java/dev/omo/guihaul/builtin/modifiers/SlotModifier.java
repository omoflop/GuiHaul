package dev.omo.guihaul.builtin.modifiers;

import dev.omo.guihaul.access.SlotAccessor;
import dev.omo.guihaul.api.data.HaulModifier;
import dev.omo.guihaul.api.Property;
import dev.omo.guihaul.api.PropertyHolder;
import dev.omo.guihaul.builtin.indexers.SlotIndexer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.slot.Slot;

import static dev.omo.guihaul.api.SharedProperties.*;

public class SlotModifier extends HaulModifier<SlotIndexer> {
    public static final Property<Integer> SLOT_ID = Property.of("slot_id", Integer.class);
    public static final Property<Integer> WIDTH = Property.of("width", Integer.class);
    public static final Property<Integer> HEIGHT = Property.of("height", Integer.class);

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
        Slot slot = screen.getSlots().get(holder.getProperty(SLOT_ID));
        SlotAccessor sa = SlotAccessor.get(slot);

        int x = holder.getProperty(X);
        int y = holder.getProperty(Y);

        if (!holder.getProperty(ABSOLUTE)) {
            x += slot.x;
            y += slot.y;
        }

        sa.guihaul$setX(x);
        sa.guihaul$setY(y);
    }
}