var yearContractId;
var febYear;

$(document).ready(function() {
	yearContractId = getCookie("yearContractId");
	febYear = getCookie("febYear");

	// 계약아이디가 없으면 메인으로 튕겨내기
    if (isNull(yearContractId)) {    	
    	jAlert('사용불가 메뉴 입니다. 기본설정값을 확인 해주세요.' , 'System Message', goHome);    	
    } else {  
    
    	$(".window").hide();
    	
    	//간이지급명세서 정보 조회
    	getList();    	
    }

});


function getList(){
		
	var formData = new FormData();
	formData.append("계약ID", yearContractId);
	formData.append("febYear", febYear);
			
	var url = rootPath + 'paymaster/getPaymentStatusMng.do';
		
	//blockUi 호출
	fncOnBlockUI();
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {	
			
			if (res.success) {
				
				//logD("# result : " + JSON.stringify(res) );
				
				var febYearHtml = "";
				var strSelected = "";
				
				for(var idx = 0 ; idx < res['ys000List'].length ; idx++){
					
					if(febYear == res['ys000List'][idx]['febYear']){
						strSelected = "selected";
						
						//쿠키값을 새로 셋팅한다.
						setCookie("yearContractId",res['ys000List'][idx]['계약ID']);
						setCookie("febYear",res['ys000List'][idx]['febYear']);
					}else{
						strSelected = "";
					}
					
					febYearHtml += "<option value='"+res['ys000List'][idx]['febYear']+"' "+strSelected+" >"+res['ys000List'][idx]['febYear']+"</option>";
				}
				$("#febYear").html(febYearHtml);
				
				var firstPayHtml = "";
				if(isNull(res['firstPayInfo']) || res['firstPayInfo'].length == 0 || isNull(res['firstPayInfo']['제출년월일'])){
					firstPayHtml += "<dl>";
					firstPayHtml += "<dt>전자신고파일을 작성하지 않았습니다.</dt><dd class='NanumRound'></dd>";
					firstPayHtml += "</dl>";
					
					$("#firstPayBtn").attr("onclick","fncAlertMessage('전자신고파일이 작성된 정보가 없습니다.');")
					
				}else{
					var year = res['firstPayInfo']['제출년월일'].substring(0,4);
					var month = res['firstPayInfo']['제출년월일'].substring(4,6);
					var day = res['firstPayInfo']['제출년월일'].substring(6,8);					
					
					firstPayHtml += "<dl>";
					firstPayHtml += "<dt>신고대상 :</dt><dd class='NanumRound'>"+res['firstPayInfo']['신고대상수']+"명</dd>";
					firstPayHtml += "</dl>";
					firstPayHtml += "<dl>";
					firstPayHtml += "<dt>최종 전자신고파일 생성일 :</dt><dd class='NanumRound'>"+year+"년 "+month+"월 "+day+"일</dd>";
					firstPayHtml += "</dl>";
					
					var 근무시기 = res['firstPayInfo']['근무시기'];
					var 제출대상구분코드 = res['firstPayInfo']['제출대상구분코드'];
					var 제출년월일 = res['firstPayInfo']['제출년월일'];
					var 계약ID = res['firstPayInfo']['계약ID'];
					var 사업장ID = res['firstPayInfo']['사업장ID'];
					var bizId = res['firstPayInfo']['bizId'];
					var 전자신고ID = res['firstPayInfo']['전자신고ID'];					
					
					$("#firstPayBtn").attr("onclick","fncMakeFileYP800('"+근무시기+"','"+제출대상구분코드+"','"+제출년월일+"','"+계약ID+"','"+사업장ID+"','"+bizId+"','"+전자신고ID+"');")
					
				}
				$("#firstPayDiv").html(firstPayHtml);
				
				var secondPayHtml = "";
				if(isNull(res['secondPayInfo']) || res['secondPayInfo'].length == 0 || isNull(res['secondPayInfo']['제출년월일'])){
					secondPayHtml += "<dl>";
					secondPayHtml += "<dt>전자신고파일을 작성하지 않았습니다.</dt><dd class='NanumRound'></dd>";
					secondPayHtml += "</dl>";
					
					$("#secondPayBtn").attr("onclick","fncAlertMessage('전자신고파일이 작성된 정보가 없습니다.');")
					
				}else{
					var year = res['secondPayInfo']['제출년월일'].substring(0,4);
					var month = res['secondPayInfo']['제출년월일'].substring(4,6);
					var day = res['secondPayInfo']['제출년월일'].substring(6,8);					
					
					secondPayHtml += "<dl>";
					secondPayHtml += "<dt>신고대상 :</dt><dd class='NanumRound'>"+res['secondPayInfo']['신고대상수']+"명</dd>";
					secondPayHtml += "</dl>";
					secondPayHtml += "<dl>";
					secondPayHtml += "<dt>최종 전자신고파일 생성일 :</dt><dd class='NanumRound'>"+year+"년 "+month+"월 "+day+"일</dd>";
					secondPayHtml += "</dl>";
					
					var 근무시기 = res['secondPayInfo']['근무시기'];
					var 제출대상구분코드 = res['secondPayInfo']['제출대상구분코드'];
					var 제출년월일 = res['secondPayInfo']['제출년월일'];
					var 계약ID = res['secondPayInfo']['계약ID'];
					var 사업장ID = res['secondPayInfo']['사업장ID'];
					var bizId = res['secondPayInfo']['bizId'];
					var 전자신고ID = res['secondPayInfo']['전자신고ID'];					
					
					$("#secondPayBtn").attr("onclick","fncMakeFileYP800('"+근무시기+"','"+제출대상구분코드+"','"+제출년월일+"','"+계약ID+"','"+사업장ID+"','"+bizId+"','"+전자신고ID+"');")
					
				}
				$("#secondPayDiv").html(secondPayHtml);
			
			} else {
				jAlert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.','');
			}
			
			$.unblockUI();
			
		},
		error:function(request,status,error){
			$.unblockUI();
		
			if (request.status=="401") {
				jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
			} else {
				jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
			}
		}
	});
	
}

