package com.movile.labuenavida.to;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SeasonInfo {

    private String name;
    private String episodesCategoryId;
    private String episodesCssClass;
    private String videosCategoryId;
    private String imagesChannelName;
    private String exclusiveImage;
    private String exclusiveCssClass;
    private String curiositiesOdinKeyTemplate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEpisodesCategoryId() {
        return episodesCategoryId;
    }

    public void setEpisodesCategoryId(String episodesCategoryId) {
        this.episodesCategoryId = episodesCategoryId;
    }

    public String getEpisodesCssClass() {
        return episodesCssClass;
    }

    public void setEpisodesCssClass(String episodesCssClass) {
        this.episodesCssClass = episodesCssClass;
    }

    public String getVideosCategoryId() {
        return videosCategoryId;
    }

    public void setVideosCategoryId(String videosCategoryId) {
        this.videosCategoryId = videosCategoryId;
    }

    public String getImagesChannelName() {
        return imagesChannelName;
    }

    public void setImagesChannelName(String imagesChannelName) {
        this.imagesChannelName = imagesChannelName;
    }

    public String getExclusiveImage() {
        return exclusiveImage;
    }

    public void setExclusiveImage(String exclusiveImage) {
        this.exclusiveImage = exclusiveImage;
    }

    public String getExclusiveCssClass() {
        return exclusiveCssClass;
    }

    public void setExclusiveCssClass(String exclusiveCssClass) {
        this.exclusiveCssClass = exclusiveCssClass;
    }

    public String getCuriositiesOdinKeyTemplate() {
        return curiositiesOdinKeyTemplate;
    }

    public void setCuriositiesOdinKeyTemplate(String curiositiesOdinKeyTemplate) {
        this.curiositiesOdinKeyTemplate = curiositiesOdinKeyTemplate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
