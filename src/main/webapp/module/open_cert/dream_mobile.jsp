<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.dreamsecurity.crypt.MsgCrypto" %>
<%@ page import="java.net.URLDecoder" %>
<%

    request.setCharacterEncoding("UTF-8");

    String encPriInfo = request.getParameter("priinfo");
    String[] param = request.getParameterValues("reqNum");

    String reqNum = "";
    String userId = "";
    String successUrl = "";
    String failUrl = "";
    String phoneNum = "";
    for (int i = 0; i < param.length; i++) {

        String paramEncoding = param[i];
        String urlParam = new String(paramEncoding.getBytes("iso-8859-1"), "utf-8");

        System.out.println(urlParam);
        String[] data = urlParam.split("_");
        if (data.length > 1) {

            reqNum = data[0];
            userId = data[1];
            successUrl = data[2];
            failUrl = data[3];
            phoneNum = data[4];
        }
    }

    MsgCrypto mscr = new MsgCrypto();
    //String rstInfo = mscr.msgDecrypt(encPriInfo,"D:/userWeb/box.newzenpnp.co.kr/WebContent/cert/newzenpnpPri.key","psh1112","EUC-KR");
    //String rstInfo = mscr.msgDecrypt(encPriInfo,"D:/Newzen/ROOT/module/cert/newzenpnpPri.key","psh1112","EUC-KR");	// 개발PC
    String rstInfo = mscr.msgDecrypt(encPriInfo, "D:/userWeb/ieumsign.co.kr/WebContent/module/cert/newzenpnpPri.key", "psh1112", "EUC-KR");
    String[] rstInfoArray = rstInfo.split("\\$");

    String resultCode = rstInfoArray[0];
    String error = rstInfoArray[1];
    String reqNumResult = rstInfoArray[9];

    System.out.println(rstInfo);
    //일치할 경우에만 정상적인 처리 가능
    String rtnStr = "";

    System.out.println(reqNum);
    System.out.println(reqNumResult);
    System.out.println("successUrl : " + successUrl);
    if (reqNum.equals(reqNumResult)) {
        // System.out.println(empName+"|-|"+rstInfoArray[8]);
        //결과코드 성공 판단
        if ((resultCode.equals("00") || resultCode.equals("02") || resultCode.equals("52") || resultCode.equals("56")) && phoneNum.equals(rstInfoArray[3])) {
            //successUrl = successUrl + "?userId="+userId+"&success=true";
            successUrl = URLDecoder.decode(successUrl, "UTF-8");
            rtnStr += "<script type='text/javascript'>";
            rtnStr += "successGo('" + successUrl + "');";
            rtnStr += "</script>";
        } else { //아닐경우
            //failUrl = failUrl + "?userId="+userId+"&success=false";
            failUrl = URLDecoder.decode(failUrl, "UTF-8");
            rtnStr += "<script type='text/javascript'>";
            rtnStr += "failGo('" + failUrl + "');";
            rtnStr += "</script>";
        }

    } else { //아닐경우
        //failUrl = failUrl + "?userId="+userId+"&success=false";
        failUrl = URLDecoder.decode(failUrl, "UTF-8");
        rtnStr += "<script type='text/javascript'>";
        rtnStr += "failGo('" + failUrl + "');";
        rtnStr += "</script>";
    }
%>
<script type="text/javascript" src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript">
    //<![CDATA[

    function fn_insUserCert() {

        var url = rootPath + "user/insUserCert.do";

        $.ajax({
            url: url,
            crossDomain: true,
            dataType: "json",
            type: "GET",
            data: {
                resultCd: '<%=rstInfoArray[0]%>',
                ci: '<%=rstInfoArray[1]%>',
                di: '<%=rstInfoArray[2]%>',
                phonNo: '<%=rstInfoArray[3]%>',
                telCo: '<%=rstInfoArray[4]%>',
                birth: '<%=rstInfoArray[5]%>',
                gender: '<%=rstInfoArray[6]%>',
                nation: '<%=rstInfoArray[7]%>',
                name: '<%=rstInfoArray[8]%>',
                reqNum: '<%=rstInfoArray[9]%>',
                reqDate: '<%=rstInfoArray[10]%>',
                certType: '001'
            },
            success: function (result) {
                console.log(result);
            },
            error: function (request, status, error) {
                console.log("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error)
                //alert("입력하신 정보를 다시 확인해주시기 바랍니다.");
                //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            }
        });
    }

    function successGo(url) {
        fn_insUserCert();
        // opener.parent.location.href=url;
        // self.close(true);
        location.href = url;
    }

    function failGo(url) {
        fn_insUserCert();
        alert('본인인증이 실패하였습니다.');
        // opener.parent.location.href=url;
        // self.close(true);
        location.href = url;
    }

    // ]]>
</script>

<% out.print(rtnStr); %>