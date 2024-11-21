<!-- 필요한 class import -->
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="tradesign.crypto.provider.*"%>
<%@ page import="tradesign.pki.pkix.X509Certificate"%>
<%@ page import="tradesign.pki.pkix.X509CRL"%>
<%@ page import="tradesign.pki.pkix.Login"%>
<%@ page import="tradesign.pki.util.*" %>
<%@ page import="java.io.*, java.security.*, java.util.*, java.lang.*" %>

<html>
<link rel="stylesheet" href="../../tradesign2.css" type="text/css">
<body>
<%
	try{
	
		JeTS.installProvider("./tradesign3280.properties");

		String message = new String(request.getParameter("logindata").getBytes("ISO-8859-1"), "utf-8");
	
		// 클라이언트에서 보내온 로그인메시지를 base64디코딩하여 login_request에 저장한다.
		byte[] login_request = tradesign.pki.util.JetsUtil.base64ToBytes(message);
		
		// Login객체 생성
		Login login1 = new Login(login_request);

		login1.setupCipher(JeTS.getServerkmCert(0), JeTS.getServerkmPriKey(0), JeTS.getServerKmKeyPassword(0));
		login1.parseLoginData(true);
		
		// 클라이언트에서 보내온 로그인메시지에서 사용자데이터영역을 userdata에 저장한다.	
		String userdata = new String(login1.getUserData());	
		String ssnumber = new String(login1.getSsn());

		// 클라이언트에서 보내온 로그인메시지에서 로그인을 시도한 사용자의 인증서를 읽는다.
		X509Certificate[] certs = login1.getSignerCerts();

		String SubjectDNStr[] = null; 
		String NotBeforeStr[] = null;
		String NotAfterStr[] = null; 
		String SerialNumber[] = null;
		String IssuerDNStr[] = null; 
		String SignatureAlgorithm[] = null;
		
		
		if (certs != null)
		{		
			SubjectDNStr  = new String[certs.length];
			NotBeforeStr  = new String[certs.length];
			NotAfterStr  = new String[certs.length];
			SerialNumber  = new String[certs.length];
			IssuerDNStr  = new String[certs.length];
			SignatureAlgorithm  = new String[certs.length];						
			
			for (int i = 0; i < certs.length; i++)
			{
				SubjectDNStr[i] = certs[i].getSubjectDNStr();
				NotBeforeStr[i] = certs[i].getNotBefore().toString();
				NotAfterStr[i] = certs[i].getNotAfter().toString();
				SerialNumber[i] = certs[i].getSerialNumber().toString();
				IssuerDNStr[i] = certs[i].getIssuerDNStr();
				SignatureAlgorithm[i] = certs[i].getSignatureAlgorithm().toString();
				
				
			}
		}
		
		X509Certificate cert = certs[0];
		boolean ret = cert.VerifyIDN(ssnumber, login1.getRandom());
		
		String idconfirm="";
		if (ret)
			idconfirm="신원확인 성공";
		else
			idconfirm="신원확인 실패";
		
		String strUserData ="", strRandom = "", strSessionId = "";
			
		strUserData = new String(login1.getUserData());
		
		strRandom = JetsUtil.toBase64String(login1.getRandom());
		strSessionId = new String(login1.gettid());		
		
%>
<table border="2" width="670" cellpadding="3" cellspacing="0" bordercolor="#97A9BB" bgcolor="#A7B9CC">
  <tr><td align="center"><font color="#FFFFFF">  Login 예제   </font></td></tr>
</table>
<table border="2" width="670" cellpadding="3" cellspacing="0" bordercolor="#97A9BB" bgcolor="#A7B9CC">
  <tr><td align="center"><font color="#FFFFFF">== 사용자에게 받은 로그인 요청 메시지(BASE64인코딩) ==</font></td></tr>
</table>
<table border="2" width="670" cellpadding="5" cellspacing="0" bordercolor="#E0E0E0" bgcolor="#F0F0F0">
  <tr>
    <td align="left">
      <textarea name="plainText" rows="6" cols="90" ><%= message %></textarea>
    </td>
</tr>
</table>
<br>
<table border="2" width="670" cellpadding="3" cellspacing="0" bordercolor="#97A9BB" bgcolor="#A7B9CC">
  <tr><td align="center"><font color="#FFFFFF">== 사용자에게 받은 로그인 요청 메시지의 인증서 정보 ==</font></td></tr>
</table>
<table border="2" width="670" cellpadding="5" cellspacing="0" bordercolor="#E0E0E0" bgcolor="#F0F0F0">
<%
	if (certs != null)
	{		
		for (int i = 0; i < certs.length; i++)
		{
			out.print("<tr><td align='left'><textarea name='signedData' rows='10' cols='90'>"+ 
			"[Subject DN]"+SubjectDNStr[i] + "\r\n" +
			"[유효기간시작일]"+NotBeforeStr[i] + "\r\n" +
			"[유효기간종료일]"+NotAfterStr[i] + "\r\n" +
			"[시리얼넘버]"+SerialNumber[i] + "\r\n" +
			"[발급자DN]"+IssuerDNStr[i] + "\r\n" +
			"[서명알고리즘]"+SignatureAlgorithm[i] + "\r\n" +
			"</textarea></td></tr>");
		}
	}	
%>
</table>
<table border="2" width="670" cellpadding="3" cellspacing="0" bordercolor="#97A9BB" bgcolor="#A7B9CC">
  <tr><td align="center"><font color="#FFFFFF">신원확인 결과</font></td></tr>
</table>
<table border="2" width="670" cellpadding="5" cellspacing="0" bordercolor="#E0E0E0" bgcolor="#F0F0F0">

<tr>
	<td>신원확인정보(사업자번호/주민등록번호): <%=  new String(login1.getSsn()) %></td>
          <td>사용안할 경우 0</td>
</tr>

<tr>
	<td>신원확인식별자 검증 :<%=  idconfirm %> </td>
</tr>
</table>

<table border="2" width="670" cellpadding="3" cellspacing="0" bordercolor="#97A9BB" bgcolor="#A7B9CC">
  <tr><td align="center"><font color="#FFFFFF">로그인 요청 메시지내 사용자 정보</font></td></tr>
</table>
<table border="2" width="670" cellpadding="5" cellspacing="0" bordercolor="#E0E0E0" bgcolor="#F0F0F0">

<tr>
	<td>UserInfo : <%=  strUserData %></td>
       <td>사용안할 경우 0</td>
</tr>

<tr>
	<td>Random (Base64 인코딩 값):<%=  strRandom  %> </td>
</tr>

<tr>
	<td>SessionID :<%=  strSessionId %> </td>
          <td>사용안할 경우 0</td>
</tr>
  <tr>
    <td align="right">
      <INPUT TYPE="button" Value="이 전 으 로" onClick="location.href='DemoLogin.jsp'">
    </td>
  </tr>
</table>
<%
} catch(Exception e) {
	e.printStackTrace();
	out.println("에러 발생:" + e.getMessage() + "<br>");
}
%>
  </body>
  </html>
