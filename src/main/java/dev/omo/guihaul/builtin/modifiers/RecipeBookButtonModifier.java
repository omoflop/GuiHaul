package dev.omo.guihaul.builtin.modifiers;

import dev.omo.guihaul.access.TexturedButtonWidgetAccessor;
import dev.omo.guihaul.api.Property;
import dev.omo.guihaul.api.PropertyHolder;
import dev.omo.guihaul.api.data.HaulModifier;
import dev.omo.guihaul.builtin.indexers.WidgetIndexer;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.util.Identifier;

import static dev.omo.guihaul.api.SharedProperties.*;

public class RecipeBookButtonModifier extends HaulModifier<WidgetIndexer> {
    private static final Identifier RECIPE_BUTTON_TEXTURE = new Identifier("textures/gui/recipe_button.png");
    public static final Property<Integer> HOVERED_V_OFFSET = Property.of("hovered_v_offset", int.class);

    public RecipeBookButtonModifier() {
        super(WidgetIndexer.class);
    }

    @Override
    public void appendProperties(PropertyHolder.Builder builder) {
        builder.add(X, 0);
        builder.add(Y, 0);
        builder.add(ABSOLUTE, false);
        builder.add(VISIBLE, true);
        builder.add(TEXTURE, RECIPE_BUTTON_TEXTURE);
        builder.add(WIDTH, 20);
        builder.add(HEIGHT, 18);
        builder.add(U, 0);
        builder.add(V, 0);
        builder.add(HOVERED_V_OFFSET, 19);
    }

    @Override
    public void modifyScreenInit(PropertyHolder holder, WidgetIndexer screen) {
        TexturedButtonWidget widget = screen.findDrawable(TexturedButtonWidget.class, w -> TexturedButtonWidgetAccessor.get(w).guihaul$getTexture().equals(RECIPE_BUTTON_TEXTURE));
        if (widget == null) return;
        if (!holder.getProperty(VISIBLE)) {
            screen.guiHaul$getDrawables().remove(widget);
            return;
        }
        TexturedButtonWidgetAccessor a = TexturedButtonWidgetAccessor.get(widget);

        a.guihaul$setTexture(holder.getProperty(TEXTURE));
        widget.setWidth(holder.getProperty(WIDTH));
        a.guihaul$setHeight(holder.getProperty(HEIGHT));
        a.guihaul$setU(holder.getProperty(U));
        a.guihaul$setV(holder.getProperty(V));
        a.guihaul$setHoveredVOffset(holder.getProperty(HOVERED_V_OFFSET));

        int x = holder.getProperty(X);
        int y = holder.getProperty(Y);

        if (!holder.getProperty(ABSOLUTE)) {
            x += widget.getX();
            y += widget.getY();
        }

        widget.setPosition(x, y);
    }
}
