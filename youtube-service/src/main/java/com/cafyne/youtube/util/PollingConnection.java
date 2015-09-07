package com.cafyne.youtube.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.DatatypeConverter;

import com.cafyne.common.property.cache.CafyneConfig;

public class PollingConnection {
	
	
	
	
	public static void main(String... args) throws IOException {

		String username = CafyneConfig.getProperty("GNIP_DATA_COLLECTOR_USERNAME");
		String password = CafyneConfig.getProperty("GNIP_DATA_COLLECTOR_PASSWORD");
		String charset = "UTF-8";

		//	Ensure that your stream format matches the data format you intend to use (e.g. '.xml' or '.json')

		//	Expected Enterprise Data Collector URL formats:
		//		JSON:	https://<host>.gnip.com/data_collectors/<data_collector_id>/activities.json
		//		XML:	https://<host>.gnip.com/data_collectors/<data_collector_id>/activities.xml

		String dataCollectorURL = "https://cafyne.gnip.com/data_collectors/12/activities.json";

		HttpURLConnection connection = null;
		InputStream inputStream = null;

		try {
			connection = getConnection(dataCollectorURL, username, password);

			inputStream = connection.getInputStream();
			int responseCode = connection.getResponseCode();

			if (responseCode >= 200 && responseCode <= 299) {

				BufferedReader reader = new BufferedReader(new InputStreamReader((inputStream), charset));
				String line = reader.readLine();

				while(line != null){
					System.out.println(line);
					line = reader.readLine();
				}
			} else {
				handleNonSuccessResponse(connection);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (connection != null) {
				handleNonSuccessResponse(connection);
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	private static void handleNonSuccessResponse(HttpURLConnection connection) throws IOException {
		int responseCode = connection.getResponseCode();
		String responseMessage = connection.getResponseMessage();
		System.out.println("Non-success response: " + responseCode + " -- " + responseMessage);
	}

	private static HttpURLConnection getConnection(String urlString, String username, String password) throws IOException {
		URL url = new URL(urlString);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setReadTimeout(1000 * 60 * 60);
		connection.setConnectTimeout(1000 * 10);

		connection.setRequestProperty("Authorization", createAuthHeader(username, password));

		return connection;
	}

	private static String createAuthHeader(String username, String password) throws UnsupportedEncodingException {
		String authToken = username + ":" + password;
		return "Basic " + DatatypeConverter.printBase64Binary(authToken.getBytes());
	}}
