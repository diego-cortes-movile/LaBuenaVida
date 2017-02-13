/**
 * 
 */
package com.movile.labuenavida.util;

import com.movile.labuenavida.to.SeasonInfo;
import com.movile.labuenavida.to.ShowInfo;

/**
 * @author Peter Escamilla (peter.escamilla@movile.com)
 *
 */
public class Constants {
    public static final String APPLICATION_ID = "520";
    public static final ShowInfo SHOW_INFO = new ShowInfo();

    static {

        SeasonInfo fourthSeason = new SeasonInfo();
        fourthSeason.setName("CUARTA TEMPORADA | HIP&Oacute;TESIS DE UN CRIMEN");
        fourthSeason.setEpisodesCategoryId("2270");
        fourthSeason.setVideosCategoryId("2271");
        fourthSeason.setImagesChannelName("Novoa -  Imagenes - Hipotesis de un crimen");
        fourthSeason.setCuriositiesOdinKeyTemplate("curiosidades.temp.4.semana.{0}.{1}");
        SHOW_INFO.addSeason(fourthSeason);
        SHOW_INFO.setCurrentSeason(fourthSeason);

        SeasonInfo thirdSeason = new SeasonInfo();
        thirdSeason.setName("TERCERA TEMPORADA | ALTER EGO");
        thirdSeason.setEpisodesCategoryId("2097");
        thirdSeason.setEpisodesCssClass("alterego");
        thirdSeason.setExclusiveCssClass("backalterego");
        thirdSeason.setExclusiveImage("logoAlterEgo.png");
        thirdSeason.setVideosCategoryId("2096");
        thirdSeason.setImagesChannelName("Novoa - Molly Imagenes");
        SHOW_INFO.addSeason(thirdSeason);

        SeasonInfo secondSeason = new SeasonInfo();
        secondSeason.setName("SEGUNDA TEMPORADA | AN&Oacute;NIMO");
        secondSeason.setEpisodesCategoryId("1647");
        secondSeason.setEpisodesCssClass("redfra");
        secondSeason.setVideosCategoryId("1648");
        secondSeason.setImagesChannelName("RafaelImagenes");
        secondSeason.setExclusiveCssClass("backrojo");
        secondSeason.setExclusiveImage("logoAnonimo.png");
        SHOW_INFO.addSeason(secondSeason);

        SeasonInfo firstSeason = new SeasonInfo();
        firstSeason.setName("PRIMERA TEMPORADA | SON DE TAC&Oacute;N");
        firstSeason.setEpisodesCategoryId("2100");
        firstSeason.setEpisodesCssClass("sondetacon");
        firstSeason.setVideosCategoryId("2099");
        firstSeason.setImagesChannelName("Novoa - Son de tacon - Imagenes");
        firstSeason.setExclusiveCssClass("backnubes");
        firstSeason.setExclusiveImage("LogoSondetacon.png");
        SHOW_INFO.addSeason(firstSeason);
    }
}
