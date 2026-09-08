package com.counterfactual.httpserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;

public class Json {

  private static ObjectMapper myObjectMapper = defaultObjectMapper();

  private static ObjectMapper defaultObjectMapper(){
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return objectMapper;
  }

  public static JsonNode parse(String jsonSource) throws IOException {
    return myObjectMapper.readTree(jsonSource);
  }

  public static <G> G fromJson(JsonNode node, Class<G> clazz) throws JsonProcessingException {
    return myObjectMapper.treeToValue(node, clazz);
  }

  public static JsonNode toJson(Object object){
    return myObjectMapper.valueToTree(object);
  }

  public static String stringify(JsonNode node) throws JsonProcessingException {return generateJson(node, false);}

  public static String stringifyPretty(JsonNode node) throws JsonProcessingException {return generateJson(node, true);}

  public static String generateJson(Object object, Boolean pretty) throws JsonProcessingException{
    ObjectWriter objectWriter = myObjectMapper.writer();
    if(pretty){
      objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
    }
    return objectWriter.writeValueAsString(object);
  }
}
