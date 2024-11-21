package com.ezsign.framework.util;

/**
 * @Class Name  : com.newzenpnp.framework.util.TokenUtil
 * @Description :
 * @Modification Information  
 * @
 * @     수정일                         수정자             수정내용
 * @ -------------     ---------   ---------------------------------
 * @  2015. 8. 10.      유지운                 최초생성
 * 
 * @Company : Newzen Pnp. Inc
 * @Author  : 유지운
 * @Date    : 2015. 8. 10. 오후 4:21:02
 * @version : 1.0
 */

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import tradesign.crypto.provider.JeTS;
import tradesign.pki.util.JetsUtil;

public class PKIUtil {
	
	public static void main(String[] args) throws Exception {
		
	}
	
	public static String getFileHash(String src) throws Exception {
		String result = "";
		
		JeTS.installProvider(System.getProperty("ktnet.properties.file"));
		MessageDigest md = MessageDigest.getInstance("SHA256", "JeTS");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(src);
			byte[] string = new byte[fis.available()];
			fis.read(string);
			byte[] hash2 = md.digest(string);
			result = JetsUtil.toBase64String(hash2);
			System.out.println("Digest Hash Value : \n" + JetsUtil.toBase64String(hash2));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(fis != null) {
				try{
					fis.close();							
				} catch (Exception e) {}
			}
		}
		return result;
	}
	
	public static String extractFileHashSHA256(String filename) throws Exception { 
		String SHA = "";
		int buff = 16384;
		try { 
			RandomAccessFile file = new RandomAccessFile(filename, "r"); 
			MessageDigest hashSum = MessageDigest.getInstance("SHA-256"); 
			byte[] buffer = new byte[buff]; 
			byte[] partialHash = null; 
			long read = 0; 

			long offset = file.length();
			int unitsize; 
			while (read < offset) { 
				unitsize = (int) (((offset - read) >= buff) ? buff : (offset - read)); file.read(buffer, 0, unitsize);
				hashSum.update(buffer, 0, unitsize);
				read += unitsize; 
			} 
			file.close(); 
			partialHash = new byte[hashSum.getDigestLength()];
			partialHash = hashSum.digest();
			StringBuffer sb = new StringBuffer();
			for(int i = 0 ; i < partialHash.length ; i++){ 
				sb.append(Integer.toString((partialHash[i]&0xff) + 0x100, 16).substring(1));
			} 
			SHA = sb.toString(); 
		} catch (FileNotFoundException e)	{
			e.printStackTrace(); 
		}
		return SHA;
	}
	
}
