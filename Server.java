import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//För att köra på linux (EDVIN)
// java -cp .:mysql-connector-java-5.1.40-bin.jar Server


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
    private final Boolean isAdmin;
    private Socket socket;
    private int clientNumber;
    public BufferedReader in;
    public PrintWriter out;

    public Execute(int clientNumber,Boolean isAdmin) throws IOException {
      this.isAdmin=isAdmin;
      this.clientNumber = clientNumber;
      log("DB update running");
    }

    public Execute(Socket socket, int clientNumber,Boolean isAdmin) throws IOException {
      this.isAdmin=isAdmin;
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
    //  DB DB = new DB(out, in);
    //DB.connectDB();


    }else if(clientNumber==0){ // tråd som hämtar från HD
    
    String token = "joachim.lindborg@lsys.se:20170525171105:P01jJQ00DUF5lYGgEotjVX/MR5Q=";

    HDAPI plugsAPI = new HDAPI();
    //String token = plugsAPI.login();
    JSONArray boxResp = plugsAPI.getBoxes(token);
    String powerNow1 = plugsAPI.getPowerNow(token,(String)boxResp.get(0));
    String powerNow2 = plugsAPI.getPowerNow(token,(String)boxResp.get(1));
    String powerNow3 = plugsAPI.getPowerNow(token,(String)boxResp.get(2));
    JSONObject statResp = plugsAPI.getStatistics(token,(String)boxResp.get(1),8,9);
    System.out.println(statResp);
    System.out.println(powerNow1);
    System.out.println(powerNow2);
    System.out.println(powerNow3);
    //DBadmin.connectDB();
    // DBadmin.startUpdate();

}
  } catch (Exception e) {
      try{log("Error handling client# " + clientNumber + ": " + e);
	  e.printStackTrace();
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
