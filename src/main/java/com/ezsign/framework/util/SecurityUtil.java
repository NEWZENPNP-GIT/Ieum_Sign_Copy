package com.ezsign.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

public class SecurityUtil {

    public static void main(String[] args) throws Exception {
    	String data = SecurityUtil.getinstance().aesDecode("");
    	System.out.println("data >>"+data);
    }

	private static SecurityUtil instance = null;
	private static final String _key     = "!@#sbwpsvldpsvl09";
	private static String iv             = null;
    private static Key keySpec           = null;
    private static final String _bit_key = "3038498702555237000";

    public static SecurityUtil getinstance() throws UnsupportedEncodingException{
    	
    	if (instance == null) instance = new SecurityUtil();

    	if (StringUtils.isEmpty(iv)) iv = _key.substring(0, 16);

    	
    	if (keySpec == null) {
    		byte[] keyBytes = new byte[16];
            byte[] b        = _key.getBytes("UTF-8");
            int len         = b.length;

            if (len > keyBytes.length) len = keyBytes.length;

            System.arraycopy(b, 0, keyBytes, 0, len);
            keySpec = new SecretKeySpec(keyBytes, "AES");
    	}
    	return instance;
    }

    /**
     *
     * 개인식별번호 암호화
     *
     * @param str
     * @return
     * @throws java.io.UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String aesEncode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException,
                                                     NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
                                                     IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
 
        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
        String enStr     = new String(Base64.encodeBase64(encrypted));
        return enStr;
    }
 

    /**
     *
     * 개인식별번호 복호화
     *
     * @param str
     * @return
     * @throws java.io.UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String aesDecode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException,
                                                     NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
                                                     IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));
 
        byte[] byteStr = Base64.decodeBase64(str.getBytes());
        return new String(c.doFinal(byteStr),"UTF-8");
    }
    
    
    public static String encrypt(String planText) {
    	try{
    		MessageDigest md = MessageDigest.getInstance("SHA-256");
    		md.update(planText.getBytes());
    		byte byteData[] = md.digest();

    		StringBuilder sb = new StringBuilder();

            for (byte byteDatum : byteData) sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));

    		StringBuilder hexString = new StringBuilder();

            for (byte byteDatum : byteData) {
                String hex = Integer.toHexString(0xff & byteDatum);

                if (hex.length() == 1) hexString.append('0');

                hexString.append(hex);
            }
    		return hexString.toString();
    	} catch(Exception e) {
    		e.printStackTrace();
    		throw new RuntimeException();
    	}
    }

    public static String bitEncryption(String data) {
        long cryptograph = Long.parseLong(data) ^ Long.parseLong(_bit_key);
        return Long.toString(cryptograph);
    }
    /**
     * <p>
     * 원본 파일을 암호화해서 대상 파일을 만든다.
     * </p>
     *
     * @param source
     *            원본 파일
     * @param dest
     *            대상 파일
     * @throws Exception
     */
    public void encryptFile(File source, File dest) throws Exception {
        crypt(Cipher.ENCRYPT_MODE, source, dest);
    }
 
    /**
     * <p>
     * 원본 파일을 복호화해서 대상 파일을 만든다.
     * </p>
     *
     * @param source
     *            원본 파일
     * @param dest
     *            대상 파일
     * @throws Exception
     */
    public void decryptFile(File source, File dest) throws Exception {
        crypt(Cipher.DECRYPT_MODE, source, dest);
    }
    
    /**
     * <p>
     * 원본 파일을 암/복호화해서 대상 파일을 만든다.
     * </p>
     *
     * @param mode
     *            암/복호화 모드
     * @param source
     *            원본 파일
     * @param dest
     *            대상 파일
     * @throws Exception
     */
    private void crypt(int mode, File source, File dest) throws Exception {
        Cipher cipher       = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(mode, keySpec);
        InputStream input   = null;
        OutputStream output = null;
        try {
            input  = new BufferedInputStream(Files.newInputStream(source.toPath()));
            output = new BufferedOutputStream(Files.newOutputStream(dest.toPath()));
            byte[] buffer = new byte[1024];
            int read;

            while ((read = input.read(buffer)) != -1) output.write(cipher.update(buffer, 0, read));

            output.write(cipher.doFinal());
        } finally {
            if (output != null) {
                try { output.close(); }
                catch (IOException ie) { }
            }
            if (input != null) {
                try { input.close(); }
                catch (IOException ie) { }
            }
        }
    }
    
    /**
     * <p>
     * 문자열을 바이트배열로 바꾼다.
     * </p>
     *
     * @param digits
     *            문자열
     * @param radix
     *            진수
     * @return
     * @throws IllegalArgumentException
     * @throws NumberFormatException
     */
    public static byte[] toBytes(String digits, int radix) throws IllegalArgumentException, NumberFormatException {
        if (digits == null) return null;

        if (radix != 16 && radix != 10 && radix != 8) throw new IllegalArgumentException("For input radix: \"" + radix + "\"");

        int divLen = (radix == 16) ? 2 : 3;
        int length = digits.length();
        if (length % divLen == 1) throw new IllegalArgumentException("For input string: \"" + digits + "\"");

        length = length / divLen;
        byte[] bytes = new byte[length];
        for (int i=0; i<length; i++) {
            int index = i * divLen;
            bytes[i] = (byte) (Short.parseShort(digits.substring(index, index + divLen), radix));
        }
        return bytes;
    }
    
}
