package com.counterfactual.httpserver;

import com.counterfactual.httpserver.config.Configuration;
import com.counterfactual.httpserver.config.ConfigurationManager;

public class HTTPServer {
  public static void main(String[] args){
    System.out.println("HelloWorld");

    ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
    Configuration config = ConfigurationManager.getInstance().getCurrentConfiguration();

    System.out.println("Port:" + config.getPort());
    System.out.println("Webroot:" + config.getWebroot());
  }
}
