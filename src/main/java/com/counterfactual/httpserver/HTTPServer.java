package com.counterfactual.httpserver;

import com.counterfactual.httpserver.config.Configuration;
import com.counterfactual.httpserver.config.ConfigurationManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer {
  public static void main(String[] args){
    System.out.println("HelloWorld");

    ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
    Configuration config = ConfigurationManager.getInstance().getCurrentConfiguration();

    System.out.println("Port:" + config.getPort());
    System.out.println("Webroot:" + config.getWebroot());

    try {
      ServerSocket serverSocket = new ServerSocket(config.getPort());
      Socket socket = serverSocket.accept();

      InputStream inputStream = socket.getInputStream();
      OutputStream outputStream = socket.getOutputStream();

      String html = "<html><head><title>HelloWorld</title></head><body><h1>My server</h1></body></html>";

      final String CRLF = "\n\r"; //13, 10

      String response =
          "HTTP/1.1 200 OK" + CRLF +  //Status Line : HTML Version Respond_Code Respond_message
          "Content-Length: " + html.getBytes().length + CRLF +
            CRLF +
            html +
            CRLF + CRLF;

      outputStream.write(response.getBytes());

      inputStream.close();
      outputStream.close();
      socket.close();
      serverSocket.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
