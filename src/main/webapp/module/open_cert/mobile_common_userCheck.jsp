<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.dreamsecurity.crypt.MsgCrypto" %>
<%@ page import="com.ezsign.framework.util.SessionUtil" %>
<%@ page import="com.ezsign.framework.vo.SessionVO" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.net.URLEncoder,java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Random" %>

<%

    request.setCharacterEncoding("UTF-8");
    MsgCrypto mscr = new MsgCrypto();

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

    String reqInfo = "";
    String encReqInfo = "";
    String rtn_url = "";
    String cpId =    "newzenpnp"; //request.getParameter("cpid");        // 회원사ID
    String urlCode = "01005"; //request.getParameter("serviceCode");     // URL 코드
    String reqdate = day; //request.getParameter("reqdate");        // 요청일시
    //reqNum은 최대 40byte 까지 사용 가능
    String reqNum = day + randomStr; //request.getParameter("clntReqNum");

    String userId = "";
    SessionVO loginVO = SessionUtil.getSession(request);
    if (loginVO != null) {
        System.out.println("userId=>" + loginVO.getUserId());
        userId = loginVO.getUserId();
    }
    String successUrl = request.getParameter("successUrl");
    String failUrl = request.getParameter("failUrl");
    String phoneNum = request.getParameter("phoneNum");

    successUrl = URLDecoder.decode(successUrl, "UTF-8");
    failUrl = URLDecoder.decode(failUrl, "UTF-8");
    phoneNum = URLDecoder.decode(phoneNum, "UTF-8");

    reqInfo = urlCode + "/" + reqNum + "/" + reqdate;  //암호화 시킬 데이터 '/'로 구분해서 합친다.
    //encReqInfo = mscr.msgEncrypt(reqInfo,"D:/userWeb/box.newzenpnp.co.kr/WebContent/cert/newzenpnpCert.der");
    //encReqInfo = mscr.msgEncrypt(reqInfo,"D:/Newzen/ROOT/module/cert/newzenpnpCert.der");  // 개발PC
    encReqInfo = mscr.msgEncrypt(reqInfo, "D:/userWeb/ieumsign.co.kr/WebContent/module/cert/newzenpnpCert.der");
    encReqInfo = URLEncoder.encode(encReqInfo);
    //msgEncrypt(암호화 시킬 값, 인증서 경로);
    //rtn_url = "http://localhost:8080/module/cert/dream_mobile.jsp?reqNum="+URLEncoder.encode(reqNum,"UTF-8");
    //rtn_url += "_"+URLEncoder.encode(userId,"UTF-8")+"_"+URLEncoder.encode(successUrl,"UTF-8")+"_"+URLEncoder.encode(failUrl,"UTF-8")+"_"+URLEncoder.encode(phoneNum,"UTF-8"); // request.getParameter("retUrl");      // 본인인증 결과수신 받을 URL
    
    // rtn_url = "https://ieumsign.co.kr/module/cert/dream_mobile.jsp?reqNum=" + reqNum; // 실서버용
    rtn_url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();  // 현재 서버 https여부, 도매인, 포트 가져옴
    System.out.println("rtn_url: " + rtn_url);
    rtn_url += "/module/cert/dream_mobile.jsp?reqNum=" + reqNum; // 개발용
    
    rtn_url += "_" + userId + "_" + URLEncoder.encode(successUrl, "UTF-8") + "_" + URLEncoder.encode(failUrl, "UTF-8") + "_" + phoneNum; // request.getParameter("retUrl");      // 본인인증 결과수신 받을 URL
    rtn_url = URLEncoder.encode(rtn_url, "UTF-8");
    String param = reqNum + "_" + userId + "_" + successUrl + "_" + failUrl + "_" + phoneNum;

    param = URLEncoder.encode(param, "UTF-8");


%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, user-scalable=no">

    <link rel="stylesheet" type="text/css" href="/css/font.css">
    <link rel="stylesheet" type="text/css" href="/css/material-icons.min.css">
    <link rel="stylesheet" type="text/css" href="/css/mobile_common.css">

    <script language=javascript>

        //var url = "https://dev.mobile-ok.com/popup/common/mobile_ncp.jsp";  //개발
        var url = "http://dev.mobile-ok.com/popup/common/hscert.jsp";  //개발PC
        //var url = "https://www.mobile-ok.com/popup/common/hscert.jsp";   //운영

        var DRMOK_window;

        function openDRMOKWindow() {
            /*
            window.name = 'sendJsp';
            DRMOK_window = window.open(url + '?cpid=<%=cpId%>&rtn_url=<%=rtn_url%>&req_info=<%=encReqInfo%>&reqNum=<%=param%>', 'DRMOKWindow', 'width=425,height=550,scrollbars=no,toolbar=no,location=no,directories=no,status=no');
            DRMOK_window.focus();

            if (DRMOK_window == null) {
                alert(" ※ 윈도우 XP SP2 또는 인터넷 익스플로러 7 사용자일 경우에는 \n    화면 상단에 있는 팝업 차단 알림줄을 클릭하여 팝업을 허용해 주시기 바랍니다. \n\n※ MSN,야후,구글 팝업 차단 툴바가 설치된 경우 팝업허용을 해주시기 바랍니다.");
            }
            */

            location.href = url + '?cpid=<%=cpId%>&rtn_url=<%=rtn_url%>&req_info=<%=encReqInfo%>&reqNum=<%=param%>';
        }


    </script>

</head>
<body>

<header class="header userCheck">
    <ul class="gnb">
        <li class="prev"><a class="btn_type btn_prev" href="#" onclick="history.back(); return false;" target="_self"></a></li>
        <li class="head_text NanumBarunGothic">본인확인</li>
        <li class="menu"></li>
    </ul>
</header>
<div id="container" class="userCheck_wrap">

    <div class="con userCheck">
        <ul>
            <li class="icon"></li>
            <li class="text desc gray NanumBarunGothic">
                회원님의 정보보호를 위하여 본인인증을 진행합니다<br>
                본인 확인에 대한 인증절차는 최초 1회만 진행됩니다<br>
                아래 버튼을 눌러 본인인증 바랍니다.
            </li>
            <li class="btn_group">
                <div class="btn_type NanumSquare_R" onclick="openDRMOKWindow()">본인인증</div>
            </li>
        </ul>
    </div>
</div>

</body>
</html>