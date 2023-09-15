package dev.omo.guihaul.builtin.modifiers;

import dev.omo.guihaul.api.PropertyHolder;
import dev.omo.guihaul.api.data.HaulModifier;
import dev.omo.guihaul.builtin.indexers.PaperDollIndexer;

import static dev.omo.guihaul.api.SharedProperties.*;

public class PaperDollModifier extends HaulModifier<PaperDollIndexer> {
    public PaperDollModifier() {
        super(PaperDollIndexer.class);
    }

    @Override
    public void appendProperties(PropertyHolder.Builder builder) {
        builder.add(X, 0);
        builder.add(Y, 0);
        builder.add(SIZE, 30);
        builder.add(VISIBLE, true);
        builder.add(ABSOLUTE, false);
    }

    @Override
    protected void modifyScreen(PropertyHolder holder, PaperDollIndexer screen) {
        boolean visible = holder.getProperty(VISIBLE);
        screen.guiHaul$setPaperDollVisible(visible);
        if (!visible) return;

        int x = holder.getProperty(X);
        int y = holder.getProperty(Y);

        if (!holder.getProperty(ABSOLUTE)) {
            x += screen.guiHaul$getPaperDollX();
            y += screen.guiHaul$getPaperDollY();
        }

        screen.guiHaul$setPaperDollPos(x, y);
        screen.guiHaul$setPaperDollSize(holder.getProperty(SIZE));
    }

    @Override
    protected void cleanupScreen(PropertyHolder holder, PaperDollIndexer screen) {
        screen.guiHaul$paperDollReset();
    }
}
