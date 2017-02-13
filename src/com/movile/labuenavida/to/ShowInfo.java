package com.movile.labuenavida.to;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;

public class ShowInfo {

    private List<SeasonInfo> seasons = new ArrayList<SeasonInfo>();
    private SeasonInfo currentSeason;

    public void addSeason(SeasonInfo seasonInfo) {
        seasons.add(seasonInfo);
    }

    public List<SeasonInfo> getSeasons() {
        return seasons;
    }

    public SeasonInfo getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(SeasonInfo currentSeason) {
        this.currentSeason = currentSeason;
    }

    public SeasonInfo getSeasonByEpisodesCategoryId(String categoryId) {
        if (StringUtils.isNotBlank(categoryId) && CollectionUtils.isNotEmpty(seasons)) {
            for (SeasonInfo seasonInfo : seasons) {
                if (StringUtils.equals(categoryId, seasonInfo.getEpisodesCategoryId())) {
                    return seasonInfo;
                }
            }
        }
        return null;
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
