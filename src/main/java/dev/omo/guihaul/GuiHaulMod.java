package dev.omo.guihaul;

import dev.omo.guihaul.api.HaulableGuis;
import dev.omo.guihaul.loading.HaulResourceLoader;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiHaulMod implements ModInitializer {
	public static Identifier getId(String path) {
		return new Identifier("guihaul", path);
	}

    public static final Logger LOGGER = LoggerFactory.getLogger("guihaul");

	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new HaulResourceLoader());
	}
}