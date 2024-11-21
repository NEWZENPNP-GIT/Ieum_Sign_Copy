package com.ezsign.push.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class FCM {
	
	public static class PushType {
		
		/** TO_TYPE - CANDY */
		public static final String CANDY 			= "001";
		/** TO_TYPE - TOPIC */
		public static final String TOPIC 			= "002";
		/** TO_TYPE - TOKEN */
		public static final String TOKEN 			= "003";
		
		/** PUSH_TYPE - 공지사항 */
		public static final String NOTICE 			= "001";
		/** PUSH_TYPE - 문의글 답변 */
		public static final String ANSWER_QUESTION 	= "002";
		/** PUSH_TYPE - 켈린더 내용 등록 */
		public static final String SCHEDULE_REGISTRATION = "003";
		/** PUSH_TYPE - 연말정산 종료일 임박 */
		public static final String END_LIMIT 		= "004";
		/** PUSH_TYPE - 연말정산 1차 마감 */
		public static final String FIRST_DEADLINE 	= "005";
		
	}
	
	final static private String FCM_URL 	= "https://fcm.googleapis.com/fcm/send";
	final static private String ANDROID_SERVER_KEY 	= "AAAAFAjRu-k:APA91bFQVHO9bkY22RoebfoTvvD9DJWAz5kwOJDfww-O8dQhwm6VnAUSMCWA755vc6PYPDGreeinvOGlso1dK5yGwMxQvQr58zF1VZnMC3axcXbelZmkgPRj0ssXtHRxCAs7hz35O3Cd";
	final static private String IOS_SERVER_KEY 	= "AAAAFAjRu-k:APA91bFQVHO9bkY22RoebfoTvvD9DJWAz5kwOJDfww-O8dQhwm6VnAUSMCWA755vc6PYPDGreeinvOGlso1dK5yGwMxQvQr58zF1VZnMC3axcXbelZmkgPRj0ssXtHRxCAs7hz35O3Cd";
	
	/**
	 * Method to send push notification to Android FireBased Cloud messaging
	 * Server.
	 * 
	 * @param tokenId
	 *            Generated and provided from Android Client Developer
	 * 
	 * @param server_key
	 *            Key which is Generated in FCM Server
	 * 
	 * @param message
	 *            which contains actual information.
	 */

	public static int send_FCM_Notification(String tokenId, String title, String message) {

		int status = 0;
		
		try {

			// Create URL instance.

			URL url = new URL(FCM_URL);

			// create connection.

			HttpURLConnection conn;

			conn = (HttpURLConnection) url.openConnection();

			conn.setUseCaches(false);

			conn.setDoInput(true);

			conn.setDoOutput(true);

			// set method as POST or GET

			conn.setRequestMethod("POST");

			// pass FCM server key

			conn.setRequestProperty("Authorization", "key=" + ANDROID_SERVER_KEY);
			

			// Specify Message Format

			conn.setRequestProperty("Content-Type", "application/json");

			// Create JSON Object & pass value

			JSONObject infoJson = new JSONObject();

			infoJson.put("title", title);

			infoJson.put("body", message);

			JSONObject json = new JSONObject();

			json.put("to", tokenId.trim());

			infoJson.put("priority", "high");
			infoJson.put("sound", "default");
			json.put("notification", infoJson);
			
			
			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());

			wr.write(json.toString());

			wr.flush();
			
			wr.close();
			
			if (null != conn) {

				status = conn.getResponseCode();

			}

			if (status != 0) {

				if (status == 200) {

					// SUCCESS message

					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					
					System.out.println("Android Notification Response : "
							+ reader.readLine());

					reader.close();
					
				} else if (status == 401) {

					// client side error

					System.out.println("Notification Response : TokenId : "
							+ tokenId + " Error occurred :");

				} else if (status == 501) {

					// server side error

					System.out
							.println("Notification Response : [ errorCode=ServerError ] TokenId : "
									+ tokenId);

				} else if (status == 503) {

					// server side error

					System.out
							.println("Notification Response : FCM Service is Unavailable  TokenId : "
									+ tokenId);

				}

			}

		} catch (MalformedURLException mlfexception) {

			// Prototcal Error

			System.out.println("Error occurred while sending push Notification!.." + mlfexception.getMessage());

		} catch (IOException mlfexception) {

			// URL problem
			System.out.println("Reading URL, Error occurred while sending push Notification!.." + mlfexception.getMessage());

		} catch (JSONException jsonexception) {

			// Message format error
			System.out.println("Message Format, Error occurred while sending push Notification!.." + jsonexception.getMessage());

		} catch (Exception exception) {

			// General Error or exception.
			System.out.println("Error occurred while sending push Notification!.." + exception.getMessage());

		} 

		return status;
		
	}

	
	public static void main(String[] args) {
		//send_FCM_Notification("fgXb2FzhYMo:APA91bEG9dttFszXk4Mf80jwsqD27nvNYFQvjYb0XYKJ255zgeIOdgb1UNirNCx9-IXO2QYB5rmoh1NVxajVKLo0bN_D_eU7YvEqAeqqoxzGgtcg3WHQXV60csZIZQzTs36cm_g_4NH9", "Hellow", "android");
		//send_FCM_Notification("ea4OVneTteo:APA91bF_8_KzYPOxtKeOF6IEENhgxjPfQf7GSDa_S0W4JALVanLLXC37asGEVrU26LIze5EpTyThnJy_wdxUP6_L_9fE5lxyWS9hLXsvqxI6pkaUW2jX1UpRRhyWAteW3BD6ZoboazsB", "뭉글뭉글", "ios");
	}
}
