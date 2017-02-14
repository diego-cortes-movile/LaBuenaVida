/**
 * 
 */
package com.movile.labuenavida.portal.processor;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compera.portal.parser.bean.Item;
import com.compera.portal.processor.NodeFinderProcessor;
import com.compera.portal.processor.Processor;
import com.compera.portal.processor.result.ProcessorResult;
import com.compera.portal.processor.result.SuccessResult;
import com.compera.portal.property.Property;
import com.compera.portal.tree.Tree;
import com.movile.labuenavida.bo.ChubBO;
import com.movile.labuenavida.enums.MessageKey;
import com.movile.labuenavida.exception.LaBuenaVidaException;
import com.movile.labuenavida.util.ConfigurationProvider;
import com.movile.labuenavida.util.HttpUtil;
import com.movile.labuenavida.videos.to.VideoResourceTO;
import com.movile.sdk.services.chub.model.Resource;
import com.movile.sdk.services.sbs.model.Subscription;

/**
 * @author Yvan Lopez (yvan.lopez@movile.com)
 *
 */
public class VideosProcessor extends Processor {

    private static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("system");
    private static final Logger EXCEPTION_LOGGER = LoggerFactory.getLogger("exception");
    private static final String RESOURCE_URL = "http://api.videos.movile.com/api/application/{0}/resource/{1}";

    @Override
    public void initiate(Item item) {}

    @Override
    public void terminate() {}

    @Override
    public ProcessorResult process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            Subscription subscription = (Subscription) request.getAttribute("subscription");
            if (subscription == null) {
                throw new LaBuenaVidaException(MessageKey.EMPTY_SUBSCRIPTION);
            }

            Integer sequentialIndex = subscription.getSequentialIndex();

            List<Resource> allVideos = ChubBO.getInstace().getAllVideosFromAllCategories();
            SYSTEM_LOGGER.info("Todos los videos: {}", allVideos);

            Resource videoActual = this.getActualVideo(allVideos, sequentialIndex);
            SYSTEM_LOGGER.info("Video actual: {}", videoActual);

            // request.setAttribute("videos", resources.getResources());

            String chubAppId = ConfigurationProvider.getApplicationProperties().getString("chub.application.id");
            Tree node = (Tree) request.getAttribute(NodeFinderProcessor.NODE_ATTRIBUTE);
            Property videoResolution = node.getProperty("videoResolution");
            String resolution = videoResolution != null ? videoResolution.getSimpleValue() : null;
            VideoResourceTO video = getVideo(chubAppId, videoActual.getId().toString(), resolution);
            
            request.setAttribute("videoActual", video);


        } catch (LaBuenaVidaException e) {
            SYSTEM_LOGGER.error("Error procesando el video: {}", e.getMessage());
        } catch (Exception e) {
            EXCEPTION_LOGGER.error("Error en el sistema: {}", e.getMessage());
        }

        return new SuccessResult();
    }

    /**
     * Obtiene el video actual segun el numero de cobros.
     * 
     * @param allVideos Todos los videos.
     * @param sequentialIndex Secuencia de envios realizados.
     * @return El video encontrado.
     */
    private Resource getActualVideo(List<Resource> allVideos, Integer sequentialIndex) {

        Resource video = null;

        for (Resource vid : allVideos) {
            String diaX = vid.getDescription().replace(" ","").substring(3);
            if (NumberUtils.isDigits(diaX) && sequentialIndex == Integer.parseInt(diaX)) {
                video = vid;
                break;
            }
        }
        return video;
    }

    public static VideoResourceTO getVideo(String application, String resourceId, String resolution) {
        VideoResourceTO video = null;
        String url = MessageFormat.format(RESOURCE_URL, application, resourceId);
        String json = HttpUtil.doGet(url);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(json);

            JsonNode children = actualObj.path("body");
            String id = children.path("data").path("id").getValueAsText();
            String downloadUrl = children.path("data").path("downloadUrl").getValueAsText();
            if (StringUtils.isNotBlank(resolution) && children.path("data").path("streamings") != null
                    && children.path("data").path("streamings").path(resolution) != null) {
                downloadUrl = children.path("data").path("streamings").path(resolution).getValueAsText();
            }
            String previewImg = children.path("data").path("previews").path("img_590").getValueAsText();
            String name = children.path("data").path("name").getValueAsText();
            String description = children.path("data").path("description").getValueAsText();
            String seasonCategoryId = actualObj.path("body").path("data").path("categoryId").getValueAsText();

            video = new VideoResourceTO();
            video.setId(id);
            video.setDownloadUrl(downloadUrl);
            video.setPreviewImg(previewImg);
            video.setName(name);
            video.setDescription(description);
            video.setSeasonCategoryId(seasonCategoryId);

        } catch (Exception e) {
            EXCEPTION_LOGGER.error("Error listing the videos for application=" + application + ", resourceId=" + resourceId + "", e);
            video = null;
        }
        return video;
    }
}
