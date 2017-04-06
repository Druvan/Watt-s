import java.io.BufferedReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
  public void DBadmin () {

  }
public void startUpdate(){

    //while (true){
      connectDB();
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

