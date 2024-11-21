<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, user-scalable=no">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">

<title>포인트 충전</title>
<link rel="stylesheet" type="text/css" href="/css/font_pc.css">
<link rel="stylesheet" type="text/css" href="/css/material-icons.min.css">
<link rel="stylesheet" type="text/css" href="/css/myPage_pointCharging_new.css">

<script type="text/javascript" src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/js/jquery.easydropdown.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript" src="/js/ui.biz.core.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script>
	var firstChk = false;
	var payAmount;
	$(document).ready(function(){
		fn_curPoint();

		// 선택된 라디오버튼 checked
		$('.point_price .item tbody td input[type="radio"]').click(function (){
			$(this).attr("checked", "checked");
			$('.point_price .item tbody td input[type="radio"]').not(this).removeAttr("checked");
		});

		//직접입력 - 입력 시
		$("#userIn").keyup(function(event) {
			console.log("userIn==keyup");
			// 사용자 직접입력 체크 확인
			$('#userPointCk').prop("checked", "checked"); 
			if($(this).val()<0) {return false;}
			payAmount = $(this).val() * 110;
			$("#chargePrice").text(AddComma(payAmount));
			$("#payAmount").val(payAmount);
		});

		//결제방법 선택
		$("#paymentState").change(function (){
			if($(this).val() == "TAX"){
				$('.point_price .txt').show();
				$('.taxBill_table').show();
				$('.txtBill_box > li').eq(1).hide();
				window.resizeTo(855, 850);
			}else{
				$('.point_price .txt').hide();
				$('.taxBill_table').hide();
				$('.txtBill_box > li').eq(1).show();
				window.resizeTo(855, 710);
			}
		});

		//결제방법 - 세금계산서 - 입금여부
		$("#depositYn").change(function (){
			if($(this).val() == "Y"){
				$(".dipositN").hide();
				$(".dipositY").show();
			}else{
				$(".dipositN").show();
				$(".dipositY").hide();
			}
		});

		$("#bizName").val(getCookie("bizName"));

		//결제방법 - 세금계산서 - 사업자등록증 파일 업로드
		$("#imgFile").change(function() {
			var file = $('#imgFile')[0].files[0].name;
			$("#fileName").text(file);
		});
	});
	
	/* 결재 module 시작 */

	webbrowser=navigator.appVersion;

	function on_pay(type){
		
		var width = 720;
		var height = 630;
		
		if(type == "TMONEY"){
			var width = 350;
			var height = 475;
		}
		
		var xpos = (screen.width - width) / 2;
		var ypos = (screen.width - height) / 6;
		var position = "top=" + ypos + ",left=" + xpos;
		var features = position + ", width="+width+", height="+height+",toolbar=no, location=no";
		
		window.name = "STPG_CLIENT";
		wallet = window.open('', 'STPG_WALLET', features);
		
		if ( wallet != null) {
			document.order_form.t_PUname.value = getCookie("bizName");
			strEncode();//한글인코딩
			document.order_form.action = getActionUrl(type);
			document.order_form.target = "STPG_WALLET";
			document.order_form.submit();
		} else {

			if ((webbrowser.indexOf("Windows NT 5.1")!=-1) && (webbrowser.indexOf("SV1")!=-1)) {	// Windows XP Service Pack 2
				alert("팝업이 차단되었습니다. 브라우저의 상단 노란색 [알림 표시줄]을 클릭하신 후 팝업창 허용을 선택하여 주세요.");
			} else {
				alert("팝업이 차단되었습니다.");
			}
		}
		
	}

	function getActionUrl(type){
		
		var actionUrl = "";
		if(type == "CARD"){
			actionUrl = pgUrl + "/card/NewCardAction.do";
		}else if (type == "BANK"){
			actionUrl = pgUrl + "/bank/NewBankAction.do";
		}else if (type == "MOBILE"){
			actionUrl = pgUrl + "/mobile/NewMobileAction.do";
		}else if (type == "VBANK"){
			actionUrl = pgUrl + "/vbank/NewVBankAction.do";
		}else if (type == "TEEN"){
			actionUrl = pgUrl + "/gift/NewTeenCashAction.do";
		}else if (type == "HAPPY"){
			actionUrl = pgUrl + "/gift/NewHappyMoneyAction.do";
		}else if (type == "CULTURE"){
			actionUrl = pgUrl + "/gift/NewCultureCashAction.do";
		}else if (type == "SMARTCUL"){
			actionUrl = pgUrl + "/gift/NewSmartCultureCashAction.do";
		}else if (type == "BOOK"){
			actionUrl = pgUrl + "/gift/NewBookCashAction.do";
		}else if (type == "TMONEY"){
			actionUrl = pgUrl + "/tmoney/TmoneyAction.do";
		}
		
		return actionUrl;
	}

	//파라미터 값이 한글인 경우 여기서 인코딩을 해준다.
	function strEncode(){
		var order_form = document.order_form;
		order_form.PGoods.value = encodeURI(order_form.t_PGoods.value);
		order_form.PNoti.value = encodeURI(order_form.t_PNoti.value);
		order_form.PMname.value = encodeURI(order_form.t_PMname.value);
		order_form.PUname.value = encodeURI(order_form.t_PUname.value);
		order_form.PBname.value = encodeURI(order_form.t_PBname.value);
	}

	//goResult() - 함수설명 : 결재완료후 결과값을 지정된 결과페이지(pay_result.jsp)로 전송합니다.
	function goResult(){
		document.order_form.target = "";
		document.order_form.action = "/payment/pay_result.jsp";
		document.order_form.submit();
	}
	// eparamSet() - 함수설명 : 결재완료후 (pay_rcv.jsp로부터)결과값을 받아 지정된 결과페이지(pay_result.jsp)로 전송될 form에 세팅합니다.
	function rstparamSet(data,hash,state,oid){
		document.order_form.PData.value 	= data;
		document.order_form.PHash.value 	= hash;
		document.order_form.PStateCd.value 	= state;
		document.order_form.POrderId.value 	= oid;
	}
	/* 결재 module 끝 */ 
	
	function fn_curPoint() {
		var bizId = getParams().bizId;
		var url = rootPath+"getPoint.do";

		$.blockUI();

		$.ajax({
			url:url,
		    crossDomain : true,
			dataType:"json",
			type:"GET",
			data: {
				bizId:bizId
			},
			success:function(result)
			{
				$.unblockUI();
				if(result.success) {

					console.log(result);
					var curPoint = AddComma(result.point);					
					$("#curPoint").html(curPoint);
					$("#bizName").val(result.bizName);
					$("#taxBillBizName").val(result.taxBillBizName);
					$("#taxBillName").val(result.taxBillName);
					$("#taxBillTel").val(result.taxBillTel);
					$("#taxBillEmail").val(result.taxBillEmail);

					if(result.taxBillTel == ""){
						firstChk = true;
					}else{
						$(".bizReg").hide();
					}
				}
			},
			error:function(request,status,error){
				$.unblockUI();
				if (request.status=="401") {
					alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.");
					window.close();
				} else {
					alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
			        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
			}
		});
		
	}
	
	function fn_setPayAmount(obj){
		$("#payAmount").val(obj.value);
	}
	
	function fn_setPayAmountUser() {	
		payAmount = $("#userIn").val() * 110;
		$("#chargePrice").text(AddComma(payAmount));
		$("#payAmount").val(payAmount);
	}
	
	function fn_pointAdd() {
		var paymentState = $("#paymentState").val();
		payAmount = $("#payAmount").val();
		
		var bizId = getParams().bizId;
		
		payAmount = removeSpecialChars(payAmount);
		
		if(paymentState.length==0) {
			alert("결제수단을 선택해주시기 바랍니다.");
			return;
		}
		if(isNull(payAmount)) {
			alert("결제금액을 입력해주시기 바랍니다.");
			return;
		}
		if(payAmount.length==0) {
			alert("결제금액을 입력해주시기 바랍니다.");
			return;
		}
		if(payAmount<11000 || payAmount>1100000) {
			alert("결제금액이 만원이상 백만원 미만인 경우에만 결제가 가능합니다.\r\n다시 결제금액을 입력해주시기 바랍니다.");
			$("#payAmount").focus();
			return;
		}
		/* if(!$("#checkApproval").prop("checked")) {
			alert("결제 동의를 체크해주시기 바랍니다.");
			$("#checkApproval").focus();
			return;
		} */
		
		var point = payAmount/110;
		if(!confirm("포인트["+AddComma(point)+"]에 대해서 충전을 진행하시겠습니까?\r\n충전금액은 부가세가 포함된 금액입니다.")) return;
		
		if(paymentState == "TAX"){
			//가격설정 (부가세포함)
			var curPoint = $("#curPoint").text().replaceAll(",", "");

			var bizName = $("#bizName").val();
			var taxBillBizName = $("#taxBillBizName").val();
			var taxBillName = $("#taxBillName").val();
			var taxBillTel = $("#taxBillTel").val();
			var taxBillEmail = $("#taxBillEmail").val();
			var depositYn = $("#depositYn").val();
			var depositDate = $("#depositDate").val();

			if(taxBillBizName == "" || taxBillName == "" || taxBillTel == "" || taxBillEmail ==""){
				alert("세금계산서 발행 정보를 입력하세요.");
				return;
			}

			if(depositYn == "N"){
				if(depositDate == ""){
					alert("입금여부가 미입금인경우 입금예정일을 입력하시기 바랍니다.");
					return;
				}
			}

			var formData = new FormData();
			formData.append("bizId", bizId);
			formData.append("chargePoint", point);
			formData.append("curPoint", curPoint);
			formData.append("bizName", bizName);
			formData.append("taxBillBizName", taxBillBizName);
			formData.append("taxBillName", taxBillName);
			formData.append("taxBillTel", taxBillTel);
			formData.append("taxBillEmail", taxBillEmail);
			formData.append("depositYn", depositYn);
			formData.append("depositDate", depositDate);

			var file = document.getElementsByName("imgFile");
			for(var i=0;i<file.length;i++) {
				if (firstChk){
					if (file[i].files[0] != undefined) {
						if(checkSpecial(file[i].files[0].name)) {
							alert("파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.");
							return;
						}
						formData.append("addFile"+i,file[i].files[0]);
					}else{
						alert("사업자등록증을 등록 해주시기 바랍니다.");
						return;
					}
				}
			}

			var url = rootPath+"payment/insTaxPaymentRequest.do";

			$.blockUI();

			$.ajax({
				url:url,
				crossDomain : true,
				dataType:"json",
				type:"POST",
				data:formData,
				contentType:false,
				processData:false,
				cache:false,
				success:function(result)
				{
					$.unblockUI();
					if(result.success) {
						setCookie("curPoint", curPoint+point);
						alert("발행 요청이 완료되었습니다.");
						window.close();
					} else {
						alert("오류가 발생하였습니다. 관리자에게 문의 바랍니다.");
					}
				},
				error:function(request,status,error){
					$.unblockUI();
					if (request.status=="401") {
						alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.");
						window.close();
					} else {
						alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
				        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
					}
				}
			});
		}else{
			//가격설정 (부가세포함)
			// payAmount = parseInt(payAmount) + (payAmount/10);
			payAmount = parseInt(payAmount);
			document.order_form.PAmt.value = payAmount;
			
			var url = rootPath+"payment/insPaymentRequest.do";

			$.blockUI();

			$.ajax({
				url:url,
			    crossDomain : true,
				dataType:"json",
				type:"GET",
				data: {
					bizId:bizId,
					point:point,
					pAmt:payAmount
				},
				success:function(result)
				{
					$.unblockUI();
					if(result.success) {
						document.order_form.POid.value = result.data;
						on_pay(paymentState);
						window.close();
					} else {
						alert("결제요청시 주문번호 생성시 오류가 발생하였습니다.");
					}
				},
				error:function(request,status,error){
					$.unblockUI();
					if (request.status=="401") {
						alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.");
						window.close();
					} else {
						alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
				        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
					}
				}
			});
		}
	}

	function fn_fileUpload() {
		$("#imgFile").click();
	}
</script>
</head>
<body>
<form name="order_form" method="post">

	<!-- 결과처리를 위한 파라미터 -->
	<input type="hidden" name="PHash" value="">
	<input type="hidden" name="PData" value="">
	<input type="hidden" name="PStateCd" value="">
	<input type="hidden" name="POrderId" value="">
	<!-- 결과처리를 위한 파라미터 -->

	<input type="hidden" name="PNoteUrl" value="https://ieumsign.co.kr/payment/insPayment.do"> <!--db처리 url 예)http://www.***.com/rnoti.jsp -->
	<input type="hidden" name="PNextPUrl" value="https://ieumsign.co.kr/payment/goPaymentResult.do"> <!--성공,실패 화면처리 예)http://www.***.com/pay_rcv.jsp -->
	<input type="hidden" name="PCancPUrl" value="https://ieumsign.co.kr/payment/goPaymentResult.do"> <!-- 결제창을 닫은 경우 화면처리 예)http://www.***.com/pay_rcv.jsp -->

	<input type="hidden" name="PEmail" value=""> <!-- 결제자 e-mail -->
	<input type="hidden" name="PPhone" value=""> <!-- 결제자 연락처 -->
	<input type="hidden" name="POid"> <!-- P_OID를 회원사에서 직접넘겨주는 경우에 함수 on_load()에서 주문번호 넣는 부분을 주석처리하시기 바랍니다 -->
	<input type="hidden" name="t_PGoods" value="이음싸인 포인트"> <!-- 상품명-->
	<input type="hidden" name="t_PNoti" value=""> <!-- 회원사에서 이용할 수 있는 여유필드 -->
	<input type="hidden" name="t_PMname" value="뉴젠피앤피"> <!-- 회원사 한글명 -->
	<input type="hidden" name="t_PUname" value=""> <!-- 결제자 이름-->
	<input type="hidden" name="t_PBname" value="뉴젠피앤피"> <!-- 계좌이체/가상계좌입금시 고객통장에 찍힐 통장인자명 -->
	<input type="hidden" name="PEname" value="NEWZENPNP"> <!-- 신용카드 결제시 영문가맹점명 -->
	<input type="hidden" name="PVtransDt" value=""> <!-- 가상계좌입금마감일 : 가상계좌에서만 사용합니다 예)20120101235959  -->
	<input type="hidden" name="PUserid" value=""> <!-- 가맹점 사용자 ID -->
	<!-- 신용카드 결제시 사용 -->
	<input type="hidden" name="PCardType" value=""> <!-- 카드결제 타입 [ 3: 앱카드 전용 결제 ] / 6: 현대카드 페이샷  -->
	<input type="hidden" name="PChainUserId" value=""> <!-- 현대카드 페이샷 고객 키값 -->
	
	<!-- 한글처리위해 비워둡니다. -->
	<input type="hidden" name="PGoods"> 
	<input type="hidden" name="PNoti"> 
	<input type="hidden" name="PMname">
	<input type="hidden" name="PUname">
	<input type="hidden" name="PBname">

	<!-- 상점아이디와 금액 -->
	<input type="hidden" name="PMid" value="newzenpnp">
	<input type="hidden" name="PAmt" value="">

	<input type="hidden" id="bizName" name="bizName" value="">
