package com.counterfactual.httpserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTTPConnectionWorkerThread extends Thread{
  private static final Logger LOGGER = LoggerFactory.getLogger(HTTPConnectionWorkerThread.class);
  private Socket socket;

  public HTTPConnectionWorkerThread(Socket socket){this.socket = socket;}
  @Override
  public void run(){
    InputStream inputStream = null;
    OutputStream outputStream = null;
    try {
      inputStream = socket.getInputStream();
      outputStream = socket.getOutputStream();

      InputStream htmlStream =
          getClass().getClassLoader().getResourceAsStream("web/login.html");

      if (htmlStream == null) {
        throw new IOException("Could not find login.html");
      }

      String html = new String(
          htmlStream.readAllBytes(),
          StandardCharsets.UTF_8
      );

      final String CRLF = "\n\r"; //13, 10

      String response =
          "HTTP/1.1 200 OK" + CRLF +  //Status Line : HTML Version Respond_Code Respond_message
              "Content-Length: " + html.getBytes().length + CRLF +
              CRLF +
              html +
              CRLF + CRLF;

      outputStream.write(response.getBytes());
      LOGGER.info("Processing Finished");
    } catch (IOException e) {
      LOGGER.error("Problem with communication: ", e);
    } finally {
      if(inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {}
      }
      if(outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e) {}
      }
      if(socket != null){
        try {
          socket.close();
        } catch (IOException e) {}
      }
    }
  }
}

