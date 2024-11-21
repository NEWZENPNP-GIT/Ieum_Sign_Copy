<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=no">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache"/>
<title>인적자원관리 전문IT기업 - 뉴젠P&amp;P</title>

<link rel="stylesheet" type="text/css" href="/css/font_pc.css">
<link rel="stylesheet" type="text/css" href="/css/material-icons.min.css">
<link rel="stylesheet" type="text/css" href="/css/bootstrap_candy.css">
<link rel="stylesheet" type="text/css" href="/css/yearTax_common.css">

<script src="/js/jquery.min.js"></script>
<script src="/js/common.js"></script>

<script type="text/javascript">
	function fn_serviceJoin() {
		var cnt = 0;
		
		if ($("#approval1").prop("checked")) cnt++;
		if ($("#approval2").prop("checked")) cnt++;
		
		if (cnt >= 2) {
			// 연말정산 서비스 등록
			fn_insBizServiceAdd();
		} else {
			alert("약관에 동의해주시기 바랍니다.");
			return;
		}
	}
	
	// 연말정산 서비스등록
	function fn_insBizServiceAdd() {
		var url = rootPath+"insBizServiceAdd.do";
		
		var businessNo = getURLParameters("businessNo");
		var serviceType = '003';
		
		$.ajax({
			url:url,
		    crossDomain : true,
			dataType:"json",
			type:"POST",
			data: {
				businessNo: businessNo,
				serviceType: serviceType
			},
			success:function(result)
			{
				if (result.success) {
					alert("연말정산 서비스에 가입되었습니다.")
					window.close();
				} else {
					alert("뉴젠피앤피 담당자에게 문의 바랍니다.")
				}
				
			},
			error:function(request,status,error){
		        alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
	}
	
	// 서비스가이드
	function fn_serviceGuide() {
		openWin("http://hometaxyear.com/service/service.jsp?c=1", "서비스가이드");
	}
	
	// 이용약관 팝업
	function fn_servicePolicy(type) {
		if (type == 1) {
			openWin (rootPath+"html/popup/popup_servicePolicy.html","이용약관", "400", "455");
		}
	}
</script>
</head>
<body>
	<div id="yearTax_wrap_popup" class="yearTax_popup join">
		<div class="container">
			<div id="popup_tit">
				<div id="popup_close" class="btn_type col_skyblue" onclick="window.close()"></div>
			</div>
			<ul class="contents">
				<li class="group_loc"><div class="NanumRound">현재 <span class="col_skyblue">캔디 연말정산</span> <span>서비스</span>를</div> 
					<div><span class="NanumRound">선택하셨습니다.</span></div></li>
				<li class="group_wrap">
					<div class="tit"><span class="NanumSquare_R">서비스 이용 금액 안내</span></div>
					<div class="textBox NanumSquare_R">
						<span>근로자 1인</span><span>X</span><span>1년</span><span>X</span><span class="row2">모바일 캔디 연말정산</span><span>=</span><span class="price">3,000원</span><span class="vat">(VAT별도)</span>
					</div>
				</li>
				<li class="group_wrap">
					<div class="tit"><span class="NanumSquare_R">서비스 이용안내</span></div>
					<div class="textList NanumRound">
						<div><span class="dot">ㆍ</span><span>이음싸인_연말정산 서비스는 모바일 캔디로 근로자가 추가서류를 업로드하고 결과를 확인할 수 있는 서비스입니다.<a class="btn_type btn_goGuide" onclick="fn_serviceGuide()">서비스 가이드 보기 ＞</a></span></div>
						<div><span class="dot">ㆍ</span><span>근로자는 모바일 앱을 이용하여 연말정산 업무를 수행할 수 있습니다.</span></div>
						<div><span class="dot">ㆍ</span><span>이용 요금은 '모바일캔디 연말정산' 기능을 이용한 근로자수 기준입니다. </span></div>
						<div><span class="dot">ㆍ</span><span>해당 서비스는 개인정보를 이용함으로 개인정보제공 동의가 필요합니다.</span></div>
						<div><span class="dot">ㆍ</span><span>뉴젠 피앤피 연말정산은 이용기간인 1년동안 정보를 보호하고 있습니다.</span></div>
					</div>
				</li>
				<li class="agree_box Dotum">
					<div><input type="checkbox" id="approval1" /><span>개인정보 취급과 관련된 사항에 대한 약관에 동의합니다.</span><a class="btn_type btn_termsView" onclick="fn_servicePolicy(1)">약관보기 ＞</a></div>
					<div><input type="checkbox" id="approval2" /><span>유료 서비스 이용 약관에 동의합니다.</span><a class="btn_type btn_termsView" onclick="fn_servicePolicy(2)">약관보기 ＞</a></div>
				</li>
				<li class="btns NanumRound">
					<a class="btn_type btn_join" onclick="fn_serviceJoin()">서비스 가입</a>
					<a class="btn_type btn_calcel" onclick="window.close()">취소</a>
				</li>
			</ul>
		</div> <!--  container -->
	</div>
</body>
</html>