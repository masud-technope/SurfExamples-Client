package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import core.Result;

public class MyClient {

	/**
	 * @param args
	 */

	 //String web_service_url="http://wssurfclipse.appspot.com/surfclipse_app?";
	//String web_service_url = "http://localhost:8080/wssurfexamples/SurfExamples_app?";
	String web_service_url="http://srlabg53-2.usask.ca/wssurfexamples/SurfExamples_app?";
	String searchQuery;
	String queryException;
	String codecontext;
	int targetExceptionLine;
	final int TOTAL_ITEMS = 15;

	public MyClient() {
		// default constructor
	}

	public MyClient(String queryException, String searchQuery, String codeContext, int targetExceptionLine) {
		// constructor for MyClient
		this.searchQuery = searchQuery;
		this.queryException=queryException;
		this.codecontext = codeContext;
		this.targetExceptionLine=targetExceptionLine;
		String params = "";
		String charset = "UTF-8";

		try {
			
			params = "queryexception="
					+ URLEncoder.encode(this.queryException, charset);
			params += "&searchquery="
					+ URLEncoder.encode(this.searchQuery, charset);
			params += "&codecontext="
					+ URLEncoder.encode(this.codecontext, charset);
			params += "&targetline="
					+ URLEncoder.encode(this.targetExceptionLine+"", charset);
			//System.out.println(URLEncoder.encode(this.codecontext, charset));
		} catch (Exception exc) {
			
		}
		this.web_service_url += params;
		System.out.println(this.web_service_url);
		System.out.println( "Query exception:"+this.queryException+", Search query:" + this.searchQuery+", selected line:"+this.targetExceptionLine);
	}

	protected ArrayList<Result> collect_search_results() {
		// code for collecting search results
		long startTime = System.currentTimeMillis();
		String jsonResponse = perform_surfexample_call();
		//String jsonResponse=collect_demo_results();
		ArrayList<Result> results = convert_json_to_results(jsonResponse);
		long endTime = System.currentTimeMillis();
		long time_interval = (endTime - startTime) / 1000;
		System.out.println("Time required for search:" + time_interval + " s");
		// now showing the results to timer label
		// show_time_interval(time_interval);
		return results; // convert_json_to_results(jsonResponse);
	}

