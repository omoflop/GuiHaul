package dev.omo.guihaul.impl;

import dev.omo.guihaul.impl.modifier.SurvivalInventoryGuiModifier;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.util.Identifier;

import static dev.omo.guihaul.api.HaulableGuis.register;
import static dev.omo.guihaul.impl.VanillaGuiModifiers.*;

public final class VanillaGuiTypes {

    public static final Identifier survival_inventory = new Identifier("minecraft:inventory_survival");
    public static final Identifier inventory_creative = new Identifier("minecraft:inventory_creative");
    public static final Identifier furnace = new Identifier("minecraft:furnace");
    public static final Identifier blast_furnace = new Identifier("minecraft:blast_furnace");
    public static final Identifier smoker = new Identifier("minecraft:smoker");
    public static final Identifier brewing_stand = new Identifier("minecraft:brewing_stand");
    public static final Identifier enchanting_table = new Identifier("minecraft:enchanting_table");
    public static final Identifier beacon = new Identifier("minecraft:beacon");
    public static final Identifier anvil = new Identifier("minecraft:anvil");
    public static final Identifier generic_container = new Identifier("minecraft:generic_container");
    public static final Identifier crafting_table = new Identifier("minecraft:crafting_table");
    public static final Identifier hopper = new Identifier("minecraft:hopper");
    public static final Identifier horse = new Identifier("minecraft:horse");
    public static final Identifier shulker_box = new Identifier("minecraft:shulker_box");
    public static final Identifier loom = new Identifier("minecraft:loom");
    public static final Identifier cartography_table = new Identifier("minecraft:cartography_table");
    public static final Identifier grindstone = new Identifier("minecraft:grindstone");
    public static final Identifier lectern = new Identifier("minecraft:lectern");
    public static final Identifier merchant = new Identifier("minecraft:merchant");
    public static final Identifier smithing_table = new Identifier("minecraft:smithing_table");
    public static final Identifier stonecutter = new Identifier("minecraft:stonecutter");

    public static void load() {
        register(survival_inventory,    InventoryScreen.class, new SurvivalInventoryGuiModifier(),  slots, paperDoll, recipeBook, guiTexture);
        register(inventory_creative,    CreativeInventoryScreen.class,                              slots);
        register(furnace,               FurnaceScreen.class,                                        slots);
        register(blast_furnace,         BlastFurnaceScreen.class,                                   slots);
        register(smoker,                SmokerScreen.class,                                         slots);
        register(brewing_stand,         BrewingStandScreen.class,                                   slots);
        register(enchanting_table,      EnchantmentScreen.class,                                    slots);
        register(beacon,                BeaconScreen.class,                                         slots);
        register(anvil,                 AnvilScreen.class,                                          slots);
        register(generic_container,     GenericContainerScreen.class,                               slots);
        register(crafting_table,        CraftingScreen.class,                                       slots);
        register(hopper,                HopperScreen.class,                                         slots);
        register(horse,                 HorseScreen.class,                                          slots);
        register(shulker_box,           ShulkerBoxScreen.class,                                     slots);
        register(loom,                  LoomScreen.class,                                           slots);
        register(cartography_table,     CartographyTableScreen.class,                               slots);
        register(grindstone,            GrindstoneScreen.class,                                     slots);
        register(lectern,               LecternScreen.class,                                        slots);
        register(merchant,              MerchantScreen.class,                                       slots);
        register(smithing_table,        SmithingScreen.class,                                       slots);
        register(stonecutter,           StonecutterScreen.class,                                    slots);
    }

}
