package com.jingdong.app;

import java.util.List;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

//import android.net.http.*;

public class Download {

	private static final int CONNECTION_TIMEOUT = 5000; /* 5 seconds */
	private static final int SOCKET_TIMEOUT = 5000; /* 5 seconds */
	private static final long MCC_TIMEOUT = 5000; /* 5 seconds */

	public static void main(String args[]) {
		try {
			test();
			System.out.println(get("http://192.168.3.4:9999/hhh/"));
			// System.out.println(sendGet("http://www.baidu.com"));
			// test();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void test() {
		System.out.println(get("http://www.baidu.com"));

		Map params = new HashMap();
		params.put("name", "zhipeng");
		params.put("age", "16");
		System.out.println(get("http://www.baidu.com", params));
	}

	public static String get(String URL) {
		return get(URL, new HashMap());
	}

	public static String get(String URL, Map params) {
		String result = "";
		HttpGet httpGet = null;

		// URIBuilder builder = null;

		String URI = setParams(params);
		// System.out.println(URL + "?" + URI);
		httpGet = new HttpGet(URL + "?" + URI);

		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
		httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, SOCKET_TIMEOUT);
		httpClient.getParams().setLongParameter(ConnManagerPNames.TIMEOUT, MCC_TIMEOUT);

		HttpResponse response = null;
		try {

			response = httpClient.execute(httpGet);
		} catch (Exception e) {
			// e.printStackTrace();
			Logger.getLogger(Configure.loggerName).severe(Configure.logTag
					+ String.format("Download failed, url: %s, uri:%s error: %s", URL, URI, e.getMessage()));
			System.out.println(String.format("Download failed, url: %s, uri:%s error: %s", URL, URI, e.getMessage()));
		}

		try {
			HttpEntity httpEntity = response.getEntity();
			InputStream inputStream = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line = "";
			while (null != (line = reader.readLine())) {
				result += (line + "\n");
			}
			ArrayList arrayList = new ArrayList(4);
			// System.out.println(result);
			// result = "358239051596619-020000000000,2782638,1,10"
			inputStream.close();
			reader.close();
		} catch (Exception e) {
			// e.printStackTrace();
			Logger.getLogger(Configure.loggerName).severe(Configure.logTag
					+ String.format("Download read response failed, url: %s error: %s", URL, e.getMessage()));
		}

		return result;
	}

	/*
	 * 没有做转义处理.
	 */
	private static String setParams(Map params) {
		String URI = "";
		Iterator<Map.Entry<String, Object>> entries = params.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<String, Object> entry = entries.next();
			// System.out.println("Key = " + entry.getKey() + ", Value = " +
			// entry.getValue());
			// builder.addParameter(entry.getKey(), (String) entry.getValue());
			URI += entry.getKey() + "=" + (String) entry.getValue() + "&";
		}
		return URI;
	}

	public static String post(String serverURL) {
		String result = "";
		HttpPost httpPost = new HttpPost(serverURL);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		try {

			response = httpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			try {
				InputStream inputStream = httpEntity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				String line = "";
				while (null != (line = reader.readLine())) {
					result += line;
				}
				ArrayList arrayList = new ArrayList(4);
				System.out.println(result);
				// result = "358239051596619-020000000000,2782638,1,10"
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	// public static void test() {
	// AndroidHttpClient ht = AndroidHttpClient.newInstance("test user agent");
	// HttpGet request = new HttpGet("");
	// HttpResponse response = null;
	// try {
	// response = ht.execute(request);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// BufferedReader rd = null;
	// try {
	// rd = new BufferedReader(new
	// InputStreamReader(response.getEntity().getContent()));
	// } catch (IllegalStateException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// StringBuffer result = new StringBuffer();
	// String line = "";
	// try {
	// while ((line = rd.readLine()) != null) {
	// result.append(line);
	// }
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// System.out.println(result.toString());
	// }

	// HTTP GET request
	private static String sendGet(String url) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", "My Test Agent");

		HttpResponse response = client.execute(request);

		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
	}

	// HTTP POST request
	private void sendPost() throws Exception {

		String url = "https://selfsolve.apple.com/wcResults.do";

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		// add header
		post.setHeader("User-Agent", "My test agent.");

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("sn", "C02G8416DRJM"));
		urlParameters.add(new BasicNameValuePair("cn", ""));
		urlParameters.add(new BasicNameValuePair("locale", ""));
		urlParameters.add(new BasicNameValuePair("caller", ""));
		urlParameters.add(new BasicNameValuePair("num", "12345"));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		HttpResponse response = client.execute(post);
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + post.getEntity());
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		System.out.println(result.toString());

	}

}
