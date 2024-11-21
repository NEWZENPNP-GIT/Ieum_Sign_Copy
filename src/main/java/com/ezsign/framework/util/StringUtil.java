package com.ezsign.framework.util;

/**
 * @Class Name  : kr.co.newzenpnp.common.util.StringUtil
 * @Description :
 * @Modification Information  
 * @
 * @     수정일                         수정자               수정내용
 * @ -------------     ---------   ---------------------------------
 * @  2017. 9. 21.      유지운                 최초생성
 **/

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.StringCharacterIterator;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class StringUtil {
	
	private static Logger logger = Logger.getLogger(StringUtil.class);
	
	private final static String EMPTY_STRING = "";
	private final static String NULL_STRING = "null";

	public static void main(String[] args) {
		try {
			System.out.println("result ="+StringUtil.getRandomNumber(4));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
     * Description  : null 또는 공백 문자열인 경우 치환 문자열로 변환하고 그렇지 않은 경우에는 trim하여 반환한다.
     * @Method Name : nvl
     * @param originString : 대상 String
     * @param replaceString : 변환 String
     * @return
     */
    public static String nvl(String originString, String replaceString) {
        return (originString == null || EMPTY_STRING.equals(originString) || NULL_STRING.equals(originString)) ? replaceString : originString.trim();
    }
    
    
    /**
     * Description  : String값을 Integer로 변환한다.
     * @Method Name : strPaserInt
     * @param paramStr
     * @return
     */
    public static int strPaserInt(String paramStr){
    	int resultInt = 0;
    	
    	if(nvl(paramStr,"").equals("")){
    		resultInt = 0;
    	}else{
    		resultInt = Integer.parseInt(paramStr);
    	}
    	
    	return resultInt;
    }

    /**
     * Description  : String값을 Long로 변환한다.
     * @Method Name : strPaserInt
     * @param paramStr
     * @return
     */
    public static long strPaserLong(String paramStr){
    	long resultInt = 0;
    	
    	if(nvl(paramStr,"").equals("")){
    		resultInt = 0;
    	}else{
    		resultInt = Long.parseLong(paramStr);
    	}
    	
    	return resultInt;
    }
    

    private static StringUtil stringUtilins = null;
    
	/**
	 * String.substring(int start, int end) 대체
	 * NullPointException 방지
	 */
	public static String substring(String src, int start, int end){
		if(src == null || "".equals(src) || start > src.length() || start > end || start < 0) return "";
		if(end > src.length()) end = src.length();

		return src.substring(start, end);
	}
	
	/**
	 *	파라미터 스트링이 null 이 아니고, "" 이 아니면 true, 아니면 false
	 *
	 *	@param param		검사 문자열
	 *
	 *	@return 검사결과
	 */
	public static boolean isNotNull(String param){
		if(param != null && "".equals(param) == false) return true;
		else return false;
	}

	/**
	 *	파라미터 스트링이 null 이거나, "" 이면 true, 아니면 false
	 *
	 *	@param param		검사 문자열
	 *
	 *	@return 검사결과
	 */
	public static boolean isNull(String param){
		if(param == null || "".equals(param) == true) return true;
		else return false;
	}
	
	//-----------------------------------------------------------------------------
	//	ASCII을 한글캐릭터셋으로 변환
	//-----------------------------------------------------------------------------
	public static String	ksc2asc(String str) {
		String	result				=	"";
		if (str != null && !str.equals("null") && !str.equals("")) {
			str						=	str.trim();

			try {
				result				=	new String(str.getBytes("KSC5601"),"8859_1");
			//	result				=	new String(str.getBytes("8859_1"),"KSC5601");
			}
			catch (UnsupportedEncodingException e) {
				
				result				=	str;
			}
		}
		else {
			result					=	"";
		}
		return	result;
	}
	
	public static String	asc2ksc(String str) {
		String	result				=	"";
		if (str != null && !str.equals("null") && !str.equals("")) {
			str						=	str.trim();

			try {
				result				=	new String(str.getBytes("8859_1"),"KSC5601");
			}
			catch (UnsupportedEncodingException e) {
				
				result				=	str;
			}
		}
		else {
			result					=	"";
		}
		return	result;
	}
	
	/**
	 *	스트링 치환 함수
	 *	
	 *	주어진 문자열(buffer)에서 특정문자열('src')를 찾아 특정문자열('dst')로 치환
	 *
	 */
	public static String ReplaceAll(String buffer, String src, String dst){
		if(buffer == null) return null;
		if(buffer.indexOf(src) < 0) return buffer;
		
		int bufLen = buffer.length();
		int srcLen = src.length();
		StringBuffer result = new StringBuffer();

		int i = 0; 
		int j = 0;
		for(; i < bufLen; ){
			j = buffer.indexOf(src, j);
			if(j >= 0) {
				result.append(buffer.substring(i, j));
				result.append(dst);
				
				j += srcLen;
				i = j;
			}else break;
		}
		result.append(buffer.substring(i));
		return result.toString();
	}
	
	//특수문자 제거 하기
	public static String StringReplace(String str){       
		String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
		str =str.replaceAll(match, "");
		return str;
	}
	
	//이메일 유효성
	public static boolean isEmailPattern(String email){
		Pattern pattern=Pattern.compile("\\w+[@]\\w+\\.\\w+");
		Matcher match=pattern.matcher(email);
		return match.find();
	}
	   
	/**
	 *	파라미터 스트링이 null or "" 이면 true, 아니면 false
	 *
	 *	@param param		검사 문자열
	 *
	 *	@return 검사결과
	 */
	public static boolean NVL(String param){
		if(param == null || "".equals(param) ) return true;
		else return false;
	}

	/**
	 * 문자열을 Form의 Input Text에 삽입할때 문자 변환
	 * @param str
	 * @return
	 */
    public static String	fn_input_text (String str) {
		if (str == null || str.equals("")) {
			return	"";
		}
		else {
			char	schr1			=	'\'';
			char	schr2			=	'\"';
			StringBuffer	sb		=	new StringBuffer(str);
			int		idx_str			=	0;
			int		edx_str			=	0;

			while (idx_str >= 0) {
				idx_str				=	str.indexOf(schr1, edx_str);
				if (idx_str < 0) {
					break;
				}
				str					=	sb.replace(idx_str, idx_str+1, "&#39;").toString();
				edx_str				=	idx_str + 5;
			}

			sb						=	new StringBuffer(str);
			idx_str					=	0;
			edx_str					=	0;
			while (idx_str >= 0) {
				idx_str				=	str.indexOf(schr2, edx_str);
				if (idx_str < 0) {
					break;
				}
				str					=	sb.replace(idx_str, idx_str+1, "&quot;").toString();
				edx_str				=	idx_str + 6;
			}

			return	str;
		}
	}

    /**
     * 데이터를 디비에 넣을 시 작업을 해준다.
     * @param str
     * @return
     */
	public static String inDataConverter(String str) {
        String result = "";
		if (str != null && !str.equals("null") && !str.equals("")) {
			str						=	str.trim();

			try {
//				result				=	new String(str.getBytes("8859_1"),"KSC5601");
				result = str;
			}
			//catch (UnsupportedEncodingException e) {
			catch (Exception e) {	
				result				=	str;
			}
		}
		else {
			result					=	"";
		}
		return	result;
	}

	/**
	 * 데이터를 디비에서 가져올 시 작업을 해준다.
	 * @param str
	 * @return
	 */
	public static String outDataConverter(String str) {
        String result = "";
		if (str != null && !str.equals("null") && !str.equals("")) {
			str						=	str.trim();

			try {
//				result				=	new String(str.getBytes("KSC5601"),"8859_1");
				result = str;
			}
			//catch (UnsupportedEncodingException e) {
			catch (Exception e) {
				
				result				=	str;
			}
		}
		else {
			result					=	"";
		}
		return	result;
	}

    /**
     * null인 경우 ""를 return
     * @param value
     * @return
     */
	public static String nvl(String value) {
		return nvl(value, "");
	}
	
	/**
	 * value가 null인 경우 defalult값을 return
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static int nvl(String value, int defaultValue) {
		if (value == null || value.equals(""))
			return defaultValue;
		else
			return Integer.parseInt(value);
	}
	
	/**
	 * Number 타입인지를 체크 한다.
	 * @param paramName
	 * @return
	 */
	public static boolean isNumber(String paramName) {
		paramName = nvl(paramName);
		try {
			Long.parseLong(paramName);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

    public static String getHtmlContents(String src)
    {
        src = ReplaceAll(src, "<", "&lt;");
   		src = ReplaceAll(src, ">", "&gt;");  
        src = ReplaceAll(src, "\n", "<br>");
        src = ReplaceAll(src, "&quot;", "\"");
        src = ReplaceAll(src, " ", "&nbsp;");
        
        return src;
    }
    
    public static String getHtmlText(String src)
    {
        src = ReplaceAll(src, "<", "&lt;");
   		src = ReplaceAll(src, ">", "&gt;");    	
        src = ReplaceAll(src, "\n", "<br>");
        src = ReplaceAll(src, "&quot;", "\"");
        src = ReplaceAll(src, " ", "&nbsp;");

        return src;
    }
    
    /**
     * HTML 태그 제거
     */
    public static String getTextByHtml(String src) {
    	String reg = "<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>";
    	String ret = src.replaceAll(reg, "");
    	return ret;
    }
        
    /**
     * 데이타를 구분자로 나누어 배열로 리턴 
     * @param str
     * @param sepe_str
     * @return
     */
	public static String[] split(String str, String sepe_str) {
		int		index				=	0;
		String[] result				=	new String[search(str,sepe_str)+1];
		String	strCheck			=	new String(str);
		while (strCheck.length() != 0) {
			int		begin			=	strCheck.indexOf(sepe_str);
			if (begin == -1) {
				result[index]		=	strCheck;
				break;
			} 
			else {
				int	end				=	begin + sepe_str.length();
				if(true) {
					result[index++]	=	strCheck.substring(0, begin);
				}
				strCheck			=	strCheck.substring(end);
				if(strCheck.length()==0 && true) {
					result[index]	=	strCheck;
					break;
				}
			}
		}
		return result;
	}
	
	public static int search(String strTarget, String strSearch) {
		int		result				=	0;
		String	strCheck			=	new String(strTarget);
		for(int i = 0; i < strTarget.length();) {
			int		loc				=	strCheck.indexOf(strSearch);
			if(loc == -1) {
				break;
			} 
			else {
				result++;
				i					=	loc + strSearch.length();
				strCheck			=	strCheck.substring(i);
			}
		}
		return result;	
	}
	
	/**
	 * 인자값으로 받은 Integre 만큼  문자를 자른후 나머지 문자는 .. 으로 표시한다
	 * @param str
	 * @param maxLength
	 * @return
	 */
    public static String setMaxLength(String str, int maxLength) {
        if (str == null) {
            return    "";
        }
        if ( str.length() <= maxLength ) return str;
        if ( maxLength < 3 ) return str.substring(0, 2);

        StringBuffer returnString = new StringBuffer();
        str = str.trim();

        returnString.append(str.substring(0, maxLength-1)).append("..");

        return    returnString.toString();
    }
    /**
     * lpad 함수
    *
     * @param str   대상문자열, len 길이, addStr 대체문자
    * @return      문자열
    */

    public static String lpad(String str, int len, String addStr) {
        String result = str;
        int templen   = len - result.length();

       for (int i = 0; i < templen; i++){
              result = addStr + result;
        }
        
        return result;
    }

    
	public static String cropByte(String str, int cut) {
		
        if (str == null) {
            return    "";
        }
        if ( str.length() <= cut ) return str;
        if ( cut < 3 ) return str.substring(0, 2);

		StringCharacterIterator iter = new StringCharacterIterator(str);
        int check = 0;
		int type = Character.getType(iter.last());
		if(type == Character.OTHER_SYMBOL) 
		  cut --;
		else check++;
			
		if(check < 1){
		    //재검사
			iter.setText(str.substring(0,cut));
			type = Character.getType(iter.last()); 
			if(type == Character.OTHER_SYMBOL)
			  cut += 2;
		}
		 
		//문자를 다시 잘라 리턴
	    return str.substring(0,cut)+"...";
	    
		/*
		String tail = "...";
		if (str == null)
            return null;
    
        int srcLen = realLength(str);
        if (srcLen < cut)
            return str;

 

        String tmpTail = tail;
        if (tail == null)
            tmpTail = "";
 
        int tailLen = realLength(tmpTail);
        if (tailLen > cut)
            return "";

 

        char a;
        int i = 0;
        int realLen = 0;
        for (i = 0; i < cut - tailLen && realLen < cut - tailLen; i++) {
           a = str.charAt(i);
           if ((a & 0xFF00) == 0)
               realLen += 1;
           else
               realLen += 2;
        }

 

        while (realLength(str.substring(0, i)) > cut - tailLen) {
            i--;
        }

 

        return str.substring(0, i) + tmpTail;
        */
		/*
		String trail = "...";
		if (str==null) return "";
    	String tmp = str;
    	int slen = 0, blen = 0;
    	char c;
    	try {
        	if(tmp.getBytes("MS949").length>cut) {
        		while (blen+1 < cut) {
        			c = tmp.charAt(slen);
        			blen++;
        			slen++;
        			if ( c  > 127 ) blen++;  //2-byte character..
        		}
        		tmp=tmp.substring(0,slen)+trail;
        	}
        } catch(java.io.UnsupportedEncodingException e) {}
    	return tmp;
    	*/
		
	}
	 public static int realLength(String s) {
        return s.getBytes().length;
    }
	/**
	 * 파일 확장자를 리턴한다.
	 * @param szTemp
	 * @return
	 */
	public static String getExt(String szTemp)
	{
		if(szTemp == null) return "";

		String fname = "";
		if (szTemp.indexOf(".") != -1) {
			fname = szTemp.substring(szTemp.lastIndexOf("."));
			return fname;
		} else {
			return "";
		}
	}
	/**
	 * 천단위 콤마 찍힌 숫자를 리턴한다.
	 * @param str
	 * @return
	 */
	public static String getMoneyType(int str) 
	{ 
		NumberFormat nf = NumberFormat.getNumberInstance(); 
		String r_str = nf.format(str); 
		return r_str; 
	}
	
	/**
	 * URL Encoding
	 * @param url
	 * @return
	 */
	public static String getUrlEncode(String url) {
		if (url != null) {
			try {
				url = url.replaceAll(" ", "*20");
				url = URLEncoder.encode(url, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} 
			url = url.replace('%','*');
		}
		return url;
	}
	
	
	/**
	 * URL Decoding
	 * @param url
	 * @return
	 */
	public static String getUrlDecode(String url) {
		if (url != null) {
			url = url.replace('*','%');
			try {
				url = URLDecoder.decode(url, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return url;
	}
	
	/**
	 * 캐릭터셋 엔코딩
	 * @param str
	 * @param encoding
	 * @return
	 */
	public static String encodingCharset(String str, String encoding) {
		String result = "";
		
		int idx = encoding.indexOf("|");
		String param1 = encoding.substring(0, idx);
		String param2 = encoding.substring(idx+1);
		
		try {
			result	= new String(str.getBytes(param1), param2);
		}
		catch (UnsupportedEncodingException e) {
			result	= str;
		}
		
		return result;
	}
	
	public static String changePassword() {
		String password = "";
		
		for(int i = 0; i < 8; i++){
			char lowerStr = (char)(Math.random() * 26 + 97);
			
			if(i%2 == 0){
				password += (int)(Math.random() * 10);
			} else {
				password += lowerStr;
			}
		}
		
		return password;
	}
	
    /**
     * <p>XXX - XXX- XXXX 형식의 전화번호 앞, 중간, 뒤 문자열 3개 입력 받아 유요한 전화번호형식인지 검사.</p>
     * 
     * 
     * @param   전화번호 문자열( 3개 )
     * @return  유효한 전화번호 형식인지 여부 (True/False)
     */
    public static boolean checkFormatTell(String tell1, String tell2, String tell3) {
		 
	 String[] check = {"02", "031", "032", "033", "041", "042", "043", "051", "052", "053", "054", "055", "061", 
				 "062", "063", "070", "080", "0505"};	//존재하는 국번 데이터
	 String temp = tell1 + tell2 + tell3;
		 
	 for(int i=0; i < temp.length(); i++){
    		if (temp.charAt(i) < '0' || temp.charAt(i) > '9')	
    			return false;    		
	 }	//숫자가 아닌 값이 들어왔는지를 확인
 		 
	 for(int i = 0; i < check.length; i++){
		 if(tell1.equals(check[i])) break;			 
		 if(i == check.length - 1) return false;
	 }	//국번입력이 제대로 되었는지를 확인
		 
	 if(tell2.charAt(0) == '0') return false; 
		
	 if(tell1.equals("02")){
		 if(tell2.length() != 3 && tell2.length() !=4) return false;
		 if(tell3.length() != 4) return false;	//서울지역(02)국번 입력때의 전화 번호 형식유효성 체크			 
	 }else{
		 if(tell2.length() != 3) return false;
		 if(tell3.length() != 4) return false;
	 }	//서울을 제외한 지역(국번 입력때의 전화 번호 형식유효성 체크	 
	 
	 return true;
    }
	
    /**
     * <p>XXX - XXX- XXXX 형식의 전화번호 하나를 입력 받아 유요한 전화번호형식인지 검사.</p>
     * 
     * 
     * @param   전화번호 문자열 (1개)
     * @return  유효한 전화번호 형식인지 여부 (True/False)
     */
    public static boolean checkFormatTell(String tellNumber) {
	 
	 String temp1;
	 String temp2;
	 String temp3;
	 String tell = tellNumber;
	 
	 tell = tell.replace("-", "");	
	 
	 if(tell.length() < 9 || tell.length() > 11  || tell.charAt(0) != '0') return false;	//전화번호 길이에 대한 체크
		 
	 if(tell.charAt(1) =='2'){	//서울지역 (02)국번의 경우일때
		 temp1 = tell.substring(0,2);
		 if(tell.length() == 9){
			 temp2 = tell.substring(2,5);
			 temp3 = tell.substring(5,9);
		 }else if(tell.length() == 10){
			 temp2 = tell.substring(2,6);
			 temp3 = tell.substring(6,10);
		 }else
			 return false;	
	 } else if(tell.substring(0,4).equals("0505")){ //평생번호(0505)국번의 경우일때
		 if(tell.length() != 11) return false;
		 temp1 = tell.substring(0,4);
		 temp2 = tell.substring(4,7);
		 temp3 = tell.substring(7,11);
	 } else {	// 서울지역 및 "0505" 를 제외한 일반적인 경우일때
		 if(tell.length() != 10) return false;
		 temp1 = tell.substring(0,3);
		 temp2 = tell.substring(3,6);
		 temp3 = tell.substring(6,10); 
	 }
		 		 
	 return checkFormatTell(temp1, temp2, temp3);
    }
	
    /**
     * <p>XXX - XXX- XXXX 형식의 휴대폰번호 앞, 중간, 뒤 문자열 3개 입력 받아 유요한 휴대폰번호형식인지 검사.</p>
     * 
     * 
     * @param   휴대폰번호 문자열,(3개)
     * @return  유효한 휴대폰번호 형식인지 여부 (True/False)
     */
    public static boolean checkFormatCell(String cell1, String cell2, String cell3) {
	 String[] check = {"010", "011", "016", "017", "018", "019"}; //유효한 휴대폰 첫자리 번호 데이터
	 String temp = cell1 + cell2 + cell3;
	 
	 for(int i=0; i < temp.length(); i++){
    		if (temp.charAt(i) < '0' || temp.charAt(i) > '9') 
    			return false;    		
         }	//숫자가 아닌 값이 들어왔는지를 확인
	 		 
	 for(int i = 0; i < check.length; i++){
	     if(cell1.equals(check[i])) break;			 
	     if(i == check.length - 1) return false;
	 }	// 휴대폰 첫자리 번호입력의 유효성 체크
		 
	 if(cell2.charAt(0) == '0') return false;
		
	 if(cell2.length() != 3 && cell2.length() !=4) return false;
	 if(cell3.length() != 4) return false;
				 
	 return true;
    }
	 
    /**
     * <p>XXXXXXXXXX 형식의 휴대폰번호 문자열 3개 입력 받아 유요한 휴대폰번호형식인지 검사.</p>
     * 
     * 
     * @param   휴대폰번호 문자열(1개)
     * @return  유효한 휴대폰번호 형식인지 여부 (True/False)
     */
    public static boolean checkFormatCell(String cellNumber) {
		 
	 String temp1;
	 String temp2;
	 String temp3;
	
	 String cell = cellNumber;
	 cell = cell.replace("-", "");		
	 
	 if(cell.length() < 10 || cell.length() > 11  || cell.charAt(0) != '0') return false;
	 
	 if(cell.length() == 10){	//전체 10자리 휴대폰 번호일 경우
		 temp1 = cell.substring(0,3);
		 temp2 = cell.substring(3,6);
		 temp3 = cell.substring(6,10);
	 }else{		//전체 11자리 휴대폰 번호일 경우
		 temp1 = cell.substring(0,3);
		 temp2 = cell.substring(3,7);
		 temp3 = cell.substring(7,11);
	 }
		 
	 return checkFormatCell(temp1, temp2, temp3);
    }
	 
    /**
     * <p> 이메일의  앞, 뒤 문자열 2개 입력 받아 유요한 이메일형식인지 검사.</p>
     * 
     * 
     * @param   이메일 문자열 (2개)
     * @return  유효한 이메일 형식인지 여부 (True/False)
     */
    public static boolean checkFormatMail(String mail1, String mail2) {
		 
	 int count = 0;
		 
	 for(int i = 0; i < mail1.length(); i++){
		 if(mail1.charAt(i) <= 'z' && mail1.charAt(i) >= 'a') continue;		
		 else if(mail1.charAt(i) <= 'Z' && mail1.charAt(i) >= 'A') continue;	
		 else if(mail1.charAt(i) <= '9' && mail1.charAt(i) >= '0') continue;	
		 else if(mail1.charAt(i) == '-' && mail1.charAt(i) == '_') continue;	
		 else return false;
	 }	// 유효한 문자, 숫자인지 체크
		 		 		 
	 for(int i = 0; i < mail2.length(); i++){	
		 if(mail2.charAt(i) <= 'z' && mail2.charAt(i) >= 'a') continue;
		 else if(mail2.charAt(i) == '.'){ count++;  continue;}
		 else return false;
	 }	// 메일 주소의 형식 체크(XXX.XXX 형태)		
		 
	 if(count == 1) return true;
	 else  return false;	 
		 
    }
	
    /**
     * <p> 이메일의 전체문자열 1개 입력 받아 유요한 이메일형식인지 검사.</p>
     * 
     * 
     * @param   이메일 문자열 (1개)
     * @return  유효한 이메일 형식인지 여부 (True/False)
     */
    public static boolean checkFormatMail(String mail) {
		 
	 String[] temp = mail.split("@");	// '@' 를 기점으로 앞, 뒤 문자열 구분
	 
	 if(temp.length == 2) return checkFormatMail(temp[0], temp[1]);
	 else return false;
    }
    
    /**
     * <p> 문자열 소문자로 치환</p>
     * 
     * 
     * @param   문자열 (1개)
     * @return  소문자
     */
    public static String lower(String text) {
    	return text.toLowerCase();
    }
    
    /**
     * <p> 문자열 대문자로 치환</p>
     * 
     * 
     * @param   문자열 (1개)
     * @return  대문자
     */
    public static String upper(String text) {
    	return text.toUpperCase();
    }

    /**
     * uuid
     * @param value
     * @return
     */
	public static String getUUID() {
		String key = UUID.randomUUID().toString();
		return ReplaceAll(key, "-", "");
	}

    /**
     * getRandom
     * @param size
     * @return
     */
	public static String getRandom(int size) {
		Random rnd =new Random();
		
		StringBuffer buf =new StringBuffer();
		
		for(int i=0;i<size;i++){
		    if(rnd.nextBoolean()){
		        buf.append((char)((int)(rnd.nextInt(26))+97));
		    }else{
		        buf.append((rnd.nextInt(10))); 
		    }
		}
		
		return buf.toString();
	}

    /**
     * getRandomNumber
     * @param 
     * @return int
     */
	public static int getRandomNumber(int length) {
		 
	    String numStr = "1";
	    String plusNumStr = "1";
	 
	    for (int i = 0; i < length; i++) {
	        numStr += "0";
	 
	        if (i != length - 1) {
	            plusNumStr += "0";
	        }
	    }
	 
	    Random random = new Random();
	    int result = random.nextInt(Integer.parseInt(numStr)) + Integer.parseInt(plusNumStr);
	 
	    if (result > Integer.parseInt(numStr)) {
	        result = result - Integer.parseInt(plusNumStr);
	    }
	 
	    return result;
	}
	
	/**
	  * 숫자에 천단위마다 콤마 넣기
	  * @param int
	  * @return String
	  * */
	 public static String toNumFormat(int num) {
		 DecimalFormat df = new DecimalFormat("#,###");
		 return df.format(num);
	 }

	/**
	  * 숫자에 천단위마다 콤마 넣기
	  * @param int
	  * @return String
	  * */
	 public static String toNumFormat(long num) {
		 DecimalFormat df = new DecimalFormat("#,###");
		 return df.format(num);
	 }
	 
	 
	/**
	 * 문자열 trim 처리 
     * 
	 * @param str
	 * @return
	 */
	public static String trim(String str){
		 if(StringUtils.equals(nvl(str,""),"")){
			 return "";
		 }else{
			 return str.trim();
		 }
	 }
	 
}
