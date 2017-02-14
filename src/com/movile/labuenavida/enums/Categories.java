package com.movile.labuenavida.enums;

/**
 * Categories list.
 * 
 * @author Yvan Lopez (yvan.lopez@movile.com)
 */
public enum Categories {

    MEJORAR(2328L, "Mejorar"), SALOMON(2329L, "Salom&oacute;n"), CUIDARTE(2330L, "Cuidarte"), DISFRUTAR(2331L, "Disfrutar");

    private Long id;
    private String description;

    private Categories(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

}
