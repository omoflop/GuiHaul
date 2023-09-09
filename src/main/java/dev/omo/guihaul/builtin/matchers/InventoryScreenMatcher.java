package dev.omo.guihaul.builtin.matchers;

import dev.omo.guihaul.api.ScreenMatcher;
import dev.omo.guihaul.api.WikiDesc;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.util.Identifier;

@WikiDesc("The survival mode inventory screen")
public class InventoryScreenMatcher implements ScreenMatcher {
    @Override
    public Identifier getScreenId() {
        return new Identifier("minecraft:inventory");
    }

    @Override
    public boolean applicableTo(Screen screen) {
        return screen instanceof InventoryScreen;
    }

    @Override
    public Class<?>[] getSupportedScreens() {
        return new Class[] { InventoryScreen.class };
    }
}
