/**
 * 
 */
package com.movile.labuenavida.portal.processor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compera.portal.parser.bean.Item;
import com.compera.portal.processor.Processor;
import com.compera.portal.processor.result.ProcessorResult;
import com.compera.portal.processor.result.SuccessResult;
import com.movile.labuenavida.enums.MessageKey;
import com.movile.labuenavida.exception.LaBuenaVidaException;
import com.movile.labuenavida.util.MovileSdkHolder;
import com.movile.sdk.services.chub.ChubClient;
import com.movile.sdk.services.chub.model.ResourceListResponse;
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

            ChubClient chubClient = MovileSdkHolder.getcHubClient();
            ResourceListResponse resources = chubClient.listResource(subscription.getApplicationId(), renewCount.intValue());

            if (resources != null) {
                request.setAttribute("videos", resources.getResources());
                request.setAttribute("videoActual", resources.getResources().get(0));
            }

        } catch (LaBuenaVidaException e) {
            SYSTEM_LOGGER.error("Error procesando el video: {}", e.getMessage());
        } catch (Exception e) {
            EXCEPTION_LOGGER.error("Error en el sistema: {}", e.getMessage());
        }

        return new SuccessResult();
    }

}
