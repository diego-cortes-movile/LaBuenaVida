package com.movile.labuenavida.exception;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.movile.labuenavida.enums.MessageKey;
import com.movile.labuenavida.util.ConfigurationProvider;

/**
 * @author Yvan Lopez (yvan.lopez@movile.com)
 *
 */
public class LaBuenaVidaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String message;

    public LaBuenaVidaException(MessageKey messageKey) {
        super();
        Validate.notNull(messageKey);
        this.message = ConfigurationProvider.getMessagesProperties().getString(messageKey.getKey());
    }

    @Override
    public String getMessage() {
        return StringUtils.stripAccents(message);
    }

    public String getHtmlMessage() {
        return StringEscapeUtils.escapeHtml4(message);
    }

}
