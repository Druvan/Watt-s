import java.io.*;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.*;

public class DBadmin {
  Connection conn = null;
  PrintWriter printer;
  BufferedReader in;
  // JDBC driver name and database URL
  //static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  static final String DB_URL = "jdbc:mysql://83.253.24.101:942/Watts";

  //CLASSPATH=$CLASSPATH:/usr/share/java/mysql.jar:/home/edvin/Datakom/Project/LockAndLaundry/LockAndLaundry/DB:/home/edvin/Datakom/Project/LockAndLaundry/LockAndLaundry/Server
  //export CLASSPATH asdasd


  //  Database credentials
  static final String USER = "awattsdmin";
  static final String PASS = "Gemen0215sam¤!=";

  private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  String currentDate;

  /*
  * PreparedStatement
  */


  static final String SQL_SELECTSLOTTABLE = "Select Slot,Date,Time,IFNULL(Booked,\"\") Booked_by from slottable where Date BETWEEN ? AND ?";
  static final String SQL_SELECTWHOBOOKED = "SELECT Slot, date, concat(start, \"-\", end) AS Time FROM mybookings WHERE customerid = ? AND Date BETWEEN ? AND ?";
  static final String SQL_INSERTBOOKING = "INSERT INTO reserved (slot_id,customer_id) SELECT slot.id,customer.id FROM slot,customer WHERE customer.id =? AND slot.id =?";
  static final String SQL_CHECKIFEXIST = "SELECT count(*) AS sqlexist FROM slot WHERE id = ?";
  static final String SQL_CHECKIFBOOKED = "SELECT count(*) AS sqlbooked FROM reserved WHERE slot_id = ?";
  static final String SQL_CHECKIFBOOKEDEXISTS = "SELECT count(*) AS sqlbookedexists FROM reserved WHERE id = ?";
  static final String SQL_DELETEBOOKEDROW = "DELETE FROM reserved WHERE id = ?";
  static final String SQL_CHECKCUSTOMER = "SELECT count(*) AS customerexists FROM customer WHERE id = ?";
  static final String SQL_TOOMANYBOOKINGS = "SELECT count(*) FROM number_of_bookings WHERE customer_id = ? AND date >= ?";
  static final String SQL_ADDUSER = "INSERT INTO users (name, salt, password) VALUES(?, ?, ?)";
  static final String SQL_CHECKNAME = "SELECT count(*) FROM user WHERE name = ?";
  static final String SQL_ADDPLUG = "INSERT INTO plugs (owner, plugnumb) SELECT users.id, ? FROM users WHERE users.id = ?";

  /*
  connect to DB
  receive from API
  parse API data
  send to DB
  sleep
  int slotExist = slotCheck(ID, SQL_CHECKCUSTOMER, "customerexists", conn);

  PreparedStatement stmt1 = conn.prepareStatement(SQL_SELECTSLOTTABLE);
          stmt1.setString(1, currentDate);
          stmt1.setString(2, endDate);
          ResultSet res = stmt1.executeQuery();

  * Constructor
*/
  /*
  public synchronized  int checkPlug(int id){
    PreparedStatement stmt = conn.prepareStatement(SQL_CHECKPLUGID);

    if(id = 0){

    }
    else

  }*/
//Make sure that users can't have the same username
    /*
   public synchronized boolean checkUserName(String name){
      try{
      PreparedStatement stmt = conn.prepareStatement(SQL_CHECKNAME);
      stmt.setString(1, name);
      ResultSet rs = stmt.executeQuery();
      int numbname = rs.getInt(0);
      if (numbname == 0) {
        System.out.printf("OK");
        return true;

      }
      else {
        System.out.printf("FEL");
        return false;
      }
    }
    catch (SQLException samex) {
         samex.printStackTrace();
         return false;
    }

  }          */

    public synchronized int addPlug(int owner, String plugnumb) {
      try{
        PreparedStatement stmt = conn.prepareStatement(SQL_ADDPLUG);

        stmt.setInt(2, owner);
         stmt.setString(1, plugnumb);
        int ifPAcc = stmt.executeUpdate();
        return  ifPAcc;
      }
      catch (SQLException addp){
        addp.printStackTrace();
        return -3;
      }

    }


public synchronized int addUser(String name, String salt, String password){
  try {
    PreparedStatement stmt = conn.prepareStatement(SQL_ADDUSER);
    stmt.setString(1, name);
    stmt.setString(2, salt);
    stmt.setString(3, password);
    int ifUAcc = stmt.executeUpdate();
    return ifUAcc;
  }

  catch (SQLException addex){
    addex.printStackTrace();
    return -2;
  }

}


  public void DBadmin () {

  }

public void startUpdate(){


    //while (true){
    connectDB();
    addUser("asd", "bich", "meisbest");
    addPlug(9, "7asdjlk3ry98u");
    //hämta från API
    //parse API
    //uppdatera DB
    //sleep
    // }
}

  public Connection connectDB(){
    try{
      System.out.println(currentDate);
      // Class.forName("com.mysql.jdbc.Driver"); //Får olika errors om denna är med eller inte...
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);

      return conn;
    }catch(SQLException se){
      //Handle errors for JDBC
	System.out.println("SQLException: " + se.getMessage());
	System.out.println("SQLState: " + se.getSQLState());
	System.out.println("VendorError: " + se.getErrorCode());
      se.printStackTrace();
      return null;
    }
  }
}

