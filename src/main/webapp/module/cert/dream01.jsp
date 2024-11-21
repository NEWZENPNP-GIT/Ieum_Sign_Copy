<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.net.URLDecoder"%>
<%@ page import ="java.util.*,java.text.SimpleDateFormat"%>

<%

	request.setCharacterEncoding("UTF-8");
    //날짜 생성
    Calendar today = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String day = sdf.format(today.getTime());

    java.util.Random ran = new Random();
    //랜덤 문자 길이
    int numLength = 6;
    String randomStr = "";

    for (int i = 0; i < numLength; i++) {
        //0 ~ 9 랜덤 숫자 생성
        randomStr += ran.nextInt(10);
    }
	//reqNum은 최대 40byte 까지 사용 가능
    String reqNum = day + randomStr;
	
	String contractId = request.getParameter("contractId");
	String bizName = request.getParameter("bizName");
	String empName = request.getParameter("empName");
	String statusCode = request.getParameter("statusCode");
	String phoneNum = request.getParameter("phoneNum");
	
	
	contractId = URLDecoder.decode(contractId, "UTF-8");
	bizName = URLDecoder.decode(bizName, "UTF-8");
	empName = URLDecoder.decode(empName, "UTF-8");
	statusCode = URLDecoder.decode(statusCode, "UTF-8");
	
	String url = "https://ieumsign.newzenpnp.co.kr/module/cert/dream03.jsp?reqNum="+URLEncoder.encode(reqNum,"UTF-8")+"_"; // 개발
    // String url = "https://ieumsign.co.kr/module/cert/dream03.jsp?reqNum="+URLEncoder.encode(reqNum,"UTF-8")+"_"; // 운영
	url += URLEncoder.encode(contractId,"UTF-8")+"_"+""+"_"+""+"_"+URLEncoder.encode(statusCode,"UTF-8")+"_"+phoneNum;
	
%>
<html>
    <head>
        <title>본인인증서비스</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<script type="text/javascript" src="/js/jquery.min.js"></script>
		<script type="text/javascript" src="/js/common.js"></script>
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
	<script type="text/javascript">
	//<![CDATA[		
		window.onload = function(){
			$('#reqForm').submit();
		}		
	// ]]>
	</script>
    </head>
    <body bgcolor="#FFFFFF" topmargin=0 leftmargin=0 marginheight=0 marginwidth=0>
        <center>
            <br><br><br>
            <span class="style1">본인인증서비스</span><br>

            <form id="reqForm" name="reqForm" method="post" action="dream02.jsp">
            <input type="hidden" name="contractId" value="<%=contractId%>"/> <!-- id값 -->
			<input type="hidden" name="bizName"  value="<%=bizName%>"/> <!-- 회사명 -->
			<input type="hidden" name="empName"  value="<%=empName%>"/> <!-- 성명 -->
			<input type="hidden" name="statusCode"  value="<%=statusCode%>"/> <!-- 계약상태코드 -->
                <table cellpadding=1 cellspacing=1>
                    <tr>
                        <td align=center>회원사ID</td>
                        <td align=left><input type="text" name="cpid" size='41' maxlength ='10' value = "newzenpnp"></td>
                    </tr>
                    <tr>
                        <td align=center>URL코드</td>
                        <td align=left><input type="text" name="serviceCode" size='41' maxlength ='6' value="01005"></td>
                    </tr>
                    <tr>
                        <td align=center>요청번호</td>
                        <td align=left><input type="text" name="clntReqNum" size='41' maxlength ='40' value='<%=reqNum%>'></td>
                    </tr>
                    <tr>
                        <td align=center>요청일시</td>
						<!-- 현재시각 세팅(YYYYMMDDHI24MISS) -->
                        <td align=left><input type="text" name="reqdate" size="41" maxlength ='14' value="<%=day%>"></td>
					</tr>
                    <tr>
                        <td align=center>결과수신URL</td>
                        <td align=left><input type="text" name="retUrl"  value="<%=url%>"></td>
                    </tr>
                </table>
                <br><br>
                <input type="submit" value="본인인증 테스트" >
            </form>
            <br>
            <br>
            <br>
        </center>
    </body>
</html>