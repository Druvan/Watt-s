import java.net.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;

import org.apache.commons.io.IOUtils;


//curl -X GET --header 'Accept: application/json' --header 'X-Authenticate-Token: joachim.lindborg@lsys.se:20170525171105:P01jJQ00DUF5lYGgEotjVX/MR5Q=' --header 'X-Application-Key: ' 'https://smarthome.hd-wireless.com:9001/boxes/78c40e104b53/database/profile/power/now'


public class HDAPI{
    static final String URL = "https://smarthome.hd-wireless.com:9001";
    static final String loginURL = URL+"/login";
    static final String boxesURL = URL+"/boxes";
    static final String powerNowEndURL = "/database/profile/power/now";
    JSONParser parser = new JSONParser();
    static final String NoToken = null;

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


    public void HDAPI(){
    }




    public String login() {

	try {
 
	    HttpURLConnection HDconn = httpPost(loginURL,NoToken);

	    OutputStreamWriter out = new OutputStreamWriter(HDconn.getOutputStream());
	    out.write(jsonLogin.toString());
	    out.close();
	    

	    JSONObject Jresp = inputToJSONObject(HDconn);
	    String token = fromJson(Jresp,"AuthenticateToken");

	    return token;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public JSONArray getBoxes(String token){

	try {
 
	    HttpURLConnection HDconn = httpGet(boxesURL,token);

	    int respCode = HDconn.getResponseCode();
	    

	    JSONArray resp = inputToJSONArray(HDconn);

	    return resp;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}


    }
    public String getPowerNow(String token, String boxname) throws Exception{
        String urlstring = boxesURL+"/"+boxname+powerNowEndURL;
	HttpURLConnection HDconn = httpGet(urlstring,token);
	int respCode = HDconn.getResponseCode();
	String resp = inputToString(HDconn); 
	return resp;
    }

    public JSONObject getStatistics(String token,String boxname,int starttime, int endtime) throws Exception{
	String urlstring = generateStatisticsURL(boxname,starttime,endtime);
	HttpURLConnection HDconn = httpGet(urlstring,token);
	int respCode = HDconn.getResponseCode();
	JSONObject resp = inputToJSONObject(HDconn);
	return resp;

    }

    public String generateStatisticsURL(String boxname, int starttime, int endtime){
	String start = Integer.toString(starttime);
	String end = Integer.toString(endtime);
	return boxesURL+"/"+boxname+"/statistics?begin_time=2017-03-31T"+start+":00&end_time=2017-03-31T"+end+":00";
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


    private String inputToString(HttpURLConnection conn){
	try{
	    InputStreamReader in =new InputStreamReader(conn.getInputStream());   
	    String response = IOUtils.toString(in);

		return response;
	}catch(Exception its){
	    its.printStackTrace();
	    return null;
	}
    }

    private JSONArray inputToJSONArray(HttpURLConnection conn){
	try{
	    String response = inputToString(conn);
	    JSONArray json = stringToJSONArray(response);
	    if(json instanceof JSONArray){
		return json;
	    }
	    else return null;
	}catch(Exception itja){
	    itja.printStackTrace();
	    return null;
	}
    }


    private JSONObject inputToJSONObject(HttpURLConnection conn){
	try{
	    String response = inputToString(conn);
	    JSONObject json = stringToJSONObject(response);
	    if(json instanceof JSONObject){
		return json;
	    }
	    else return null;
	}catch(Exception itjo){
	    itjo.printStackTrace();
	    return null;
	}
    }

    public JSONArray stringToJSONArray(String string){
	try{
	    JSONArray json = (JSONArray) parser.parse(string);
	    return json;
	}catch(Exception stj){
	    stj.printStackTrace();
	    return null;
	}

    }

    public JSONObject stringToJSONObject(String string){
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
