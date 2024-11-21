package com.ezsign.framework.util;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class HttpUtil {
	private final static String USER_AGENT  = "Mozilla/5.0";
	private final static int    BUFFER_SIZE = 1024 * 9;
	
	public static String HTTPPost(String url, String json) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		con.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");

		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post JSON Data : " + json);
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(json);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		System.out.println("Response Data : ");
		System.out.println(response.toString());
		//print result
		return response.toString();
	}

	public static String HTTPGet(String url, String json) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		con.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");

		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Get JSON Data : " + json);
		
		// Send get request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(json);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		System.out.println("Response Data : ");
		System.out.println(response.toString());
		//print result
		return response.toString();
	}
	
	/**
	 * Description  : request body 값을 추출한다.
	 * @Method Name : getRequestBody
	 * @param request : HttpServletRequest
	 * @return body String
	 */
	public static String getRequestBody(HttpServletRequest request) {		
		
		StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

		try{
			InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
            
		}catch (Exception e) {
			e.printStackTrace();			
		}
		
        return stringBuilder.toString();
    }

	public static void setResponsePdfView(HttpServletResponse response, File file) throws IOException {

		if(file.exists()) {
			response.setContentLength((int)file.length());
		}

		response.setContentType("application/pdf;charset=UTF-8");
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");

		ServletOutputStream out = response.getOutputStream();
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(file);
			byte[] outByte = new byte[BUFFER_SIZE];
			while(fis.read(outByte, 0, BUFFER_SIZE) != -1)
			{
				out.write(outByte, 0, BUFFER_SIZE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if(out != null) {
				out.flush();
				out.close();
			}
			if(fis != null) {
				try{
					fis.close();
				} catch (Exception e) {}
			}
			response.flushBuffer();
		}
	}

	public static void setResponseFile(HttpServletRequest request, HttpServletResponse response, File file, String saveName) throws IOException {

		if (saveName.toLowerCase().endsWith(".pdf")) {
			response.setContentType("application/pdf;charset=UTF-8");
		} else {
			response.setContentType("application/octet-stream;charset=UTF-8");
		}

		//response.setContentType("application/x-msdownload");
		
		if(file.exists())
			response.setContentLength((int)file.length());
		
		String disposition = "attachment";
		
		String userAgent = request.getHeader("User-Agent");
		if (userAgent.indexOf("MSIE 5.5") > -1) { // MS IE 5.5 이하
			response.setHeader("Content-Disposition", "filename=" + URLEncoder.encode(saveName, "UTF-8").replaceAll("\\+", "\\ ") + ";");
		} else if (userAgent.indexOf("MSIE") > -1) { // MS IE (보통은 6.x 이상 가정)
			response.setHeader("Content-Disposition", disposition+"; filename=" + java.net.URLEncoder.encode(saveName, "UTF-8").replaceAll("\\+", "\\ ") + ";");
		} else if (userAgent.indexOf("Trident") > -1) { // MS IE 11
			response.setHeader("Content-Disposition", disposition+"; filename=" + java.net.URLEncoder.encode(saveName, "UTF-8").replaceAll("\\+", "\\ ") + ";");
		} else if (userAgent.indexOf("Android") > -1) { // Android
			response.setHeader("Content-Disposition", disposition+"; filename=" + java.net.URLEncoder.encode(saveName, "UTF-8").replaceAll("\\+", "\\ ") + ";");
		} else if (userAgent.indexOf("iPhone") > -1) { // iPhone
			response.setHeader("Content-Disposition", disposition+"; filename=" + new String(saveName.getBytes("euc-kr"), "latin1").replaceAll("\\+", "\\ ") + ";");
		} else { // 모질라나 오페라
			response.setHeader("Content-Disposition", disposition+"; filename=" + new String(saveName.getBytes("euc-kr"), "latin1").replaceAll("\\+", "\\ ") + ";");
		}
		
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
		
	    
		ServletOutputStream out = response.getOutputStream();
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(file);
	        byte[] outByte = new byte[BUFFER_SIZE];
	        while(fis.read(outByte, 0, BUFFER_SIZE) != -1)
	        {
	        	out.write(outByte, 0, BUFFER_SIZE);
	        }
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if(out != null) {
				out.flush();
				out.close();
			}
			if(fis != null) {
				try{
					fis.close();
				} catch (Exception e) {}
			}
			response.flushBuffer();
		}
	}
	
	public static void HTTPSGet(String urlStr) {
		StringBuffer sb = new StringBuffer();
		try { 
			TrustManager[] trustAllCerts = new TrustManager[] {
					new X509TrustManager() {
						public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
							return null; 
						} 
						public void checkClientTrusted1(X509Certificate[] certs, String authType) {
							
						} 
						public void checkServerTrusted1(X509Certificate[] certs, String authType) {
							
						}
						@Override
						public void checkClientTrusted(X509Certificate[] arg0, String arg1)
								throws CertificateException {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void checkServerTrusted(X509Certificate[] chain, String authType)
								throws CertificateException {
							// TODO Auto-generated method stub
							
						}
					} 
			}; 
			SSLContext sc = SSLContext.getInstance("SSL"); 
			sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
			HttpsURLConnection .setDefaultSSLSocketFactory(sc.getSocketFactory()); 
			URL url = new URL(urlStr); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
			InputStreamReader in = new InputStreamReader((InputStream) conn.getContent());
			BufferedReader br = new BufferedReader(in);
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			} 
			System.out.println(sb.toString()); 
			br.close(); 
			in.close(); 
			conn.disconnect(); 
		} catch (Exception e) {
			System.out.println(e.toString()); 
		} 
	}
	
	// 전자계약 이관시 필요 유틸 추가(22.02.21)
	
	public static String base64Encode(String text) throws Exception{
		byte[] getBytes = text.getBytes();

		Encoder encoder = Base64.getEncoder();
		byte[] encodedBytes = encoder.encode(getBytes);
		
		String res = new String (encodedBytes);
		return res;
	}
	
	public static String base64Decode(String text) throws Exception{
		byte[] getBytes = text.getBytes();
		Decoder decoder = Base64.getDecoder();
		
		byte[] decodedBytes = decoder.decode(getBytes);
		
		String res = new String(decodedBytes, "UTF-8");

		return res;
	}
	


}
