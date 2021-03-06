package com.wireshout.snipe4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SnipeInstance {
	private String url;
	private String apiKey;
	
	public SnipeInstance(String endpoint, String key) {
		url = endpoint;
		apiKey = key;
	}
	
	public String makeGetRequest(String endpoint) {
		return makeGetRequest(endpoint, null);
	}
	
	public String makeGetRequest(String endpoint, String queryString) {
		HttpResponse response = null;
		String retResult = null;
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request;
			if(queryString == null || queryString.isEmpty()) {
				System.out.println("* GET Request: " + url + "/" + endpoint);
				request = new HttpGet(url + "/" + endpoint);
			} else {
				System.out.println("* GET Request: " + url + "/" + endpoint + "?" + queryString);
				request = new HttpGet(url + "/" + endpoint + "?" + queryString);
			}

			request.addHeader("Authorization", "Bearer " + apiKey);
			request.addHeader("Accept", "application/json");

			long startTime = System.currentTimeMillis();
			response = client.execute(request);
			System.out.println("Response Time: " + (System.currentTimeMillis() - startTime) + "ms");
			System.out.println("Response Code: " + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
			
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			retResult = result.toString();
			System.out.println("Response Body: " + retResult);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return retResult;
	}
	
	public SnipeResponse makePatchRequest(String endpoint, List<NameValuePair> body) {
		SnipeResponse response;
		try {
			HttpClient client = HttpClientBuilder.create().build();
			System.out.println("* PTH Request: " + url + "/" + endpoint);
			HttpPatch request = new HttpPatch(url + "/" + endpoint);
			request.setEntity(new UrlEncodedFormEntity(body));
	
			request.addHeader("Authorization", "Bearer " + apiKey);
			request.addHeader("Accept", "application/json");

			long startTime = System.currentTimeMillis();
			response = new SnipeResponse(client.execute(request));
			System.out.println("Response Time: " + (System.currentTimeMillis() - startTime) + "ms");
			
			return response;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public SnipeResponse makePostRequest(String endpoint, List<NameValuePair> body) {
		SnipeResponse response;
		try {
			HttpClient client = HttpClientBuilder.create().build();
			System.out.println("* PST Request: " + url + "/" + endpoint);
			HttpPost request = new HttpPost(url + "/" + endpoint);
			request.setEntity(new UrlEncodedFormEntity(body));
	
			request.addHeader("Authorization", "Bearer " + apiKey);
			request.addHeader("Accept", "application/json");

			long startTime = System.currentTimeMillis();
			response = new SnipeResponse(client.execute(request));
			System.out.println("Response Time: " + (System.currentTimeMillis() - startTime) + "ms");
			
			return response;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean makeDeleteRequest(String endpoint) {
		SnipeResponse response;
		try {
			HttpClient client = HttpClientBuilder.create().build();
			System.out.println("* DEL Request: " + url + "/" + endpoint);
			HttpDelete request = new HttpDelete(url + "/" + endpoint);
	
			request.addHeader("Authorization", "Bearer " + apiKey);
			request.addHeader("Accept", "application/json");

			long startTime = System.currentTimeMillis();
			response = new SnipeResponse(client.execute(request));
			System.out.println("Response Time: " + (System.currentTimeMillis() - startTime) + "ms");
			
			return response.wasSuccessful();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
