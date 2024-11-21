<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "java.util.*" %>
<%@ page import ="com.dreamsecurity.crypt.*"%>
<%@ page import ="java.net.URLEncoder"%>
<%@ page import="java.net.URLDecoder"%>
<%

request.setCharacterEncoding("UTF-8");
  MsgCrypto mscr = new MsgCrypto();

  String reqInfo    = "";
  String encReqInfo = "";
  String rtn_url = "";
  String cpId       = request.getParameter("cpid");        // 회원사ID
  String urlCode    = request.getParameter("serviceCode");     // URL 코드
  String reqdate    = request.getParameter("reqdate");        // 요청일시
  String reqNum   = request.getParameter("clntReqNum");
  
  String contractId = request.getParameter("contractId");
  //기업명 _(언더바) 제거 2021-11-22 이두호
  String bizName = request.getParameter("bizName").replace("_", "");
  String empName = request.getParameter("empName");
  String statusCode = request.getParameter("statusCode");
  
  contractId = URLDecoder.decode(contractId, "UTF-8");
  bizName = URLDecoder.decode(bizName, "UTF-8");
  empName = URLDecoder.decode(empName, "UTF-8");
  statusCode = URLDecoder.decode(statusCode, "UTF-8");
  
  
  reqInfo = urlCode + "/" + reqNum + "/" + reqdate;  //암호화 시킬 데이터 '/'로 구분해서 합친다.
  //encReqInfo = mscr.msgEncrypt(reqInfo,"E:/cert/newzenpnpCert.der");
  encReqInfo = mscr.msgEncrypt(reqInfo,"E:/userWeb/ieumsign.newzenpnp.co.kr/WebContent/module/cert/newzenpnpCert.der"); // 개발
  //encReqInfo = mscr.msgEncrypt(reqInfo,"E:/userWeb/ieumsign.co.kr/WebContent/module/cert/newzenpnpCert.der"); // 운영
  encReqInfo = URLEncoder.encode(encReqInfo);
  //msgEncrypt(암호화 시킬 값, 인증서 경로);
  rtn_url = request.getParameter("retUrl");      // 본인인증 결과수신 받을 URL
  
  
  
	String param = reqNum+"_"+contractId+"_"+bizName+"_"+empName+"_"+statusCode;
  	
  	param = URLEncoder.encode(param, "UTF-8");
	
	
%>

<html>
<head>
<title>본인인증서비스</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<style>
<!--
   body,p,ol,ul,td
   {
   font-family: 굴림;
   font-size: 12px;
   }

   a:link { size:9px;color:#000000;text-decoration: none; line-height: 12px}
   a:visited { size:9px;color:#555555;text-decoration: none; line-height: 12px}
   a:hover { color:#ff9900;text-decoration: none; line-height: 12px}

   .style1 {
    color: #6b902a;
    font-weight: bold;
  }
  .style2 {
      color: #666666
  }
  .style3 {
    color: #3b5d00;
    font-weight: bold;
  }
-->
</style>


<script language=javascript>
  
  //var url = "https://dev.mobile-ok.com/popup/common/mobile_ncp.jsp";  //개발(구)
  //var url = "http://dev.mobile-ok.com/popup/common/hscert.jsp";  //개발PC
  var url = "https://s.mobile-ok.com/popup/common/hscert.jsp";  //개발
  //var url = "https://www.mobile-ok.com/popup/common/hscert.jsp";   //운영

    var DRMOK_window;
    function openDRMOKWindow(){
    window.name = 'sendJsp';
      DRMOK_window = window.open(url+'?cpid=<%=cpId%>&rtn_url=<%=rtn_url%>&req_info=<%=encReqInfo%>&reqNum=<%=param%>', 'DRMOKWindow', 'width=425,height=550,scrollbars=no,toolbar=no,location=no,directories=no,status=no' );
		DRMOK_window.focus();

        if(DRMOK_window == null){
      alert(" ※ 윈도우 XP SP2 또는 인터넷 익스플로러 7 사용자일 경우에는 \n    화면 상단에 있는 팝업 차단 알림줄을 클릭하여 팝업을 허용해 주시기 바랍니다. \n\n※ MSN,야후,구글 팝업 차단 툴바가 설치된 경우 팝업허용을 해주시기 바랍니다.");
        }
        
    }

    
    window.onload = function(){   	
    	
    	openDRMOKWindow();
    	
	}

    function submitForm() {
    	openDRMOKWindow();
    }

</script>


<body bgcolor="#FFFFFF" topmargin=0 leftmargin=0 marginheight=0 marginwidth=0>

<center>
<br><br><br><br><br><br>
<span class="style1">본인인증서비스 요청화면</span><br>
<br><br>
<table cellpadding=1 cellspacing=1>
    <tr>
        <td align=center>회원사ID</td>
        <td align=left><%=cpId%></td>
    </tr>
    <tr>
        <td align=center>URL코드</td>
        <td align=left><%=urlCode%></td>
    </tr>

    <tr>
        <td align=center>요청일시</td>
        <td align=left><%=reqdate%></td>
    </tr>

    <tr>
        <td align=center>&nbsp</td>
        <td align=left>&nbsp</td>
    </tr>

    <tr>
        <td align=center>결과수신URL</td>
        <td align=left><%=rtn_url%></td>
    </tr>
</table>

<!-- 본인인증서비스 요청 form --------------------------->
<form id="reqDRMOKForm" name="reqDRMOKForm" method="post" action="#">
    <input type="hidden" name="req_info"     value = "<%=encReqInfo%>">
    <input type="hidden" name="rtn_url"      value = "<%=rtn_url%>">
    <input type="hidden" name="cpid"      value = "<%=cpId%>">
    <input type="hidden" name="contractId" value="<%=contractId%>"/> <!-- id값 -->
	<input type="hidden" name="bizName"  value="<%=bizName%>"/> <!-- 회사명 -->
	<input type="hidden" name="empName"  value="<%=empName%>"/> <!-- 성명 -->
	<input type="hidden" name="statusCode"  value="<%=statusCode%>"/> <!-- 계약상태코드 -->
  	<input type="hidden" name="newpop"      value = "Y">
  	<input type="hidden" name="popup"      value = "N">
    <input type="submit" id="btn" value="본인인증서비스 요청"  onclick= "submitForm()">
</form>
<BR>
<BR>
<!--End 본인인증서비스 요청 form ----------------------->

</center>
</BODY>
</HTML>
