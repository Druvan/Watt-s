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
    System.out.println("The Lock N' Laundry server is running.");
    int clientNumber = 0;
    ServerSocket listener = new ServerSocket(9898);

    try {
      while (true) {
        new Execute(listener.accept(), clientNumber++).start();
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
    public Execute(Socket socket, int clientNumber) throws IOException {
      this.socket = socket;
      this.clientNumber = clientNumber;
      this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.out = new PrintWriter(socket.getOutputStream(), true);
      log("New connection with client# " + clientNumber + " at " + socket);
    }

public void run() {
  try {

    socket.setKeepAlive(true);


    DB DB = new DB(out, in);
    DB.connectDB();

    out.println("--------------------------------\n---Welcome to Lock N' Laundry---\n--------------------------------\n");
    out.println("Enter your ID:");

    int ID;


    while (true){
      ID = Integer.parseInt(in.readLine());
      int confirmID = DB.login(ID);
      if(confirmID == 1){
        out.println("Login successful");
        break;
      } else {
        out.println("Invalid ID, try again");
      }
    }

    while (true) {
    int mainInput = DB.mainMenu();

    if(mainInput == 1 ){
      DB.bookSlot(ID);
    }else if (mainInput == 2){
      DB.cancelSlot(ID);
    }else if (mainInput == 0){
      out.println("Goodbye");
      in.close();
      out.close();
      socket.close();
      break;
    } else {
      out.println("Wrong input, try again");
    }
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
