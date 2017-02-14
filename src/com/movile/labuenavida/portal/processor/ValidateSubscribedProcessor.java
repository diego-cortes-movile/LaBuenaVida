/**
 * 
 */
package com.movile.labuenavida.portal.processor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compera.portal.parser.bean.Item;
import com.compera.portal.processor.Processor;
import com.compera.portal.processor.result.ProcessorResult;
import com.compera.portal.processor.result.RedirectResult;
import com.compera.portal.processor.result.SuccessResult;
import com.movile.labuenavida.bo.SubscriptionBo;
import com.movile.labuenavida.util.Util;
import com.movile.mob.util.BaseN;
import com.movile.sdk.services.sbs.model.Subscription;

/**
 * @author Yvan Lopez (yvan.lopez@movile.com)
 *
 */
public class ValidateSubscribedProcessor extends Processor {

    private static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("system");

    private static final String LP_LABUENAVIDA = "http://1mvl.com/lp/buenaVida/co/";

    // Usado para pruebas
    private static final Boolean IS_SUSCRIBED_TEST = false;

    @Override
    public void initiate(Item item) {}

    @Override
    public void terminate() {}

    @Override
    public ProcessorResult process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (IS_SUSCRIBED_TEST) {
            SYSTEM_LOGGER.info("Validación de prueba.");
            request.setAttribute("isSubscribed", true);
            return new SuccessResult();
        }

        Subscription subscription = null;
        Boolean isSubscribed = null;
        String msisdn = null;

        // Busca en cookies.
        Cookie[] cookies = request.getCookies();
        if (cookies != null && ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if ("msisdn".equals(cookie.getName()) || "user".equals(cookie.getName())) {
                    SYSTEM_LOGGER.info("Msisdn encontrado: {}", cookie.getName());
                    subscription = SubscriptionBo.getInstance().findSubscription(msisdn);
                    isSubscribed = (subscription != null) ? true : false;
                    SYSTEM_LOGGER.info("Esta suscrito en cookies?: {} - Suscripcion: {}", isSubscribed, subscription);
                }
            }
        }

        // Busca en headers.
        msisdn = Util.getPhoneFromHeaders(request);
        if (StringUtils.isNotBlank(msisdn)) {
            SYSTEM_LOGGER.info("Msisdn encontrado: {}", msisdn);
            subscription = SubscriptionBo.getInstance().findSubscription(msisdn);
            isSubscribed = (subscription != null) ? true : false;
            SYSTEM_LOGGER.info("Esta suscrito: {} - Suscripcion: {}", isSubscribed, subscription);
        }

        // Busca en parametros.
        if ((isSubscribed == null || !isSubscribed) && (StringUtils.isNotBlank(request.getParameter("msisdn"))
                || StringUtils.isNotBlank(request.getParameter("user")) || StringUtils.isNotBlank((String) request.getParameter("dm")))) {

            if (StringUtils.isNotEmpty(request.getParameter("msisdn"))) {
                msisdn = request.getParameter("msisdn");
            } else if (StringUtils.isNotEmpty(request.getParameter("user"))) {
                msisdn = request.getParameter("user");
            } else {
                msisdn = String.valueOf(BaseN.BASE64.revertToDecimal((String) request.getParameter("dm")));
            }

            SYSTEM_LOGGER.info("Msisdn encontrado: {}", msisdn);
            subscription = SubscriptionBo.getInstance().findSubscription(msisdn);
            isSubscribed = (subscription != null) ? true : false;
            SYSTEM_LOGGER.info("Esta suscrito?: {} - Suscripcion: {}", isSubscribed, subscription);
        }

        if (isSubscribed == null || !isSubscribed) {
            return new RedirectResult(LP_LABUENAVIDA);
        }

        Cookie cookie = new Cookie("msisdn", msisdn);
        cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(365));
        cookie.setPath("/");
        response.addCookie(cookie);

        request.setAttribute("msisdn", msisdn);
        request.setAttribute("subscription", subscription);
        request.setAttribute("isSubscribed", isSubscribed);

        return new SuccessResult();
    }

}
