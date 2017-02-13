package com.movile.mob.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compera.commons.bean.Carrier;
import com.compera.commons.util.Criptografia;
import com.compera.portal.processor.UserIdentifierProcessor;
import com.compera.portal.tree.Tree;

public class Util {
    private static final String CARRIER_TREE_PROPERTY = "carrier";
    private static final Logger sys = LoggerFactory.getLogger("system");

    public static Carrier getTreeCarrier(Tree node) {
        if (node.getProperty(CARRIER_TREE_PROPERTY) != null) {
            return Carrier.valueOf(node.getProperty(CARRIER_TREE_PROPERTY).getSimpleValue());
        }
        return null;
    }

    public static String getTreeProperty(Tree node, String property) {
        if (node.getProperty(property) != null) {
            return node.getProperty(property).getSimpleValue();
        }
        return null;
    }

    public static String getPhone(HttpServletRequest request) {

        String phone = (String) request.getAttribute(UserIdentifierProcessor.USER_ATTRIBUTE);

        if (StringUtils.isNotBlank(phone)) {
            sys.debug("Retrieved " + phone + " from user REQUEST USER attribute");
        } else {
            phone = request.getParameter("phone_redirect");
            sys.debug("Retrieved " + phone + " from phone_redirect REQUEST parameter");
        }

        if (StringUtils.isBlank(phone)) {
            phone = (String) request.getSession().getAttribute("phone_session");
            sys.debug("Retrieved " + phone + " from phone_session SESSION attribute");
        }

        if (StringUtils.isBlank(phone)) {
            String id = request.getParameter("i");
            if (StringUtils.isNotBlank(id)) {
                id = Criptografia.descriptografarNumerosMinusculos(id.trim());
                sys.debug("Retrieved " + phone + " from i encrypted REQUEST parameter");
            }
        }

        if (phone == null)
            sys.warn("Dind't find telephone");
        return phone;
    }
}
