/**
 * Movile Connect > everyone
 */
package com.movile.labuenavida.util;

/**
 * Chub categories
 * @author Carlos Rodriguez
 */
public enum ChubCategory {

    SCREENSAVER((int) 1545),
    WALLPAPER((int) 1544),
    SOUND((int) 1543),
    VIDEO((int) 1104),

    UNDEFINED((int) -1),

    ;

    private final int value;

    private ChubCategory(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name();
    }

    public static ChubCategory valueOf(int type) {

        for (ChubCategory category : ChubCategory.values()) {
            if (category.value == type) {
                return category;
            }
        }

        return UNDEFINED;
    }

}
