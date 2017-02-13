package com.movile.labuenavida.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.movile.labuenavida.util.ConfigurationProvider;
import com.movile.labuenavida.util.MovileSdkHolder;

/**
 * {@link ServletContextListener} implementation for the Carrier Billing service.
 * 
 * @author Yvan Lopez (yvan.lopez@movile.com)
 */
public class LaBuenaVidaContextListener implements ServletContextListener {

    private static final Logger EXCEPTION_LOGGER = LoggerFactory.getLogger("exception");

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ConfigurationProvider.startConfiguration();
            MovileSdkHolder.initialize();
        } catch (ConfigurationException e) {
            EXCEPTION_LOGGER.error("Error initializing", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Not implemented
    }
}
