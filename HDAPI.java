import java.net.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import org.apache.commons.io.IOUtils;



public class HDAPI{
    static final String URL = "https://smarthome.hd-wireless.com:9001";
    static final String loginURL = URL+"/login";
    static final String boxesURL = URL+"/boxes";
    JSONParser parser = new JSONParser();
    private static final JSONObject jsonLogin = new JSONObject(){
    {
        try {
            put("ID","joachim.lindborg@lsys.se");
	    put("Password","4WR2wUTp");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
};
    static final String NoToken = null;


    public void HDAPI(){
    }




    public String login() {

	try {
 
	    HttpURLConnection HDconn = httpPost(loginURL,NoToken);

	    OutputStreamWriter out = new OutputStreamWriter(HDconn.getOutputStream());
	    out.write(jsonLogin.toString());
	    out.close();
	    
	    InputStreamReader in =new InputStreamReader(HDconn.getInputStream()); 
	    JSONObject Jresp = inputToJSON(HDconn);
	    String token = fromJson(Jresp,"AuthenticateToken");

	    return token;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }
	public JSONObject getBoxes(String token){

	try {
 
	    HttpURLConnection HDconn = httpGet(boxesURL,token);

	    int respCode = HDconn.getResponseCode();
	    
	    InputStreamReader in =new InputStreamReader(HDconn.getInputStream());
	    String response = IOUtils.toString(in);
	    System.out.println(response);
	    JSONObject Jresp = inputToJSON(HDconn);
	    System.out.println(Jresp);

	    return Jresp;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}


}


    
    private HttpURLConnection httpPost(String url,String token){
	try{
	URL obj = new URL(url);
	HttpURLConnection HDconn = (HttpURLConnection) obj.openConnection();
 
	HDconn.setRequestProperty("Content-Type", "application/json");
	if(token!=null) {HDconn.setRequestProperty("X-Authenticate-Token",token);}
	HDconn.setRequestProperty("Accept", "application/json");
	HDconn.setDoOutput(true);
 
	HDconn.setRequestMethod("POST");
	return HDconn;
	}catch(Exception hP) {
	     hP.printStackTrace();
	    return null;
	}

    }

 private HttpURLConnection httpGet(String url,String token){
	try{
	URL obj = new URL(url);
	HttpURLConnection HDconn = (HttpURLConnection) obj.openConnection();
 
	HDconn.setRequestProperty("Accept", "application/json");
        HDconn.setRequestProperty("X-Authenticate-Token",token);

 
	HDconn.setRequestMethod("GET");
	return HDconn;
	}catch(Exception hG) {
	     hG.printStackTrace();
	    return null;
	}

    }
    //curl -X GET --header 'Accept: application/json' --header 'X-Authenticate-Token: joachim.lindborg@lsys.se:20170513085611:IQeazexJKqfUjS34VJOgqwXfVDU='  'https://smarthome.hd-wireless.com:9001/boxes'
    //--header 'X-Application-Key:'




    private JSONObject inputToJSON(HttpURLConnection conn){
	try{
	    InputStreamReader in =new InputStreamReader(conn.getInputStream());   
	    String response = IOUtils.toString(in);
	    JSONObject json = stringToJSON(response);
	    if(json instanceof JSONObject){
		return json;
	    }
	else return null;
	}catch(Exception itj){
	    itj.printStackTrace();
	    return null;
	}
    }

    public JSONObject stringToJSON(String string){
	try{
	    JSONObject json = (JSONObject) parser.parse(string);
	    return json;
	}catch(Exception stj){
	    stj.printStackTrace();
	    return null;
	}

    }

    private String fromJson(JSONObject jsonObject,String field){
	try{
	    String ans = (String) jsonObject.get(field);
	    return ans;
	}catch(Exception stj){
	    stj.printStackTrace();
	    return null;
	}
    }  


/** 
 * Parses the provided string into a  {@code JSONObject}.
 * @param json string to be parsed
 * @return a {@code JSONObject}
 * @throws {@code AssertionError} if the string cannot be parsed into a {@code JSONObject}
 
protected JSONObject parseJSONObject(String json) throws AssertionError {
  JSONParser parser=getJSONParser();
  try {
    Object obj=parser.parse(json);
    assertTrue(obj instanceof JSONObject);
    return (JSONObject)obj;
  }
 catch (  Exception e) {
    throw new AssertionError("not a valid JSON object: " + e.getMessage());
  }
}
*/
}
//curl -X POST -H "Content-Type: application/json" -H "X-Application-Key: " -H "Accept: application/json" -d '{ "Id": "joachim.lindborg@lsys.se", "Password": "4WR2wUTp", }' https://smarthome.hd-wireless.com:9001/login
