<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.URLDecoder"%>
<%@ page import="com.dreamsecurity.crypt.*"%>
<%

	request.setCharacterEncoding("UTF-8");

	String encPriInfo = request.getParameter("priinfo");
	String[] param = request.getParameterValues("reqNum");

	String reqNum = "";
	String contractId = "";
	String bizName = "";
	String empName = "";
	String statusCode = "";
	String phoneNum = "";

	for(int i=0; i<param.length; i++){

		String paramEncoding = param[i];
		System.out.println("한글 : "+paramEncoding);
		String urlParam = new String(paramEncoding.getBytes("iso-8859-1"), "utf-8");
		System.out.println("한글 : "+urlParam);


		System.out.println(urlParam);
		String[] data = urlParam.split("_");
		if(data.length>1) {

			reqNum = data[0];
			contractId = data[1];
			bizName = data[2];
			empName = data[3];
			statusCode = data[4];
			phoneNum = data[5];
		}
	}

	MsgCrypto mscr = new MsgCrypto();
	String rstInfo = mscr.msgDecrypt(encPriInfo,"E:/userWeb/ieumsign.newzenpnp.co.kr/WebContent/module/cert/newzenpnpPri.key","psh1112","EUC-KR"); // 개발
	//String rstInfo = mscr.msgDecrypt(encPriInfo,"E:/userWeb/ieumsign.co.kr/WebContent/module/cert/newzenpnpPri.key","psh1112","EUC-KR"); // 운영
	String[] rstInfoArray = rstInfo.split("\\$");


	String resultCode = rstInfoArray[0];
	String error = rstInfoArray[1];
	String reqNumResult = rstInfoArray[9];

	System.out.println(rstInfo);
	//일치할 경우에만 정상적인 처리 가능
	String rtnStr = "";

	if (reqNum.equals(reqNumResult)) {

		System.out.println(empName+"|-|"+rstInfoArray[8]);
		System.out.println(phoneNum+"|-|"+rstInfoArray[3]);
		System.out.println(resultCode);
		//결과코드 성공 판단
		if ((resultCode.equals("00") || resultCode.equals("02")|| resultCode.equals("52") || resultCode.equals("56"))
				&&phoneNum.equals(rstInfoArray[3])
		) {
			rtnStr +="<script type='text/javascript'>" ;
			rtnStr +="successGo();";
			rtnStr +="</script>";
		} else { //아닐경우
			rtnStr +="<script type='text/javascript'>" ;
			rtnStr +="failGo();";
			rtnStr +="</script>";
		}

	}
%>
<script type="text/javascript" src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript">
	//<![CDATA[

	function fn_insUserCert() {

		var url = rootPath+"user/insUserCert.do";

		$.ajax({
			url:url,
			crossDomain : true,
			dataType:"json",
			type:"GET",
			data: {
				resultCd:'<%=rstInfoArray[0]%>',
				ci:'<%=rstInfoArray[1]%>',
				di:'<%=rstInfoArray[2]%>',
				phonNo:'<%=rstInfoArray[3]%>',
				telCo:'<%=rstInfoArray[4]%>',
				birth:'<%=rstInfoArray[5]%>',
				gender:'<%=rstInfoArray[6]%>',
				nation:'<%=rstInfoArray[7]%>',
				name:'<%=rstInfoArray[8]%>',
				reqNum:'<%=rstInfoArray[9]%>',
				reqDate:'<%=rstInfoArray[10]%>',
				certType:'001'
			},
			success:function(result)
			{
				console.log(result);
				self.close(true);
			},
			error:function(request,status,error){
				console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				self.close(true);
				//alert("입력하신 정보를 다시 확인해주시기 바랍니다.");
				//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
	}

	function successGo() {
		alert('본인인증이 완료되었습니다.');
		fn_insUserCert();
		/*opener.parent.location.href=rootPath+"contract/contractPersonView.do?id="+escape(encodeURIComponent('<%=contractId%>'));
		self.close(true);*/
		if(opener) {
			opener.parent.location.href=rootPath+"contract/contractPersonEtcView.do?id="+escape(encodeURIComponent('<%=contractId%>'));
		} else {
			parent.location.href=rootPath+"contract/contractPersonEtcView.do?id="+escape(encodeURIComponent('<%=contractId%>'));
		}

	}
	function failGo() {
		alert('본인인증이 실패하였습니다.');
		fn_insUserCert();
		//self.close(true);
	}
	// ]]>
</script>

<% out.print(rtnStr);  %>