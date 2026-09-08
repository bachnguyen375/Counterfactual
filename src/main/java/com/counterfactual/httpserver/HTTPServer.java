package com.counterfactual.httpserver;

import com.counterfactual.httpserver.config.Configuration;
import com.counterfactual.httpserver.config.ConfigurationManager;
import com.counterfactual.httpserver.core.ServerListenerThread;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTTPServer {
  private final static Logger LOGGER = LoggerFactory.getLogger(HTTPServer.class);

  public static void main(String[] args){
    LOGGER.info("Hello World");

    ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
    Configuration config = ConfigurationManager.getInstance().getCurrentConfiguration();

    LOGGER.info("Port:" + config.getPort());
    LOGGER.info("Webroot:" + config.getWebroot());

    ServerListenerThread listenerThread = null;
    try {
      listenerThread = new ServerListenerThread(config.getPort(), config.getWebroot());
    } catch (IOException e) {
      e.printStackTrace();
    }
    listenerThread.start();
  }
}
