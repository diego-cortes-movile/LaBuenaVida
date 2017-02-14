package com.movile.labuenavida.enums;

/**
 * Categories list.
 * 
 * @author Yvan Lopez (yvan.lopez@movile.com)
 */
public enum Categories {

    MEJORAR("Mejorar"), SALOMON("Salom&oacute;n"), CUIDARTE("Cuidarte"), DISFRUTAR("Disfrutar");

    private String category;

    private Categories(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
