/**
 * 
 */
package com.movile.labuenavida.videos;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.movile.labuenavida.to.SeasonInfo;
import com.movile.labuenavida.util.Constants;
import com.movile.labuenavida.util.HttpUtil;
import com.movile.labuenavida.videos.to.VideoResourceTO;

/**
 * @author Peter Escamilla (peter.escamilla@movile.com)
 *
 */
public class VideoPublisher {

    static String resourceUrl = "http://api.videos.movile.com/api/application/{0}/resource/{1}";
    static String categoryUrl = "http://api.videos.movile.com/api/application/{0}/category/{1}";

    public static VideoResourceTO getVideo(String resource, String category, String app) {
        if (StringUtils.isEmpty(resource)) {
            return getLatestVideo(category, app);
        }
        VideoResourceTO video = null;
        String url = MessageFormat.format(resourceUrl, app, resource);
        String json = HttpUtil.doGet(url);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(json);
            String id = actualObj.path("body").path("data").path("id").getValueAsText();
            String downloadUrl = actualObj.path("body").path("data").path("downloadUrl").getValueAsText();
            String previewImg = actualObj.path("body").path("data").path("previews").path("img_590").getValueAsText();
            String name = actualObj.path("body").path("data").path("name").getValueAsText();
            String description = actualObj.path("body").path("data").path("description").getValueAsText();
            video = new VideoResourceTO();
            video.setId(id);
            video.setDownloadUrl(downloadUrl);
            video.setPreviewImg(previewImg);
            video.setName(name);
            video.setDescription(description);
        } catch (Exception e) {
            e.printStackTrace();
            video = null;
        }
        return video;
    }

    public static List <VideoResourceTO> getAllVideos(String category, String app) {
        List <VideoResourceTO> videos = new ArrayList <VideoResourceTO>();
        String url = MessageFormat.format(categoryUrl, app, category);
        String json = HttpUtil.doGet(url);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(json);
            SeasonInfo seasonInfo = Constants.SHOW_INFO.getSeasonByEpisodesCategoryId(category);
            Iterator<JsonNode> childrens = actualObj.path("body").path("children").getElements().next().path("children").getElements();
            while (childrens.hasNext()) {
                JsonNode children = childrens.next();
                String id = children.path("data").path("id").getValueAsText();
                String downloadUrl = children.path("data").path("downloadUrl").getValueAsText();
                String previewImg = children.path("data").path("previews").path("img_590").getValueAsText();
                String name = children.path("data").path("name").getValueAsText();
                String description = children.path("data").path("description").getValueAsText();
                String seasonCategoryId = actualObj.path("body").path("data").path("categoryId").getValueAsText();
                VideoResourceTO video = new VideoResourceTO();
                video.setId(id);
                video.setDownloadUrl(downloadUrl);
                video.setPreviewImg(previewImg);
                video.setName(name);
                video.setDescription(description);
                video.setSeasonCategoryId(seasonCategoryId);
                video.setSeasonDescription(seasonInfo != null ? seasonInfo.getName() : null);
                videos.add(video);
            }
        } catch (Exception e) {
            e.printStackTrace();
            videos = null;
        }
        return videos;
    }

    private static VideoResourceTO getLatestVideo(String category, String app) {
        String videoCategory = StringUtils.defaultIfBlank(category, Constants.SHOW_INFO.getCurrentSeason().getEpisodesCategoryId());
        List <VideoResourceTO> videos = getAllVideos(videoCategory, app);
        if (CollectionUtils.isNotEmpty(videos)) {
            return videos.get(0);
        }
        return null;
    }
}
