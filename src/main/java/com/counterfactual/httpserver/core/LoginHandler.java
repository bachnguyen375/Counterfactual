package com.counterfactual.httpserver.core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class LoginHandler {

  private static final String USERNAME = "Altman";
  private static final String PASSWORD = "ARABIA";
  private static int attempts = 3;

  public static void handleLogin(
      String body,
      OutputStream outputStream
  ) throws IOException {
    String username = "";
    String password = "";

    // Parse username&password
    String[] parameters = body.split("&");
    for (String parameter : parameters) {

      String[] pair = parameter.split("=", 2);
      if (pair.length != 2) {
        continue;
      }
      String key = URLDecoder.decode(
          pair[0],
          StandardCharsets.UTF_8
      );
      String value = URLDecoder.decode(
          pair[1],
          StandardCharsets.UTF_8
      );

      if (key.equals("username")) {
        username = value;
      }

      if (key.equals("password")) {
        password = value;
      }
    }

    // Correct credentials → go to home page
    if (username.equals(USERNAME)
        && password.equals(PASSWORD)) {

      sendRedirect(outputStream, "/home");
    }
    else{
      attempts -= 1;
      if(attempts == 0){
        sendRedirect(outputStream, "/fail");
      }
      sendRedirect(outputStream, "/");
    }
  }

  private static void sendRedirect(
      OutputStream outputStream,
      String location
  ) throws IOException {

    String CRLF = "\r\n";

    String response =
        "HTTP/1.1 302 Found" + CRLF +
            "Location: " + location + CRLF +
            "Content-Length: 0" + CRLF +
            CRLF;

    outputStream.write(
        response.getBytes(StandardCharsets.UTF_8)
    );

    outputStream.flush();
  }
}