	protected String collect_demo_results() {
		// code for collecting demo results
		String jsonText = new String();
		try {
			String surfclipse_dir = System.getProperty("user.home")
					+ "/surfexamples";
			String tempFile = surfclipse_dir + "/temp.txt";
			Scanner scanner = new Scanner(new File(tempFile));
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				jsonText += line;
			}
			scanner.close();
		} catch (Exception exc) {
		}
		return jsonText;
	}

	protected String perform_surfexample_call() {
		// code for performing SurfClipse call
		String jsonResponse = new String();
		try {
			// String
			// myurl="http://srlabg53-2.usask.ca/wssurfclipse/surfclipse_app?searchquery=java.lang.ClassNotFoundException%3A+org.sqlite.JDBC&stacktrace=java.lang.ClassNotFoundException%3A+org.sqlite.JDBC%0D%0A%09at+java.net.URLClassLoader%241.run%28Unknown+Source%29%0D%0A%09at+java.net.URLClassLoader%241.run%28Unknown+Source%29%0D%0A%09at+java.security.AccessController.doPrivileged%28Native+Method%29%0D%0A%09at+java.net.URLClassLoader.findClass%28Unknown+Source%29%0D%0A%09at+java.lang.ClassLoader.loadClass%28Unknown+Source%29%0D%0A%09at+sun.misc.Launcher%24AppClassLoader.loadClass%28Unknown+Source%29%0D%0A%09at+java.lang.ClassLoader.loadClass%28Unknown+Source%29%0D%0A%09at+java.lang.Class.forName0%28Native+Method%29%0D%0A%09at+java.lang.Class.forName%28Unknown+Source%29%0D%0A%09at+core.ANotherTest.main%28ANotherTest.java%3A18%29%0D%0A&codecontext=%09%09%7B%0D%0A%09%09%2F%2Fcode+for+making+connection+with+a+sqlite+database%0D%0A%09%09%09Class.forName%28%22org.sqlite.JDBC%22%29%3B%0D%0A%09%09%09Connection+connection%3Dnull%3B%0D%0A%09%09%09connection%3DDriverManager.getConnection%28%22jdbc%3Asqlite%3A%22%2B%22%2F%22%2B%22test.db%22%29%3B%0D%0A%09%09%09Statement+statement%3Dconnection.createStatement%28%29%3B%0D%0A%09%09%09String+create_query%3D%22create+table+History+%28+LinkID+INTEGER+primary+key%2C+Title+TEXT+not+null%2C+LinkURL+TEXT+not+null%29%3B%22%3B%0D%0A%09%09%09boolean+created%3Dstatement.execute%28create_query%29%3B%0D%0A%09%09%09System.out.println%28%22Succeeded%22%29%3B%0D%0A%09%09%7Dcatch%28Exception+exc%29%7B%0D%0A%09%09%09exc.printStackTrace%28%29%3B%0D%0A%09%09%7D%0D%0A%09%09%0D%0A&recentpagedata=http%3A%2F%2Flinuxsleuthing.blogspot.ca%2F2011%2F02%2Fcalculating-embedded-os-x-times.html%3D1.0";//%2Chttp%3A%2F%2Flinuxsleuthing.blogspot.com%2F2011%2F02%2Fcalculating-embedded-os-x-times.html%3D1.0%2Chttp%3A%2F%2Fproductforums.google.com%2Fforum%2F%23%21msg%2Fchrome%2Fc4lWKkfjOdc%2Fra0z11nR0_oJ%3D1.0%2Chttp%3A%2F%2Fproductforums.google.com%2Fd%2Fmsg%2Fchrome%2Fc4lWKkfjOdc%2Fra0z11nR0_oJ%3D1.0%2Chttp%3A%2F%2Flinuxsleuthing.blogspot.ca%2F%3D1.0%2Chttp%3A%2F%2Fstackoverflow.com%2Fquestions%2F18477800%2Fpcommandlink-dont-work-in-pdatatable%3D1.0%2Chttp%3A%2F%2Flinuxsleuthing.blogspot.ca%2F2011%2F06%2Fdecoding-google-chrome-timestamps-in.html%3D1.0%2Chttp%3A%2F%2Flinuxsleuthing.blogspot.com%2F2011%2F06%2Fdecoding-google-chrome-timestamps-in.html%3D1.0%2Chttp%3A%2F%2Fwww.prothom-alo.com%2Finternational%2Farticle%2F42614%2F%25E0%25A6%25B8%25E0%25A6%25BF%25E0%25A6%25B0%25E0%25A6%25BF%25E0%25A6%25AF%25E0%25A6%25BC%25E0%25A6%25BE%25E0%25A6%25AF%25E0%25A6%25BC_%25E0%25A6%25AA%25E0%25A6%25B6%25E0%25A7%258D%25E0%25A6%259A%25E0%25A6%25BF%25E0%25A6%25AE%25E0%25A6%25BE_%25E0%25A6%25B8%25E0%25A6%25BE%25E0%25A6%25AE%25E0%25A6%25B0%25E0%25A6%25BF%25E0%25A6%2595_%25E0%25A6%2585%25E0%25A6%25AD%25E0%25A6%25BF%25E0%25A6%25AF%25E0%25A6%25BE%25E0%25A6%25A8_%25E0%25A6%25AF%25E0%25A7%2587%25E0%25A6%2595%25E0%25A7%258B%25E0%25A6%25A8%25E0%25A7%258B_%25E0%25A6%25AE%25E0%25A7%2581%25E0%25A6%25B9%25E0%25A7%2582%25E0%25A6%25B0%25E0%25A7%258D%25E0%25A6%25A4%25E0%25A7%2587%3D1.0%2Chttp%3A%2F%2Fwww.prothom-alo.com%2Ftechnology%2Farticle%2F42533%2F%25E0%25A6%25A8%25E0%25A6%25A4%25E0%25A7%2581%25E0%25A6%25A8%25E0%25A6%25AD%25E0%25A6%25BE%25E0%25A6%25AC%25E0%25A7%2587_%25E0%25A6%2587%25E0%25A6%25AF%25E0%25A6%25BC%25E0%25A6%25BE%25E0%25A6%25B9%25E0%25A7%2581%25E0%25A6%25B0_%25E0%25A6%2585%25E0%25A6%25AC%25E0%25A7%258D%25E0%25A6%25AF%25E0%25A6%25AC%25E0%25A6%25B9%25E0%25A7%2582%25E0%25A6%25A4_%25E0%25A6%2587_%25E0%25A6%25AE%25E0%25A7%2587%25E0%25A6%2587%25E0%25A6%25B2%3D0.006737946999085468%2Chttp%3A%2F%2Fwww.prothom-alo.com%2Fsports%2Farticle%2F42577%2F%25E0%25A6%259A%25E0%25A7%258B%25E0%25A6%259F_%25E0%25A6%259B%25E0%25A6%25BF%25E0%25A6%259F%25E0%25A6%2595%25E0%25A7%2587_%25E0%25A6%25A6%25E0%25A6%25BF%25E0%25A6%25B2_%25E0%25A6%25B8%25E0%25A6%25BE%25E0%25A6%2595%25E0%25A6%25BF%25E0%25A6%25AC%25E0%25A6%2595%25E0%25A7%2587%3D0.0024787521766663585%2Chttp%3A%2F%2Fwww.prothom-alo.com%2Fopinion%2Farticle%2F42541%2F%25E0%25A6%2586%25E0%25A6%2593%25E0%25A6%25AF%25E0%25A6%25BC%25E0%25A6%25BE%25E0%25A6%25AE%25E0%25A7%2580_%25E0%25A6%25B2%25E0%25A7%2580%25E0%25A6%2597_%25E0%25A6%259C%25E0%25A6%25BF%25E0%25A6%25A4%25E0%25A6%25AC%25E0%25A7%2587_%25E0%25A6%25A8%25E0%25A6%25BE_%25E0%25A6%25AC%25E0%25A6%25BF%25E0%25A6%258F%25E0%25A6%25A8%25E0%25A6%25AA%25E0%25A6%25BF%3D0.0024787521766663585%2Chttp%3A%2F%2Fwww.prothom-alo.com%2Fnational%2Farticle%2F42490%2F%25E0%25A6%25A4%25E0%25A6%25A4%25E0%25A7%258D%25E0%25A6%25A4%25E0%25A7%258D%25E0%25A6%25AC%25E0%25A6%25BE%25E0%25A6%25AC%25E0%25A6%25A7%25E0%25A6%25BE%25E0%25A6%25AF%25E0%25A6%25BC%25E0%25A6%2595%25E0%25A7%2587%25E0%25A6%25B0_%25E0%25A6%259C%25E0%25A6%25A8%25E0%25A7%258D%25E0%25A6%25AF_%25E0%25A6%2587%25E0%25A6%2589%25E0%25A6%25A8%25E0%25A7%2582%25E0%25A6%25B8%25E0%25A6%2595%25E0%25A7%2587_%25E0%25A6%25A7%25E0%25A6%25B0%25E0%25A7%2587%25E0%25A6%259B%25E0%25A7%2587%25E0%25A6%25A8_%25E0%25A6%2596%25E0%25A6%25BE%25E0%25A6%25B2%25E0%25A7%2587%25E0%25A6%25A6%25E0%25A6%25BE%3D0.0024787521766663585%2Chttp%3A%2F%2Fwww.prothom-alo.com%2Fnational%2Farticle%2F42696%2F%25E0%25A6%25B8%25E0%25A6%25BE%25E0%25A6%25B9%25E0%25A6%25B8%25E0%25A7%2580_%25E0%25A6%259D%25E0%25A6%25B0%25E0%25A6%25A8%25E0%25A6%25BE%25E0%25A6%25B0_%25E0%25A6%25B8%25E0%25A6%2599%25E0%25A7%258D%25E0%25A6%2597%25E0%25A7%2587_%25E0%25A6%258F%25E0%25A6%25AE%25E0%25A6%25A8_%25E0%25A6%2586%25E0%25A6%259A%25E0%25A6%25B0%25E0%25A6%25A3_%25E0%25A6%2595%25E0%25A7%2587%25E0%25A6%25A8%3D0.0024787521766663585%2Chttp%3A%2F%2Fwww.timestampconvert.com%2F%3Fgo2%3Dtrue%26offset%3D6%26timestamp%3D13021960235%26Submit%3D%2B%2B%2B%2B%2B%2BConvert%2Bto%2BDate%2B%2B%2B%2B%2B%2B%3D0.0024787521766663585%2Chttp%3A%2F%2Fwww.timestampconvert.com%2F%3Fgo2%3Dtrue%26offset%3D6%26timestamp%3D13021960235373859%26Submit%3D%2B%2B%2B%2B%2B%2BConvert%2Bto%2BDate%2B%2B%2B%2B%2B%2B%3D0.0024787521766663585%2Chttp%3A%2F%2Fwww.timestampconvert.com%2F%3D0.0024787521766663585%2Chttp%3A%2F%2Fstackoverflow.com%2Fquestions%2F12244322%2Fphp-generating-13-digit-timestamp-like-mktime%3D0.0024787521766663585%2Chttp%3A%2F%2Fpenguin.ewu.edu%2F%7Ebojianxu%2Ftalks%2Ftalk-icde2009.pdf%3D0.0024787521766663585%2Chttps%3A%2F%2Fmail.google.com%2Fmail%2Fu%2F0%2F%3Fshva%3D1%23search%2Fwcre%2F13f9b388031c6f18%3D0.0024787521766663585";
			URL url = new URL(this.web_service_url);
			HttpURLConnection httpconn = (HttpURLConnection) url
					.openConnection();
			httpconn.setRequestMethod("GET");
			System.out.println(httpconn.getResponseMessage());
			if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader breader = new BufferedReader(
						new InputStreamReader(httpconn.getInputStream()));
				String line = null;
				while ((line = breader.readLine()) != null) {
					jsonResponse += line;
				}
				// System.out.println(jsonResponse);
				// display_json_results(jsonResponse);
			}
		} catch (Exception exc) {
			System.out.println(exc.getMessage());
		}
		return jsonResponse;
	}

	protected ArrayList<Result> convert_json_to_results(String jsonResponse) {
		// code for displaying JSON results
		//System.out.println(jsonResponse);
		ArrayList<Result> resultColl = new ArrayList<Result>();
		try {
			JSONParser parser = new JSONParser();
			JSONArray items = (JSONArray) parser.parse(jsonResponse);

			for (int i = 0; i < items.size(); i++) {
				JSONObject jsonObj = (JSONObject) items.get(i);
				Result result = new Result();
				result.rank = Integer.parseInt(jsonObj.get("rank").toString());
				try{
				result.matchedKeyWords = jsonObj.get("title").toString();
				}catch(Exception e){}
				try {
					result.completeCode = jsonObj.get("example").toString();
				} catch (Exception e) {
					// TODO: handle exception
				}
				try{
				result.totalScore = Double.parseDouble(jsonObj
						.get("totalscore").toString());
				}catch(Exception e){
					
				}
				try{
				result.structural_relevance = Double.parseDouble(jsonObj.get(
						"structure").toString());
				}catch(Exception e){}
				try{
				result.content_relevance = Double.parseDouble(jsonObj.get(
						"lexical").toString());
				}catch(Exception e){
					
				}
				try{
				result.handler_quality = Double.parseDouble(jsonObj.get(
						"quality").toString());
				}catch(Exception e){
					
				}
				resultColl.add(result);
//				System.out.println(result.structural_relevance + "\t"
//						+ result.content_relevance + "\t"
//						+ result.handler_quality);
				// System.out.println(jsonObj.get("rank")+" "+jsonObj.get("title")+" "+jsonObj.get("resultURL"));
				if (i == TOTAL_ITEMS)
					break;
			}
		} catch (Exception exc) {
			System.out.println(exc.getMessage());
			exc.printStackTrace();
		}
		return resultColl;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stubs
		int exceptionID = 35;
		String queryException="SocketException";
		String searchQuery = "SocketException Socket";
		String codecontext = ContextCodeLoader.loadContextCode(exceptionID);
		System.out.println(codecontext);
		MyClient myclient = new MyClient(queryException, searchQuery, codecontext,5);
		// MyClient myclient=new MyClient();
		long start_time = System.currentTimeMillis();
		ArrayList<Result> results = myclient.collect_search_results();
		long end_time = System.currentTimeMillis();
		System.out.println("Time elapsed:" + (end_time - start_time) / 1000.0);
		System.out.println("Results extracted:" + results.size());
	}
}
