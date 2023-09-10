package dev.omo.guihaul.builtin;

import dev.omo.guihaul.api.HaulApi;
import dev.omo.guihaul.builtin.conditions.ContainerNameCondition;
import dev.omo.guihaul.builtin.conditions.ModLoadedCondition;
import dev.omo.guihaul.builtin.conditions.ServerCondition;
import dev.omo.guihaul.builtin.matchers.InventoryScreenMatcher;
import dev.omo.guihaul.builtin.modifiers.RecipeBookButtonModifier;
import dev.omo.guihaul.builtin.modifiers.SlotModifier;
import dev.omo.guihaul.builtin.modifiers.SlotRangeModifier;
import dev.omo.guihaul.entry.HaulApiEntrypoint;
import net.minecraft.util.Identifier;

import static dev.omo.guihaul.GuiHaulMod.getId;

public class BuiltinApiEntrypoint implements HaulApiEntrypoint {


    private static Identifier builtin(String path) {
        return new Identifier("builtin", path);
    }

    @Override
    public void onInitializeApi(HaulApi.Builder builder) {
        builder.addModifier(builtin("slot"), new SlotModifier());
        builder.addModifier(builtin("slot_range"), new SlotRangeModifier());
        builder.addModifier(builtin("recipe_book_button"), new RecipeBookButtonModifier());

        builder.addCondition(builtin("mod_loaded"), new ModLoadedCondition());
        builder.addCondition(builtin("container_name"), new ContainerNameCondition());
        builder.addCondition(builtin("server"), new ServerCondition());

        builder.addScreenMatcher(new InventoryScreenMatcher());
    }

    @Override
    public Identifier getApiProviderName() {
        return getId("builtin");
    }
}