//계약년도를 변경한다.
function fncFebYearChg(){
	febYear = $("#febYear").val();
	
	getList();
}

//전자신고파일을 생성한다.
function fncMakeFileYP800(근무시기,제출대상구분코드,제출년월일,계약ID,사업장ID,bizId,전자신고ID){
		
	jConfirm('전자파일을 생성 하시겠습니까?', 'Confirmation Dialog', function(cfrm) {
		
		if (cfrm) {
			
			var url = rootPath + '/febhometax/makeFileYP800.do';
			
			var formData = new FormData();
			formData.append('근무시기', 근무시기 );
			formData.append('제출대상구분코드', 제출대상구분코드 );
			formData.append('제출년월일', 제출년월일 );
			formData.append('계약ID', 계약ID );
			formData.append('사업장ID', 사업장ID );
			formData.append('bizId', bizId );
			formData.append('전자신고ID', 전자신고ID );
			formData.append('febYear', $("#febYear").val());
			
			//blockUi 호출
			fncOnBlockUI();
		    
			$.ajax({
				url:url,
			    crossDomain : true,
				dataType:"json",
				type:"POST",
				processData: false,
			    contentType: false,
				data: formData,
				success:function(result) {
					$.unblockUI();
					
					if(!result.success){
						alert(result.message);	
					}else{
						
						jConfirm('처리되었습니다.\n다운로드 하시겠습니까?', 'Confirmation Dialog', function(cfrm) {
							if (cfrm) {
								//전자문서 파일다운로드 
								passwdPopup(result);
							}
						});
																		
					}
					
				},
				error:function(request,status,error){
					$.unblockUI();
					
					if (request.status=="401") {
						alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.");
						location.href=rootPath;
					} else if (request.status=="403") {
						alert("Http 세션이 만료되었습니다."+error);				
					} else {
						alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
				        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
					}
				}
			});
			
		}
		
	});

}

var passwdPopup = function(result)
{					
	$("#yp800FileName").val(result.yp800FileName);
	$("#yp800FilePath").val(result.yp800FilePath);
	
	$("#file_password").val("");
    $(".window").show();
};

function fnc_pup_close(){
	$("#file_password").val("");
	$(".window").hide();  
	
	fnc_filedownload('N');
}

//파일다운로드 처리
var fnc_filedownload = function(encYn)
{
	
	if(encYn == 'Y' && isNull($.trim($("#file_password").val()))){
		jAlert("파일 비밀번호를 입력해주십시오.","",function(){
			$("#file_password").val('');
			$("#file_password").focus();
		});		
		
		return false;
	}
	
	$(".window").hide();
	$("#전자문서비밀번호").val($.trim($("#file_password").val()));
	
	var form = document.frm;
	form.setAttribute("action", rootPath +  "/febhometax/fileDownYP800.do");
	form.target = "blankFrame";
	form.submit();
	
	/*var url = "/febhometax/fileDownYE800.do?fileName="+encodeURI(fileName)+"&filePath="+encodeURI(filePath);
	var link = document.createElement('a');
	document.body.appendChild(link);
	link.href = url;
	link.click();*/
}

//안매문구 팝업
function fncGuidePop(){
	openWin (rootPath + "html/feb/hometax/yearTax_payment_guide_popup.html","guidePop","778","900"); 
}

