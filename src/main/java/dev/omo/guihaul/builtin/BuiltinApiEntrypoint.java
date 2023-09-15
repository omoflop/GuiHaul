package dev.omo.guihaul.builtin;

import dev.omo.guihaul.api.HaulApi;
import dev.omo.guihaul.api.WikiGenerator;
import dev.omo.guihaul.builtin.conditions.ContainerNameCondition;
import dev.omo.guihaul.builtin.conditions.ModLoadedCondition;
import dev.omo.guihaul.builtin.conditions.ServerCondition;
import dev.omo.guihaul.builtin.matchers.InventoryScreenMatcher;
import dev.omo.guihaul.builtin.modifiers.*;
import dev.omo.guihaul.entry.HaulApiEntrypoint;
import dev.omo.guihaul.util.IntegerRange;
import net.minecraft.util.Identifier;

import static dev.omo.guihaul.GuiHaulMod.getId;

public class BuiltinApiEntrypoint implements HaulApiEntrypoint {


    private static Identifier builtin(String path) {
        return new Identifier("builtin", path);
    }

    @Override
    public void onInitializeApi(HaulApi.Builder builder) {
        // ============================== MODIFIERS ============================== //
        builder.addModifier(builtin("slot"), new SlotModifier(),
                "Modifies the properties of a specified slot"
        );
        builder.addModifier(builtin("slot_range"), new SlotRangeModifier(),
                "Similar to [[Slot|Gui Modifiers#Slot]], but applies to a range of slots instead"
        );
        builder.addModifier(builtin("slot_batch"), new SlotBatchModifier(),
                "Similar to [[Slot|Gui Modifiers#Slot]], but automagically arranges the slots in a specified pattern, optionally with spacing"
        );
        builder.addModifier(builtin("recipe_book_button"), new RecipeBookButtonModifier(),
                "Modifies properties of the recipe book button"
        );
        builder.addModifier(builtin("paper_doll"), new PaperDollModifier(),
                "Modifies the position, size, and visibility of the paper doll seen in the inventory"
        );
        builder.addModifier(builtin("screen_properties"), new ScreenPropertiesModifier(),
                "Modifies the position, size, and visibility a screen and it's background size"
        );
        builder.addModifier(builtin("background"), new ScreenBackgroundModifier(),
                "Modifies the texture, uv, and position of a screen's background"
        );

        // ============================== CONDITIONS ============================== //
        builder.addCondition(builtin("mod_loaded"), new ModLoadedCondition(),
                "Tests whether a specified mod is loaded or not"
        );
        builder.addCondition(builtin("container_name"), new ContainerNameCondition(),
                "Tests for the name of the opened container using regex. Not all screens have container names"
        );
        builder.addCondition(builtin("server"), new ServerCondition(),
                "Tests for either/both the connected server's name and ip address"
        );

        // ============================== SCREENS ============================== //
        builder.addScreenMatcher(new InventoryScreenMatcher(),
                "The survival mode inventory"
        );

        // ============================== TYPE HANDLERS ============================== //
        builder.addPropertyTypeHandler(IntegerRange.class, json -> IntegerRange.parse(json.getAsString()),
                "Represents a range of numbers.",
                "`\"4..7\"` represents integers 4, 5, 6 and 7",
                "`\"1,3,5,7\"` represents integers 1, 3, 5 and 7",
                "`\"0..5,7..10\"` represents integers 0, 1, 2, 3, 4, 5, 7, 8, 9 and 10",
                "`\"5,10,15,17..20\"` represents integers 5, 10, 15, 17, 18, 19 and 20"
        );
        builder.addPropertyTypeHandler(Identifier.class, json -> new Identifier(json.getAsString()),
            "Represents a name and path used by Minecraft. Requires a name and a path, separated by a `:`",
                "`\"minecraft:dirt`\"",
                "`\"builtin:slot\"`",
                "`\"create:andesite_alloy\"`"
        );
    }

    @Override
    public Identifier getApiProviderName() {
        return getId("builtin");
    }
}
