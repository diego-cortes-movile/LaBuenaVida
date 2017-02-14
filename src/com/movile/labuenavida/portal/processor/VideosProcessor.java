/**
 * 
 */
package com.movile.labuenavida.portal.processor;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compera.portal.parser.bean.Item;
import com.compera.portal.processor.Processor;
import com.compera.portal.processor.result.ProcessorResult;
import com.compera.portal.processor.result.SuccessResult;
import com.movile.labuenavida.bo.ChubBO;
import com.movile.labuenavida.enums.MessageKey;
import com.movile.labuenavida.exception.LaBuenaVidaException;
import com.movile.sdk.services.chub.model.Resource;
import com.movile.sdk.services.sbs.model.Subscription;

/**
 * @author Yvan Lopez (yvan.lopez@movile.com)
 *
 */
public class VideosProcessor extends Processor {

    private static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("system");
    private static final Logger EXCEPTION_LOGGER = LoggerFactory.getLogger("exception");

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

            Long renewCount = subscription.getRenewCount();

            List<Resource> allVideos = ChubBO.getInstace().getAllVideosFromAllCategories();
            SYSTEM_LOGGER.info("Todos los videos: {}", allVideos);

            Resource videoActual = this.getActualVideo(allVideos, renewCount);
            SYSTEM_LOGGER.info("Video actual: {}", videoActual);

            // request.setAttribute("videos", resources.getResources());

            request.setAttribute("videoActual", videoActual);


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
     * @param renewCount Numero de cobros hechos.
     * @return El video encontrado.
     */
    private Resource getActualVideo(List<Resource> allVideos, Long renewCount) {

        Resource video = null;

        for (Resource vid : allVideos) {
            String diaX = vid.getDescription().replace(" ","").substring(3);
            if (NumberUtils.isDigits(diaX) && renewCount == Long.parseLong(diaX)) {
                video = vid;
                break;
            }
        }
        return video;
    }

}
