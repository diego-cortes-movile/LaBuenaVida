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

import com.compera.portal.parser.bean.Item;
import com.compera.portal.processor.NodeFinderProcessor;
import com.compera.portal.processor.Processor;
import com.compera.portal.processor.result.ProcessorResult;
import com.compera.portal.processor.result.RedirectResult;
import com.compera.portal.processor.result.SuccessResult;
import com.compera.portal.tree.Tree;
import com.movile.labuenavida.properties.ApplicationProperties;
import com.movile.labuenavida.util.SubscriptionUtil;
import com.movile.labuenavida.util.Util;
import com.movile.mob.integration.AtlasWS;

/**
 * @author Peter Escamilla (peter.escamilla@movile.com)
 *
 */
public class LogueoProcessor extends Processor {

    public static final String DETRAS_KEYWORD = "detras";

    @Override
    public void initiate(Item item) {}

    @Override
    public void terminate() {}

    @Override
    public ProcessorResult process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Tree node = (Tree) request.getAttribute(NodeFinderProcessor.NODE_ATTRIBUTE);
        String prev = Util.getTreeProperty(node, "prev");
        request.setAttribute("prev", prev);
        
        Boolean isSubscribed = null;
        Cookie[] cookies = request.getCookies();
        String msisdn = null;
        Boolean isValidNumber = true;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("msisdn".equals(cookie.getName())) {
                    msisdn = cookie.getValue();
                    isSubscribed = SubscriptionUtil.isSubscribed(msisdn);
                }
            }
        }
        if ((isSubscribed == null || !isSubscribed) && StringUtils.isNotBlank(request.getParameter("msisdn"))) {
            msisdn = request.getParameter("msisdn");
            isSubscribed = SubscriptionUtil.isSubscribed(msisdn);
        }
        if (StringUtils.isNotBlank(msisdn)) {
            String carrierID = AtlasWS.getCarrier(msisdn);
            isValidNumber = ApplicationProperties.getInstance().getCarrier().equals(carrierID);
        }
        
        request.setAttribute("isValidNumber", isValidNumber);
        request.setAttribute("isSubscribed", isSubscribed);
        
        if (isSubscribed != null && isSubscribed) {
            if (DETRAS_KEYWORD.equals(prev)) {
                return new RedirectResult(request.getContextPath() + "/wap/detrasdecamaras");
            } else {
                return new RedirectResult(request.getContextPath() + "/wap/retonovoa");
            }
        }
        
        return new SuccessResult();
    }

}
