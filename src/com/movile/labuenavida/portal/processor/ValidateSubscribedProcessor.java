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
import com.compera.portal.processor.Processor;
import com.compera.portal.processor.result.ProcessorResult;
import com.compera.portal.processor.result.SuccessResult;
import com.movile.labuenavida.util.SubscriptionUtil;

/**
 * @author Yvan Lopez (yvan.lopez@movile.com)
 *
 */
public class ValidateSubscribedProcessor extends Processor {

    @Override
    public void initiate(Item item) {}

    @Override
    public void terminate() {}

    @Override
    public ProcessorResult process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//        Boolean isSubscribed = null;
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("msisdn".equals(cookie.getName())) {
//                    isSubscribed = SubscriptionUtil.isSubscribed(cookie.getValue());
//                }
//            }
//        }
//        if ((isSubscribed == null || !isSubscribed) && StringUtils.isNotBlank(request.getParameter("msisdn"))) {
//            String msisdn = request.getParameter("msisdn");
//            msisdn = "57" + msisdn;
//            isSubscribed = SubscriptionUtil.isSubscribed(msisdn);
//        }
//        request.setAttribute("isSubscribed", isSubscribed);
        return new SuccessResult();
    }

}
