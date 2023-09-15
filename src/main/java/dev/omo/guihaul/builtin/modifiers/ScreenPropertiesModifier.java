package dev.omo.guihaul.builtin.modifiers;

import dev.omo.guihaul.api.Property;
import dev.omo.guihaul.api.PropertyHolder;
import dev.omo.guihaul.api.data.HaulModifier;
import dev.omo.guihaul.builtin.indexers.HandledScreenIndexer;

import static dev.omo.guihaul.api.SharedProperties.*;

public class ScreenPropertiesModifier extends HaulModifier<HandledScreenIndexer> {
    public static final Property<Integer> BG_WIDTH = Property.of("bg_width", int.class);
    public static final Property<Integer> BG_HEIGHT = Property.of("bg_height", int.class);
    public static final Property<Boolean> RECENTER = Property.of("recenter", boolean.class);


    public ScreenPropertiesModifier() {
        super(HandledScreenIndexer.class);
    }

    @Override
    public void appendProperties(PropertyHolder.Builder builder) {
        builder.add(X, 0);
        builder.add(Y, 0);
        builder.add(BG_WIDTH, -1);
        builder.add(BG_HEIGHT, -1);
        builder.add(WIDTH, -1);
        builder.add(HEIGHT, -1);
        builder.add(VISIBLE, true);
        builder.add(ABSOLUTE, false);
        builder.add(RECENTER, true);
    }

    @Override
    protected void modifyScreen(PropertyHolder holder, HandledScreenIndexer screen) {
        int x = holder.getProperty(X);
        int y = holder.getProperty(Y);

        int bgWidth = holder.getProperty(BG_WIDTH);
        int bgHeight = holder.getProperty(BG_HEIGHT);

        if (holder.getProperty(RECENTER)) {
            if (bgWidth > 0) x = (holder.getProperty(WIDTH) - bgWidth) / 2;
            if (bgHeight > 0) y = (holder.getProperty(HEIGHT) - bgHeight) / 2;
        }

        if (!holder.getProperty(ABSOLUTE)) {
            x += screen.guiHaul$getX();
            y += screen.guiHaul$getY();
        }

        screen.guiHaul$setPos(x, y);

        if (bgWidth > 0) screen.guiHaul$setBgWidth(bgWidth);
        if (bgHeight > 0) screen.guiHaul$setBgHeight(bgHeight);
    }
}
