/**
 * 
 */
package com.movile.labuenavida.videos.to;

/**
 * @author Peter Escamilla (peter.escamilla@movile.com)
 *
 */
public class VideoResourceTO {
    private String id;
    private String downloadUrl;
    private String previewImg;
    private String name;
    private String description;
    private String seasonCategoryId;
    private String seasonDescription;
    /**
     * 
     */
    public VideoResourceTO() {
    }
    
    
    
    /**
     * <p>
     * Getter for the field <code>id</code>
     * </p>
     * @return the id
     */
    public String getId() {
        return id;
    }



    /**
     * <p>
     * Setter for the field <code>id</code>
     * </p>
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }



    /**
     * <p>
     * Getter for the field <code>downloadUrl</code>
     * </p>
     * @return the downloadUrl
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }



    /**
     * <p>
     * Setter for the field <code>downloadUrl</code>
     * </p>
     * @param downloadUrl the downloadUrl to set
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }



    /**
     * <p>
     * Getter for the field <code>previewImg</code>
     * </p>
     * @return the previewImg
     */
    public String getPreviewImg() {
        return previewImg;
    }



    /**
     * <p>
     * Setter for the field <code>previewImg</code>
     * </p>
     * @param previewImg the previewImg to set
     */
    public void setPreviewImg(String previewImg) {
        this.previewImg = previewImg;
    }



    /**
     * <p>
     * Getter for the field <code>name</code>
     * </p>
     * @return the name
     */
    public String getName() {
        return name;
    }



    /**
     * <p>
     * Setter for the field <code>name</code>
     * </p>
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }



    /**
     * <p>
     * Getter for the field <code>description</code>
     * </p>
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>
     * Setter for the field <code>description</code>
     * </p>
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the seasonCategoryId
     */
    public String getSeasonCategoryId() {
        return seasonCategoryId;
    }

    /**
     * @param seasonCategoryId the seasonCategoryId to set
     */
    public void setSeasonCategoryId(String seasonCategoryId) {
        this.seasonCategoryId = seasonCategoryId;
    }

    /**
     * @return the seasonDescription
     */
    public String getSeasonDescription() {
        return seasonDescription;
    }

    /**
     * @param seasonDescription the seasonDescription to set
     */
    public void setSeasonDescription(String seasonDescription) {
        this.seasonDescription = seasonDescription;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((downloadUrl == null) ? 0 : downloadUrl.hashCode());
        result = prime * result + ((previewImg == null) ? 0 : previewImg.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VideoResourceTO other = (VideoResourceTO) obj;
        if (downloadUrl == null) {
            if (other.downloadUrl != null)
                return false;
        } else if (!downloadUrl.equals(other.downloadUrl))
            return false;
        if (previewImg == null) {
            if (other.previewImg != null)
                return false;
        } else if (!previewImg.equals(other.previewImg))
            return false;
        return true;
    }
}
