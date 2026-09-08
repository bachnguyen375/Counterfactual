package com.counterfactual.httpserver.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class HTTPConnectionWorkerThread extends Thread {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(
          HTTPConnectionWorkerThread.class
      );

  private final Socket socket;

  public HTTPConnectionWorkerThread(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {

    try (
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream()
    ) {

      BufferedReader reader = new BufferedReader(
          new InputStreamReader(inputStream)
      );

      // Example:
      // GET /HTTP/1.1
      // POST /login HTTP/1.1
      String requestLine = reader.readLine();

      if (requestLine == null) {
        return;
      }

      LOGGER.info("Request: {}", requestLine);

      String[] requestParts = requestLine.split(" ");

      if (requestParts.length < 2) {
        return;
      }

      String method = requestParts[0];
      String path = requestParts[1];

      /*
       * Read headers
       */
      int contentLength = 0;

      String line;

      while (!(line = reader.readLine()).isEmpty()) {

        if (line.toLowerCase().startsWith(
            "content-length:"
        )) {

          contentLength = Integer.parseInt(
              line.substring(
                  "content-length:".length()
              ).trim()
          );
        }
      }

      /*
       * Read POST body
       */
      String body = "";

      if (contentLength > 0) {

        char[] bodyChars =
            new char[contentLength];

        reader.read(bodyChars);

        body = new String(bodyChars);
      }

      /*
       * ROUTING
       */

      // Login request
      if (method.equals("POST")
          && path.equals("/login")) {

        LoginHandler.handleLogin(
            body,
            outputStream
        );

        return;
      }

      // Login page
      if (method.equals("GET") && path.equals("/")) {
        sendResource(
            outputStream,
            "web/login.html",
            "text/html; charset=UTF-8"
        );
        return;
      }

      // Home page
      if (method.equals("GET") && path.equals("/home")) {
        sendResource(
            outputStream,
            "web/home.html",
            "text/html; charset=UTF-8"
        );
        return;
      }
      //Fail page
      if (method.equals("GET") && path.equals("/fail")) {
        sendResource(
            outputStream,
            "web/fail.html",
            "text/html; charset=UTF-8"
        );
        socket.close();
        return;
      }

      // Catch all 404
      send404(outputStream);
    } catch (IOException e) {

      LOGGER.error(
          "Problem with communication: ",
          e
      );

    } finally {

      try {
        socket.close();
      } catch (IOException e) {
        LOGGER.error(
            "Could not close socket",
            e
        );
      }
    }
  }

  private void sendResource(
      OutputStream outputStream,
      String resourcePath,
      String contentType
  ) throws IOException {

    InputStream resource =
        getClass()
            .getClassLoader()
            .getResourceAsStream(resourcePath);

    if (resource == null) {
      send404(outputStream);
      return;
    }

    byte[] body = resource.readAllBytes();

    String CRLF = "\r\n";

    String response =
        "HTTP/1.1 200 OK" + CRLF +
            "Content-Type: " + contentType + CRLF +
            "Content-Length: " + body.length + CRLF +
            CRLF;

    outputStream.write(
        response.getBytes(StandardCharsets.UTF_8)
    );

    outputStream.write(body);
    outputStream.flush();
  }

  private void send404(
      OutputStream outputStream
  ) throws IOException {

    String body = "<html><body><h1>404 Not Found</h1></body></html>";

    byte[] bodyBytes =
        body.getBytes(StandardCharsets.UTF_8);

    String CRLF = "\r\n";

    String response =
        "HTTP/1.1 404 Not Found" + CRLF +
            "Content-Type: text/html; charset=UTF-8" + CRLF +
            "Content-Length: " + bodyBytes.length + CRLF +
            CRLF;

    outputStream.write(
        response.getBytes(StandardCharsets.UTF_8)
    );

    outputStream.write(bodyBytes);
    outputStream.flush();
  }
}



