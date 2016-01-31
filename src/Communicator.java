import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Communicator {
	private static final String USER_AGENT = "Mozilla/5.0";
	private static String apikey = "45470c01-3a48-4ded-9141-fe420c7f7b59";
	private static String url_extract = "https://api.havenondemand.com/1/api/sync/extractentities/v2";
	private static String charset = java.nio.charset.StandardCharsets.UTF_8
			.name();
	private static String url_vizgrad = "http://www.vizgr.org/historical-events/search.php";

	public static String search(int beginYear, int beginMonth, int beginDay,
			int endYear, int endMonth, int endDay) {
		StringBuilder query = new StringBuilder("?");
		query.append("begin_date=" + new Integer(beginYear).toString());

		if (beginMonth < 10)
			query.append("0");
		query.append(new Integer(beginMonth).toString());

		if (beginDay < 10)
			query.append("0");
		query.append(new Integer(beginDay).toString() + "&end_date="
				+ new Integer(endYear).toString());

		if (endMonth < 10)
			query.append("0");
		query.append(new Integer(endMonth).toString());

		if (endDay < 10)
			query.append("0");
		query.append(new Integer(endDay).toString() + "&format=json");
		query.append("&html=true");
		System.out.println(query);

		StringBuffer response = new StringBuffer();
		try {

			getRequest(query.toString(), response, url_vizgrad);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response.toString();

	}

	public static String extract(String txt) {
		// standardize in ConceptNet format

		String query;

		StringBuffer response = new StringBuffer();
		try {

			query = String
					.format("?text=%s&entity_type=people_eng&entity_type=places_eng&apikey=%s",
							URLEncoder.encode(txt, charset), apikey);
			getRequest(query, response, url_extract);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response.toString();
	}

	private static void getRequest(String query, StringBuffer response,
			String url) throws IOException, MalformedURLException,
			ProtocolException {
		HttpURLConnection connection = (HttpURLConnection) new URL(url + query)
				.openConnection();
		// connection.setDoOutput(true); // Triggers POST.
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept-Charset", charset);
		connection.setRequestProperty("User-Agent", USER_AGENT);
		connection.setRequestProperty("Content-Type", "application-json");

		/*
		 * try (OutputStream output = connection.getOutputStream()) {
		 * output.write(query.getBytes(charset)); }
		 */
		int responseCode = connection.getResponseCode();
		// System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			// System.out.println(response);
		} else {
			System.out.println("Bad request");
			System.out.println(connection);
		}
	}

	public String sendGET(URL url) throws Exception {
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application-json");
		int responseCode = con.getResponseCode();
		/*
		 * System.out.println("\nSending 'GET' request to URL : " + url);
		 * System.out.println("Response Code : " + responseCode);
		 */

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

}
