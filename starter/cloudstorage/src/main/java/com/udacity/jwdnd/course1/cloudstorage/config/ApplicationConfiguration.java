package com.udacity.jwdnd.course1.cloudstorage.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    private int maxUploadSizeInMb = -1;

    @Bean
    public TomcatServletWebServerFactory containerFactory() {
        return new TomcatServletWebServerFactory() {
            protected void customizeConnector(Connector connector) {
                super.customizeConnector(connector);
                connector.setMaxPostSize(maxUploadSizeInMb);
                connector.setMaxSavePostSize(maxUploadSizeInMb);
                if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol) {

                    ((AbstractHttp11Protocol <?>) connector.getProtocolHandler()).setMaxSwallowSize(maxUploadSizeInMb);
                    logger.info("Set MaxSwallowSize "+ maxUploadSizeInMb);
                }
            }
        };

    }
}
