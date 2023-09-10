package dev.omo.guihaul.builtin.indexers;

import dev.omo.guihaul.api.data.ScreenIndexer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public interface WidgetIndexer extends ScreenIndexer {
    Collection<Drawable> guiHaul$getDrawables();
    Collection<Element> guiHaul$getElements();
    Collection<Selectable> guiHaul$getSelectables();
    default @Nullable <T extends Drawable>T findDrawable(Class<T> clazz, Predicate<T> test) {
        for (Drawable drawable : guiHaul$getDrawables()) {
            if (drawable.getClass() == clazz) {
                if (test.test((T)drawable)) return (T)drawable;
            }
        }
        return null;
    }
}
