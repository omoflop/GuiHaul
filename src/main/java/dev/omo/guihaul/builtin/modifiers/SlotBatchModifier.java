package dev.omo.guihaul.builtin.modifiers;

import dev.omo.guihaul.access.SlotAccessor;
import dev.omo.guihaul.api.Property;
import dev.omo.guihaul.api.PropertyHolder;
import dev.omo.guihaul.api.SharedProperties;
import dev.omo.guihaul.api.data.HaulModifier;
import dev.omo.guihaul.builtin.indexers.SlotIndexer;

import java.util.concurrent.atomic.AtomicInteger;

import static dev.omo.guihaul.api.SharedProperties.*;
import static dev.omo.guihaul.builtin.modifiers.SlotRangeModifier.forEachSlot;

public class SlotBatchModifier extends HaulModifier<SlotIndexer> {
    public static final Property<Integer> X_SPACING = Property.of("x_spacing", int.class);
    public static final Property<Integer> Y_SPACING = Property.of("y_spacing", int.class);
    public static final Property<Integer> SLOTS_PER_ROW = Property.of("slots_per_row", int.class);


    public SlotBatchModifier() {
        super(SlotIndexer.class);
    }

    @Override
    public void appendProperties(PropertyHolder.Builder builder) {
        builder.add(X_SPACING, 2);
        builder.add(Y_SPACING, 2);
        builder.add(SLOTS_PER_ROW, 9);
        builder.add(X, 0);
        builder.add(Y, 0);
        builder.add(WIDTH, 16);
        builder.add(HEIGHT, 16);
        builder.add(RANGE);
        builder.add(ABSOLUTE, false);
    }

    @Override
    public void modifyScreen(PropertyHolder holder, SlotIndexer screen) {
        int x = holder.getProperty(SharedProperties.X);
        int y = holder.getProperty(SharedProperties.Y);
        boolean absolute = holder.getProperty(SharedProperties.ABSOLUTE);
        int width = holder.getProperty(SharedProperties.WIDTH);
        int height = holder.getProperty(SharedProperties.HEIGHT);
        int xSpacing = holder.getProperty(X_SPACING);
        int ySpacing = holder.getProperty(Y_SPACING);
        int slotsPerRow = holder.getProperty(SLOTS_PER_ROW);

        AtomicInteger xOff = new AtomicInteger(0);
        AtomicInteger yOff = new AtomicInteger(0);
        // When using relative positioning, base it on the top-left-most slot in the range
        if (!absolute) {
            forEachSlot(holder, screen, (slot) -> {
                xOff.set(Math.min(xOff.get(), slot.x));
                yOff.set(Math.min(yOff.get(), slot.y));
            });
        }

        AtomicInteger index = new AtomicInteger();
        forEachSlot(holder, screen, (slot) -> {
            SlotModifier.applySlot(holder, slot);
            SlotAccessor sa = SlotAccessor.get(slot);
            sa.guihaul$storeState();
            int i = index.get();

            sa.guihaul$setX(x + xOff.get() + ((i % slotsPerRow)*(width + xSpacing)));
            sa.guihaul$setY(y + yOff.get() + (((int)Math.floor((double)i / slotsPerRow))*(height + ySpacing)));

            index.addAndGet(1);
        });
    }

    @Override
    public void cleanupScreen(PropertyHolder holder, SlotIndexer screen) {
        forEachSlot(holder, screen, SlotModifier::resetSlot);
    }
}
