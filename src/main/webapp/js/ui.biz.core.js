var bizPoint = "0";

function biz_main() {
	if (getCookie("loginType") === "9") location.href = rootPath+"biz_main.html";
	else                                alert("시스템관리자만 접근가능한 메뉴입니다.");
}

function biz_grp_main() {
	if (getCookie("loginType") > "6") location.href = rootPath+"biz_grp_main.html";
	else                              alert("중간관리자만 접근가능한 메뉴입니다.");

}

function user_main() {
	if (getCookie("loginType") > "5") location.href = rootPath+"html/user_main.html";
	else                              alert("관리자만 접근가능한 메뉴입니다.");

}

function user_grp_main() {
	if (getCookie("loginType") > "6") location.href = rootPath+"user_grp_main.html";
	else                              alert("중간관리자만 접근가능한 메뉴입니다.");
}

function contract_main() {
	if (getCookie("loginType") > "5") location.href = rootPath+"contract_main.html";
	else                              alert("관리자만 접근가능한 메뉴입니다.");
}

function contract_docu() {
	if (getCookie("loginType") > "5") location.href = rootPath+"contract_docu.html";
	else                              alert("관리자만 접근가능한 메뉴입니다.");
}

function paystub_main() {
	if (getCookie("loginType") > "5") location.href = rootPath+"paystub_main.html";
	else                              alert("관리자만 접근가능한 메뉴입니다.");
}

function annual_main() {
	if (getCookie("loginType") > "5") location.href = rootPath+"annual_main.html";
	else                              alert("관리자만 접근가능한 메뉴입니다.");
}

function system_main() {
	if (getCookie("loginType") === "9") location.href = rootPath+"system_main.html";
	else                                alert("시스템관리자만 접근가능한 메뉴입니다.");
}

function fn_bizAdd() {
	if (getCookie("loginType") > "5") window.open(rootPath+"biz_add.html", "BizAdd", "width=470, height=540, scrollbars=no");
	else                              alert("관리자만 접근가능한 메뉴입니다.");
}

function fn_bizInfo(bizId) {
	if (getCookie("loginType") > "5") window.open(rootPath+"biz_info.html?bizId="+bizId, "BizManager", "width=470, height=540, scrollbars=no");
	else                              alert("관리자만 접근가능한 메뉴입니다.");
}

function fn_userBox() {
	if (getCookie("loginType") > "5") window.open(rootPath+"user_box.html", "UserPassword", "width=470, height=540, scrollbars=no");
	else                              alert("관리자만 접근가능한 메뉴입니다.");
}

function fn_contractInfo(contId,userId, contractDate) {
	if (getCookie("loginType") >= "5") window.open(rootPath+"html/contract/contract_popup_form_edit.html?contId="+contId+"&contractDate="+contractDate+"&userId="+userId, "FormManager", "width=500, height=560, scrollbars=no");
	else                               alert("관리자만 접근가능한 메뉴입니다.");
}

function fn_contractUpload() {
	if (getCookie("loginType") > "5") window.open(rootPath+"contract_upload.html", "ContractUpload", "width=470, height=540, scrollbars=no");
	else                              alert("관리자만 접근가능한 메뉴입니다.");
}


function fn_contractDownload() {
	windowObj = window.open(rootPath+"html/contract/contract_popup_download.html", "ContractDownload", "width=500, height=610, scrollbars=no");
}

function fn_contractNewUpload() {
	var bizId = $("#searchCompany").val()
	if(!isNull(bizId)){
		window.open(rootPath+"html/contract/contract_popup_form_regist.html?bizId="+bizId, "ContractFileUpload", "width=525, height=606, scrollbars=no");
	}
}

function fn_contractNewUpdate(id, fileName) {
	if (getCookie("loginType") === "9") {
		var bizId = $("#searchCompany").val()
		if (!isNull(bizId)) window.open(rootPath+"html/contract/contract_popup_convert_regist.html?fileId=" + id + "&fileName=" + encodeURI(encodeURIComponent(fileName)) + "&bizId=" + bizId, "ContractFileUpdate", "width=500, height=560, scrollbars=no");
	} else      alert("시스템관리자만 접근가능한 메뉴입니다.");
}

function fn_contractNewSignUpdate(id, bizId) {
	window.open(rootPath+"html/contract/contract_popup_form_change.html?fileId=" + id + "&bizId=" + bizId, "ContractFileSignUpdate", "width=580, height=560, scrollbars=no");
}

function fn_contractNewSignUpdate2(id, bizId) {
	window.open(rootPath+"html/contract/contract_popup_form_change2.html?fileId=" + id + "&bizId=" + bizId, "ContractFileSignUpdate", "width=500, height=350, scrollbars=no");
}

//노무첨삭 팝업
function fn_contractWorkSOS(id) {
	window.open(rootPath+"html/contract/contract_popup_editingCheckg.html?fileId=" + id, "ContractFileSignUpdate", "width=680, height=600, scrollbars=no");
}

function fn_fileDownload(id) {
	if (isSetNull(id, "") === "") {
		alert("파일이 존재하지 않습니다.");
		return;
	}
	var url = rootPath+"content/downloadFile.do?fileId="+id;
	downloadDataUrlFromJavascript(url);
}

function fn_checkService(serviceType, redirectUri) {
	var url = rootPath+"biz/getCheckService.do";
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		data: { serviceType:serviceType },
		success:function(result) {
			if (result.success === true && result.total>0) location.href = redirectUri;
			else 										   alert("서비스 사용신청을 먼저 해주시기 바랍니다.");
		},
		error:function(request,status,error){
			if (request.status === "401") {
				alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.");
				location.href=rootPath;
			} else {
				alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
			}
		}
	});
}

function fn_logout() {
	if (!confirm("로그아웃을 하시겠습니까?")) return;
	var url = rootPath+"logout.do";
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		success:function(result) {
			if (result.success === true) location.href = rootPath;
			else alert("입력하신 정보를 다시 확인해주시기 바랍니다.");
		},
		error:function(request,status,error){
			alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
		}
	});
}

function fn_pointAdd(id) {
	window.open(rootPath+"goPointCharge.do?bizId="+id, "PointAdd", "width=845, height=625, scrollbars=no");
}

function fn_getPoint() {
	var url = rootPath+"getPoint.do";
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"GET",
		success:function(result) {
			if (result.success) bizPoint = AddComma(result.point);
			else 				bizPoint = getCookie("curPoint");

			var bizId = result.bizId;
			var htmlData = '<a class="btn_inner NanumSquare_R" onclick="fn_pointAdd(\'' + bizId + '\')">포인트 충전</a>'
			
			$("#curPoint").html(bizPoint);
			$("#pointCharge").html(htmlData);
		},
		error:function(request,status,error) { }
	});	
}

function fn_processHistory(contId) {
	window.open(rootPath+"html/popup/process_history.html?contId=" + contId, "processHistory", "width=882, height=606, scrollbars=no");
}

function fn_fileDocumentDownload(id) {
	if (isSetNull(id, "") === "") {
		alert("파일이 존재하지 않습니다.");
		return;
	}
	var url = rootPath+"document/downDocumentPdf.do?fileId="+id;
	downloadDataUrlFromJavascript(url);
}

//Init
function fn_initUiBizCore() {
	var bizName = "";
	var loginName = "";
	
	bizName   = textLengthOverCut(getCookie("bizName"), 10, "...");
	loginName = textLengthOverCut(getCookie("loginName"), 10, "...");

	$("#bizName").html(bizName);
	$("#loginName").html(loginName);
	
	fn_getPoint();	
}