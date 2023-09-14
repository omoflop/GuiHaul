package dev.omo.guihaul.builtin.conditions;

import dev.omo.guihaul.api.data.HaulCondition;
import dev.omo.guihaul.api.Property;
import dev.omo.guihaul.api.PropertyHolder;

public class ContainerNameCondition extends HaulCondition {
    public static final Property<String> PATTERN = Property.of("pattern", String.class);

    @Override
    public void appendProperties(PropertyHolder.Builder builder) {
        builder.add(PATTERN);
    }

    @Override
    protected void init(PropertyHolder holder) {
        super.init(holder);
    }

    @Override
    protected boolean passesCondition(PropertyHolder holder, Context ctx) {
        if (ctx.containerName() == null) return false;
        return ctx.containerName().matches(holder.getProperty(PATTERN));
    }
}
