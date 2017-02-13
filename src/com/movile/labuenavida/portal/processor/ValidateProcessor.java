/**
 * 
 */
package com.movile.labuenavida.portal.processor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.compera.portal.parser.bean.Item;
import com.compera.portal.processor.Processor;
import com.compera.portal.processor.result.CommitResult;
import com.compera.portal.processor.result.ProcessorResult;
import com.movile.labuenavida.properties.ApplicationProperties;
import com.movile.labuenavida.util.SubscriptionUtil;
import com.movile.mob.integration.AtlasWS;

/**
 * @author Peter Escamilla (peter.escamilla@movile.com)
 *
 */
public class ValidateProcessor extends Processor {

    @Override
    public void initiate(Item item) {}

    @Override
    public void terminate() {}

    @Override
    public ProcessorResult process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String responseText = "";
        String msisdn = request.getParameter("msisdn");
        msisdn = "57" + msisdn;
        String carrierID = AtlasWS.getCarrier(msisdn);
        if (ApplicationProperties.getInstance().getCarrier().equals(carrierID)) {
            if (SubscriptionUtil.isSubscribedWaitingRenew(msisdn)) {
                responseText = "Recuerda recargar tu linea para seguir disfrutando de todo lo que Rafael Novoa tiene para ti.";
            } else {
                if (SubscriptionUtil.isSubscribed(msisdn)) {
                    responseText = "success";
                } else {
                    responseText = "A&uacute;n no te encuentras suscrito, env&iacute;a un mensaje de texto con la palabra ACEPTO al 35047";
                }
            }
        } else {
            if ((!StringUtils.isNumeric(carrierID)) || "0".equals(carrierID)) {
                responseText = "N&uacute;mero inv&aacute;lido, por favor vuelve a ingresar tu l&iacute;nea Claro correctamente";
            } else {
                responseText = "Lo sentimos, este es un servicio exclusivo para Usuarios Claro.";
            }
        }
        PrintWriter outGrade;
        outGrade = response.getWriter();

        outGrade.print(responseText);
        outGrade.flush();
        return new CommitResult();
    }

}
