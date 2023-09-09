package dev.omo.guihaul.builtin.conditions;

import dev.omo.guihaul.api.WikiDesc;
import dev.omo.guihaul.api.data.HaulCondition;
import dev.omo.guihaul.api.Property;
import dev.omo.guihaul.api.PropertyHolder;
import net.fabricmc.loader.api.FabricLoader;

@WikiDesc("Checks to see if a specific mod is present")

public class ModLoadedCondition extends HaulCondition {
    public static final Property<String> MOD_ID = Property.of("mod_id", String.class);

    @Override
    public void appendProperties(PropertyHolder.Builder builder) {
        builder.add(MOD_ID);
    }

    @Override
    protected void init(PropertyHolder holder) {
        super.init(holder);
    }

    @Override
    protected boolean passesCondition(PropertyHolder holder, Context ctx) {
        return FabricLoader.getInstance().isModLoaded(holder.getProperty(MOD_ID));
    }
}
