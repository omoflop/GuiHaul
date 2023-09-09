package dev.omo.guihaul.api;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

public interface ScreenMatcher {
    Identifier getScreenId();
    boolean applicableTo(Screen screen);
    Class<?>[] getSupportedScreens();

    class Impl implements ScreenMatcher {

        private final Identifier id;
        private final Predicate<Screen> predicate;
        private final Class<?>[] classes;

        Impl(Identifier id, Predicate<Screen> predicate, Class<?>... classes) {

            this.id = id;
            this.predicate = predicate;
            this.classes = classes;
        }

        @Override
        public Identifier getScreenId() {
            return id;
        }

        @Override
        public boolean applicableTo(Screen screen) {
            return predicate.test(screen);
        }

        @Override
        public Class<?>[] getSupportedScreens() {
            return classes;
        }
    }

    static ScreenMatcher of(Identifier id, Predicate<Screen> predicate, Class<?>... classes) {
        return new Impl(id, predicate, classes);
    }
}
