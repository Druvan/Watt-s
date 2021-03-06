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
    static final String SQL_FINDPLUGS = "SELECT * FROM plugs WHERE plugs.owner = ?";
    static final String SQL_ADDPOWERTIMESTAMP = "INSERT INTO powerUse (belongsTo, timestamp, power) SELECT id,?,? FROM plugs WHERE plugs.id=?";
    static final String SQL_DELETEUSER = "DELETE FROM users WHERE id = ?";
    static final String SQL_GETSOLARTIMESTAMP = "select * from solarCellView where owner = ? and timestamp between ? and ?";
    static final String SQL_GETPLUGTIMESTAMP = "select * from plugView where owner = ? and timestamp between ? and ?";
    static final String SQL_GETSOLARSUMDAY = "select * from solarCellSumDay where owner = ? and date between ? and ?";
    static final String SQL_GETONESOLARSUMDAY = "select date, sumPower ,owner from solarCellSumDay where owner = ? and belongsTo = ? and date between ? and ? group by date";
    static final String SQL_GETPLUGSUMDAY = "select * from plugSumDay where owner = ? and date between ? and ?";
    static final String SQL_GETONEPLUGSUMDAY = "select date, sumPower ,owner from plugSumDay where owner = ? and belongsTo = ? and date between ? and ? group by date";
    

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
  public synchronized int delUser(int id){
    try{
      PreparedStatement stmt = conn.prepareStatement(SQL_DELETEUSER);
      stmt.setInt(1, id);
      int ifUDel = stmt.executeUpdate();
      return  ifUDel;
    }
    catch (SQLException deletu) {
      deletu.printStackTrace();
      return -9;
    }
  }
    public synchronized ResultSet findPlug(int owner) {
	try {
	    PreparedStatement stmt = conn.prepareStatement(SQL_FINDPLUGS);
	    stmt.setInt(1, owner);
	    ResultSet rs = stmt.executeQuery();
	    return rs;
	}
	catch (SQLException findp) {
            findp.printStackTrace();
	    return null;
	}
    }


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

    public synchronized int addPowerTimestamp(String timestamp, int powerUse, int owner){
	try {
	    PreparedStatement stmt = conn.prepareStatement(SQL_ADDPOWERTIMESTAMP);
	    stmt.setString(1, timestamp);
	    stmt.setInt(2, powerUse);
	    stmt.setInt(3, owner);
	    int ifAcc = stmt.executeUpdate();
	    return ifAcc;
	}

	catch (SQLException powerex){
	    powerex.printStackTrace();
	    return -2;
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
	//	connectDB();
	HDAPI plugupdater = new HDAPI();
	plugupdater.login();
	//addUser("asd", "bich", "meisbest");
	//addPlug(7, "77d77d7");

        //addPowerTimestamp("2017-04-06 15:48:00",500,4);

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

