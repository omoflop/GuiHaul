package dev.omo.guihaul.util;

public enum GuiAlignment {
    none(false, false, false, false),
    top_left(true, false, true, false),
    top(true, false, false, false),
    top_right(true, false, false, true),
    left(false, false, true, false),
    center(false, false, false, false),
    right(false, false, false, true),
    bottom_left(false, true, true, false),
    bottom(false, true, false, false),
    bottom_right(false, true, false, false);
    
    public final boolean isUp;
    public final boolean isDown;
    public final boolean isLeft;
    public final boolean isRight;

    GuiAlignment(boolean up, boolean down, boolean left, boolean right) {
        this.isUp = up;
        this.isDown = down;
        this.isLeft = left;
        this.isRight = right;
    }
}
