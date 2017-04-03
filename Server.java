import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.sql.*;
import java.util.*;



public class Server {


  public static void main(String[] args) throws Exception {
    System.out.println("Watt-s server running");
    int clientNumber = 0;
    Boolean isAdmin;
    new Execute(clientNumber++,true).start();
    ServerSocket listener = new ServerSocket(9898);

    try {
      while (true) {
        new Execute(listener.accept(), clientNumber++, false).start();
      }
    } finally {
      listener.close();
    }
  }

  private static class Execute extends Thread {
    private Socket socket;
    private int clientNumber;
    public BufferedReader in;
    public PrintWriter out;

    public Execute(int clientNumber,Boolean isAdmin) throws IOException {
      this.isAdmin=isAdmin
      this.clientNumber = clientNumber;
      log("DB update running");
    }

    public Execute(Socket socket, int clientNumber,Boolean isAdmin) throws IOException {
            this.isAdmin=isAdmin
      this.socket = socket;
      this.clientNumber = clientNumber;
      this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.out = new PrintWriter(socket.getOutputStream(), true);
      log("New connection with client# " + clientNumber + " at " + socket);
    }

public void run() {
  try {




    if(clientNumber!=0) {
      socket.setKeepAlive(true);
//här ska clienten skicaks vidare
      DB DB = new DB(out, in);
    DB.connectDB();
}else if(clientNumber==0){
    DBadmin DBadmin = new DBadmin();
    DBadmin.startUpdate();

}
  } catch (IOException e) {
      try{log("Error handling client# " + clientNumber + ": " + e);
      out.println("Goodbye");
      in.close();
      out.close();
      socket.close();
      } catch (IOException er){
            log("Can't kill sockets, THEY ARE TOO STRONG");
      }

  }finally {
    try {
      socket.close();
    } catch (IOException e) {
      log("Can't kill sockets, THEY ARE TOO STRONG");
    }
    log("Connection with client# " + clientNumber + " closed");
  }
}

private void log(String message) {
  System.out.println(message);
}

}
}
