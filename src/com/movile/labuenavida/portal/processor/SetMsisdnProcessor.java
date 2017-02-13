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

/**
 * @author Peter Escamilla (peter.escamilla@movile.com)
 *
 */
public class SetMsisdnProcessor extends Processor {

    @Override
    public void initiate(Item item) {}

    @Override
    public void terminate() {}

    @Override
    public ProcessorResult process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (StringUtils.isNotBlank(request.getParameter("msisdn"))) {
            String msisdn = request.getParameter("msisdn");
            msisdn = "57" + msisdn;
            Cookie cookie = new Cookie("msisdn", msisdn);
            cookie.setMaxAge(315360000);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return new SuccessResult();
    }

}