</form>
<div id="pointCharging_wrap">
	<section class="point_box">
		<article class="point_view">
			<div class="item">
				<h3>잔여 포인트</h3>
				<p><strong id="curPoint">0</strong>P</p>
			</div>
			<ul class="txt">
				<li>ㆍ포인트 정책 : 100원 / 1P</li>
				<li>ㆍ전자계약서 전송 서비스 : <span>1건 / 10P</span></li>
				<li>ㆍ회사 양식 등록 서비스 : <span>1건 / 100P</span></li>
				<li>ㆍ최종 계약서 재전송 : <span>1건 / 1P</span></li>
			</ul>
		</article>
		<article class="point_price">
			<div class="item">
				<table>
					<caption>포인트 결제 금액 선택 (VAT 포함)</caption>
					<colgroup>
						<col width="8%">
						<col width="*">
						<col width="30%">
					</colgroup>
					<thead>
						<tr>
							<th colspan="2">포인트</th>
							<th>결제금액</th>
						</tr>
					</thead>
					<tbody>
						<input type="hidden" id="payAmount" value="55000">
						<tr>
							<td><input type="radio" name="pointCk" onclick="fn_setPayAmount(this)" value="55000" checked></td>
							<td><strong>500</strong> P</td>
							<td><strong>55,000</strong> 원</td>
						</tr>
						<tr>
							<td><input type="radio" name="pointCk" onclick="fn_setPayAmount(this)" value="110000"></td>
							<td><strong>1,000 </strong> P</td>
							<td><strong>110,000</strong> 원</td>
						</tr>
						<tr>
							<td><input type="radio" name="pointCk" onclick="fn_setPayAmount(this)" value="550000"></td>
							<td><strong>5,000 </strong> P</td>
							<td><strong>550,000</strong> 원</td>
						</tr>
						<tr>
							<td><input type="radio" name="pointCk" id="userPointCk" onclick="fn_setPayAmountUser()" value=""></td>
							<td>직접입력 <input class="userIn" type="number" min="10" max="10000" id="userIn" step="10" > P</td>
							<td><strong id ="chargePrice">0</strong> 원</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="paymentChoice">
				<select id="paymentState">
				    <option value=""> 결제방법</option>
				    <option value="BANK">계좌이체</option>
					<option value="VBANK">가상계좌결제</option>
					<option value="TAX">세금계산서</option>
				</select>
				<a href="javascript:fn_pointAdd();">충전하기</a>
			</div>
			<div class="txt">
				<ol>
					<li>포인트 충전 결제 금액은 하단의 계좌로 직접 이체합니다.<br><strong>KB국민은행 392801-04-214028 (주)뉴젠피앤피</strong></li>
					<li>[충전요청]시 포인트 후 충전 후 세금계산서를 발행합니다.<br>게시판>관리자문의하기에서 처리사항 확인 가능합니다.</li>
					<li>
						입금여부체크
						<select id="depositYn">
							<option value="N">미입금</option>
							<option value="Y">입금</option>
						</select>
					</li>
				</ol>
			</div>
		</article>
	</section>

	<table class="taxBill_table">
		<caption>※ 세금계산서 발행 정보 입력</caption>
		<colgroup>
			<col width="15%">
			<col width="*">
			<col width="15%">
			<col width="*">
		</colgroup>
		<tbody>
		<tr>
			<th>회사명</th>
			<td><input type="text" id="taxBillBizName"></td>
			<th>담당자명</th>
			<td><input type="text" id="taxBillName"></td>
		</tr>
		<tr>
			<th>연락처</th>
			<td><input type="text" id="taxBillTel"></td>
			<th>이메일</th>
			<td><input type="text" id="taxBillEmail"></td>
		</tr>
		<%--
		<tr>
			<td colspan="2" style="text-align: left"><span style="color: #00a9e9; font-size: 10pt;"></span></td>
			<td colspan="2" style="text-align: left"><span style="color: #00a9e9; font-size: 10pt;">※ 세금계산서 발행 이메일주소를 입력합니다.</span></td>
		</tr>
		--%>
		<tr>
			<th class="dipositN">입금예정일</th>
			<td class="dipositN"><input type="text" id="depositDate" maxlength="20" placeholder="최대 20자까지 입력가능"></td>
			<td colspan="2" class="dipositY"></td>
			<td colspan="2" class="blue">※ 세금계산서 발행 이메일주소를 입력합니다.</td>
		</tr>
		<tr class="dipositN">
			<td colspan="2" class="blue">※ 입금여부가 미입금인 경우 입금예정일을 입력합니다.</td>
		</tr>
		<tr class="bizReg">
			<th>사업자등록증</th>
			<td>
				<input type="file" id="imgFile" name="imgFile">
				<a href="javascript:fn_fileUpload();">파일선택</a>
				<div class="default_box down_file">
					<span class="file_icon"></span>
					<span class="file_name" id="fileName"></span>
				</div>
			</td>
		</tr>
		</tbody>
	</table>

	<ol class="txtBill_box">
		<li>
			사용 후 남은 잔여 포인트에 대해서는 환불이 가능하며, 서비스 이용약관12조 이용요금의 반환 항목에 준하여 진행합니다.<br>
			단, 이벤트 및 무료로 지급한 포인트에 대해서는 환불이 불가합니다.
		</li>
		<li>
			포인트 충전하기를 진행하시면 결제 대행사인 "세틀뱅크"로 현금영수증 발행을 선택하실 수 있습니다.<br>
			당사에서는 해당 충전 금액에 대한 세금계산서는 별도로 발행하지 않습니다.
		</li>
		<li>기타 문의사항은 <span>service@newzenpnp.com</span>을 통하여 접수하시기 바랍니다.</li>
	</ol>
</div>
</body>
</html>