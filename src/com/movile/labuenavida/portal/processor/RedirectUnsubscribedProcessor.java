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
import com.movile.labuenavida.util.SubscriptionUtil;
import com.movile.labuenavida.util.Util;

/**
 * @author Peter Escamilla (peter.escamilla@movile.com)
 *
 */
public class RedirectUnsubscribedProcessor extends Processor {

    @Override
    public void initiate(Item item) {}

    @Override
    public void terminate() {}

    @Override
    public ProcessorResult process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Tree node = (Tree) request.getAttribute(NodeFinderProcessor.NODE_ATTRIBUTE);
        String prev = Util.getTreeProperty(node, "prev");

        Boolean isSubscribed = false;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("msisdn".equals(cookie.getName())) {
                    isSubscribed = SubscriptionUtil.isSubscribed(cookie.getValue());
                }
            }
        }
        if ((isSubscribed == null || !isSubscribed) && StringUtils.isNotBlank(request.getParameter("msisdn"))) {
            String msisdn = request.getParameter("msisdn");
            msisdn = "57" + msisdn;
            isSubscribed = SubscriptionUtil.isSubscribed(msisdn);
        }
        if (!isSubscribed) {
            if (LogueoProcessor.DETRAS_KEYWORD.equals(prev)) {
                return new RedirectResult(request.getContextPath() + "/wap/logueodetras");
            } else {
                return new RedirectResult(request.getContextPath() + "/wap/logueo");
            }
        }
        return new SuccessResult();
    }

}
