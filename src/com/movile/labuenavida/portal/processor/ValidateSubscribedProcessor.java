/**
 * 
 */
package com.movile.labuenavida.portal.processor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compera.portal.parser.bean.Item;
import com.compera.portal.processor.Processor;
import com.compera.portal.processor.result.ProcessorResult;
import com.compera.portal.processor.result.RedirectResult;
import com.compera.portal.processor.result.SuccessResult;
import com.movile.labuenavida.bo.SubscriptionBo;

/**
 * @author Yvan Lopez (yvan.lopez@movile.com)
 *
 */
public class ValidateSubscribedProcessor extends Processor {

    private static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("system");

    private static final String LP_LABUENAVIDA = "http://1mvl.com/lp/buenaVida/co/";

    // Usado para pruebas
    private static final Boolean IS_SUSCRIBED_TEST = true;

    @Override
    public void initiate(Item item) {}

    @Override
    public void terminate() {}

    @Override
    public ProcessorResult process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (IS_SUSCRIBED_TEST) {
            SYSTEM_LOGGER.info("Validación de prueba.");
            return new SuccessResult();
        }

        Boolean isSubscribed = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("msisdn".equals(cookie.getName()) || "user".equals(cookie.getName())) {
                    SYSTEM_LOGGER.info("Msisdn encontrado: {}", cookie.getName());
                    isSubscribed = (SubscriptionBo.getInstance().findSubscription(cookie.getName()) != null) ? true : false;
                    SYSTEM_LOGGER.info("Está suscrito en cookies?: {}", isSubscribed);
                }
            }
        }

        // TODO: procesar 'dm' y msisdn encriptado.
        if ((isSubscribed == null || !isSubscribed)
                && (StringUtils.isNotBlank(request.getParameter("msisdn")) || StringUtils.isNotBlank(request.getParameter("user")))) {

            String msisdn = StringUtils.isNotEmpty(request.getParameter("msisdn")) ? request.getParameter("msisdn") : request.getParameter("name");
            SYSTEM_LOGGER.info("Msisdn encontrado: {}", msisdn);
            isSubscribed = (SubscriptionBo.getInstance().findSubscription(msisdn) != null) ? true : false;
            SYSTEM_LOGGER.info("Está suscrito en request params?: {}", isSubscribed);
        }

        if (isSubscribed == null || !isSubscribed) {
            return new RedirectResult(LP_LABUENAVIDA);
        }

        request.setAttribute("isSubscribed", isSubscribed);
        return new SuccessResult();
    }

}
