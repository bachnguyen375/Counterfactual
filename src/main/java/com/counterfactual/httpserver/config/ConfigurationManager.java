package com.counterfactual.httpserver.config;

import com.counterfactual.httpserver.util.Json;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {
  private static ConfigurationManager myConfigurationManager;
  private static Configuration myCurrentConfiguration;

  private ConfigurationManager(){};
  public static ConfigurationManager getInstance(){
    if(myConfigurationManager==null)
      myConfigurationManager = new ConfigurationManager();
    return myConfigurationManager;
  }

  /**
   * Throws error if no filepath
   * @param filePath
   */
  public void loadConfigurationFile(String filePath) {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (FileNotFoundException e) {
      throw new HttpConfigurationException(e);
    }
    StringBuffer stringBuffer = new StringBuffer();
    int i;
    while (true){
      try {
        if (!((i = fileReader.read()) != -1))
          break;
      } catch (IOException e) {
        throw new HttpConfigurationException(e);
      }
      stringBuffer.append((char)i);
    }
    JsonNode config = null;
    try {
      config = Json.parse(stringBuffer.toString());
    } catch (IOException e) {
      throw new HttpConfigurationException("Error parsing the config file",e);
    }
    try {
      myCurrentConfiguration = Json.fromJson(config, Configuration.class);
    } catch (JsonProcessingException e) {
      throw new HttpConfigurationException("Error parsing the config file, internal",e);
    }
  }

  /**
   * Throws error if no current configuration
   */
  public Configuration getCurrentConfiguration(){
    if (myCurrentConfiguration == null){
      throw new HttpConfigurationException("No current figuration set");
    }
    return myCurrentConfiguration;
  }
}
