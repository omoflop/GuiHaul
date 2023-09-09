package dev.omo.guihaul;

import dev.omo.guihaul.api.HaulApi;
import dev.omo.guihaul.builtin.conditions.ContainerNameCondition;
import dev.omo.guihaul.builtin.conditions.ModLoadedCondition;
import dev.omo.guihaul.builtin.conditions.ServerCondition;
import dev.omo.guihaul.builtin.modifiers.SlotModifier;
import dev.omo.guihaul.builtin.modifiers.SlotRangeModifier;
import dev.omo.guihaul.entry.HaulApiEntrypoint;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiHaulMod implements ModInitializer, HaulApiEntrypoint {
	public static Identifier getId(String path) {
		return new Identifier("guihaul", path);
	}

    public static final Logger LOGGER = LoggerFactory.getLogger("guihaul");

	public static final boolean DEBUG = System.getProperty("guihaul.debug", "false").equals("true");
	public static final boolean GENERATE_WIKI = System.getProperty("guihaul.generateWiki", "false").equals("true");

	@Override
	public void onInitialize() {
		HaulApi.Builder builder = new HaulApi.Builder();
		for (HaulApiEntrypoint entrypoint : FabricLoader.getInstance().getEntrypoints("guihaul", HaulApiEntrypoint.class)) {
			try {
				entrypoint.onInitializeApi(builder);
			} catch (Exception e) {
				LOGGER.error("Failed to load entrypoint for mod {}, reason: {}", entrypoint.getApiProviderName(), e);
			}
		}

		if (DEBUG) {
			HaulApi.printApiDebug();
		}

		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ModResourceLoader());

	}

	private static Identifier builtin(String path) {
		return new Identifier("builtin", path);
	}

	@Override
	public void onInitializeApi(HaulApi.Builder builder) {
		builder.addModifier(builtin("slot"), new SlotModifier());
		builder.addModifier(builtin("slot_range"), new SlotRangeModifier());

		builder.addCondition(builtin("mod_loaded"), new ModLoadedCondition());
		builder.addCondition(builtin("container_name"), new ContainerNameCondition());
		builder.addCondition(builtin("server"), new ServerCondition());

		builder.addScreenMatcher(new Identifier("inventory"), screen -> screen instanceof InventoryScreen);
	}

	@Override
	public Identifier getApiProviderName() {
		return getId("builtin");
	}
}