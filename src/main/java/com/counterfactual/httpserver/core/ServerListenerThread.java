package com.counterfactual.httpserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerListenerThread extends Thread{
  private int port;
  private String webroot;
  private ServerSocket serverSocket;
  private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);

  public ServerListenerThread(int port, String webroot) throws IOException {
    this.port = port;
    this.webroot = webroot;
    serverSocket = new ServerSocket(this.port);
  }

  @Override
  public void run(){
    try {
      Socket socket = serverSocket.accept();
      LOGGER.info("Client Accepted");

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
