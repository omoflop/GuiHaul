package dev.omo.guihaul;

import dev.omo.guihaul.api.HaulApi;
import dev.omo.guihaul.builtin.conditions.ContainerNameCondition;
import dev.omo.guihaul.builtin.conditions.ModLoadedCondition;
import dev.omo.guihaul.builtin.modifiers.SlotModifier;
import dev.omo.guihaul.entry.HaulApiEntrypoint;
import dev.omo.guihaul.ref.BuiltInConditionTypes;
import dev.omo.guihaul.ref.BuiltinElementTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiHaulMod implements ModInitializer, HaulApiEntrypoint {
	public static Identifier getId(String path) {
		return new Identifier("guihaul", path);
	}

    public static final Logger LOGGER = LoggerFactory.getLogger("guihaul");

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

		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ModResourceLoader());
		if (System.getProperty("guihaul.debug", "false").equals("true")) {
			HaulApi.printDebug();
		}

		if (System.getProperty("guihaul.generateWiki", "false").equals("true")) {
			//WikiGenerator.start();
		}
	}

	@Override
	public void onInitializeApi(HaulApi.Builder builder) {
		builder.addModifier(BuiltinElementTypes.SLOT, new SlotModifier());

		builder.putConditionType(BuiltInConditionTypes.MOD_LOADED, new ModLoadedCondition());
		builder.putConditionType(BuiltInConditionTypes.CONTAINER_NAME, new ContainerNameCondition());
	}

	@Override
	public Identifier getApiProviderName() {
		return getId("builtin");
	}
}