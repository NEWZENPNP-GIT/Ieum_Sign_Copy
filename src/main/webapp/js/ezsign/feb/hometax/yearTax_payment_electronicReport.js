var yearContractId;
var 근무시기;

$(document).ready(function() {
	yearContractId = getCookie("yearContractId");
	근무시기 = $("#근무시기").val();
	
	// 사업장조회
	fn_getYS030List();
	
	// 전자신고 사업장 조회
	fn_getYP800List(1);
	
    $("#chkAll").click(function(){
        var chk = $(this).is(":checked");
        if(chk) $("#dataList input").prop('checked', true);
        else  $("#dataList input").prop('checked', false);
    });

});
	
function fn_getYS030List() {
	
	var url = rootPath + '/febsystem/getYS030List.do';
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"GET",
		data: {
			계약ID:yearContractId,
			startPage:0,
			endPage:100
		},
		success:function(result) {
			if (result.success) {
				
				// 사업장 추가
				$.each(result.data, function(i, row) {
	                var option = $("<option value='"+row.사업장ID+"'>"+row.사업장명+"</option>");
	                $('#사업장ID').append(option);
				});
				
			}
		},
		error:function(request,status,error){
			if (request.status=="401") {
				alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.");
				location.href=rootPath;
			} else {
				alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
		        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		}
	});
}
	
function fn_getYP800List(page) {

	$("#chkAll").prop('checked', false);
	
	var url = rootPath+"febhometax/getYP800List.do";
	
	var viewPage = $("#viewPage").val();
	if(viewPage==""||viewPage=="0") viewPage = 10;

	if (page==0)
		curPage=0;
	else 
		curPage = page-1;
	
	if (curPage<0) {
		curPage=0;
		return;
	}
	if (curPage*viewPage>totPage) {
		curPage-=1;
		return;
	}
	
	var 사업장ID = $("#사업장ID").val();
	var searchKey = $("#searchKey").val();
	var searchValue = $("#searchValue").val();
	var startPage = (curPage*viewPage);
	var endPage = viewPage;
	var sortName = $("#sortName").val();
	var sortOrder = $("#sortOrder").val();
	
	//blockUi 호출
	fncOnBlockUI();
    
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		data: {
			계약ID:yearContractId,
			사업장ID:사업장ID,
			근무시기:근무시기, 
			searchKey:searchKey,
			searchValue:searchValue,
			startPage:startPage,
			endPage:endPage,
			sortName:sortName,
			sortOrder:sortOrder
		},
		success:function(result)
		{
			var htmlData = "";
			if (result.success==true) {
				totPage = result.total;
				var totalPage = Math.ceil(totPage/viewPage);
				
				// 전자신고 사업장내역
				$.each(result.data, function(i, row) {
					
					htmlData += '<tr class=""> ';
					htmlData += '	<td class="chk_area"><input type="checkbox" id="chk" name="chk" value="'+row.계약ID+"|"+row.사업장ID+"|"+row.bizId+"|"+row.전자신고ID+'"> </td> ';
					htmlData += '	<td class="value">'+row.사업장명+'</td> ';
					htmlData += '	<td class="value">'+isSetNull(row.사업자등록번호, '')+'</td> ';
					htmlData += '	<td class="value">'+isSetNull(row.종사업자일련번호, '')+'</td> ';

					htmlData += '	<td class="value">';
					if(row.단위과세자여부==1) htmlData += '여'; else htmlData += '부';
					htmlData += '</td> ';
					
					if(row.근로자인원수 != row.신고대상수){
						htmlData += '	<td class="value">'+AddComma(row.근로자인원수)+'</td> ';
						htmlData += '	<td class="value"><font color="red">'+AddComma(row.신고대상수)+'</font></td> ';
					}else{
						htmlData += '	<td class="value">'+AddComma(row.근로자인원수)+'</td> ';
						htmlData += '	<td class="value">'+AddComma(row.신고대상수)+'</td> ';
						
					}
					htmlData += '	<td class="value sort_right">'+AddComma(row.과세소득)+'</td> ';
					htmlData += '	<td class="value sort_right">'+AddComma(row.비과세소득)+'</td> ';
					
					if(row.근무시기 == '1'){
						htmlData += '	<td class="value">상반기</td> ';
					}else{
						htmlData += '	<td class="value">하반기</td> ';
					}
					
					htmlData += '	<td class="value">';
					if(row.제작여부==1) htmlData += '제작'; else htmlData += '미제작';
					htmlData += '</td> ';
					
					var regDate = "";
					if(row.등록일시 != null && row.등록일시 != ""){
						regDate = convertDateTime(row.등록일시).format("yyyy-MM-dd HH:mm:ss");
					}
					
					
					htmlData += '	<td class="value">'+regDate+'</td> ';
					htmlData += '</tr> ';
				});
				
				if(page==1) {
					
					$('#page-selection').bootpag({
						total: totalPage,       // total pages
						page: page,         	// default page
						maxVisible: 10, 	   	// visible pagination
						leaps: true,	      	// next/prev leaps through maxVisible
					    firstLastUse: true,
					    first: '',
					    last: '',
					    wrapClass: 'pagination',
					    activeClass: 'active',
					    disabledClass: 'disabled',
					    nextClass: 'btn_next',
					    prevClass: 'btn_prev',
					    lastClass: 'end_page',
					    firstClass: 'first_page'
					}).on("page", function(event, num){
						fn_getYP800List(num);
					}); 
				}
			} else {
				htmlData += '<tr> <td colspan="14">요청하신 자료가 존재하지 않습니다.</td></tr>';
				
			}
			$("#dataList").html(htmlData);

			$.unblockUI();
		},
		error:function(request,status,error){
			$.unblockUI();
			if (request.status=="401") {
				alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.");
				location.href=rootPath;
			} else {
				alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
		        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		}
	});
}
	
/* 전자매체 문서 생성  */
function saveYP800() {
	// 전자신고 파일 제작		
	var url = rootPath + '/febhometax/saveYP800.do';

	/*var 사업장ID = $("#사업장ID").val();
	var 제출대상구분 = $("#제출대상구분").val();
	var 제출년 = $("#제출년").val();*/
	
	var 제출월 = $("#제출월").val();
	var 제출일 = $("#제출일").val();

	
	if(제출월 < 1 || 제출월 > 12){
		alert("제출월이 잘못되었습니다.");
		$("#제출월").focus();
		return;
	}				
	if(제출일 < 1 || 제출일 > 31){
		alert("제출일이 잘못되었습니다.");
		$("#제출일").focus();
		return;
	}
	
	if(제출월.length < 2){
		if(제출월 < 10){
			제출월 = '0'+제출월;	
        }
	}
	
	if(제출일.length < 2){
        if(제출일 < 10){
        	제출일 = '0'+제출일;	
        }
	}
	
	if($("#chk:checked").length == 0){
		alert("선택된 정보가 없습니다.");
		return;
	}
	
	$("#제출년월일").val($("#제출년").val()+''+제출월+''+제출일);
 
	var params = $("#frm").serialize();			

	//blockUi 호출
	fncOnBlockUI();
    
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		data: params,
		success:function(result) {
			$.unblockUI();
			
			if(!result.success){
				alert(result.message);	
			}else{
				/*
				if(isNull(result.message)){
					alert("처리되었습니다.");						
				}else{
					alert(result.message);	
				}*/
				
				fn_getYP800List();
				$("#chkAll").prop('checked', false);
				
				if(confirm("처리되었습니다.\n다운로드 하시겠습니까?")){						
					//전자문서 파일다운로드 
					passwdPopup(result);	
				}
																			
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
	
var passwdPopup = function(result)
{					
	$("#yp800FileName").val(result.yp800FileName);
	$("#yp800FilePath").val(result.yp800FilePath);
	
	$("#file_password").val("");
    $(".window").show();
};

//파일다운로드 처리
var fnc_filedownload = function(encYn)
{
	
	if(encYn == 'Y' && isNull($.trim($("#file_password").val()))){
		alert("파일 비밀번호를 입력해주십시오.");
		$("#file_password").val('');
		$("#file_password").focus();
		
		return;
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

// 전자신고 초기데이타 생성
var basicDataCreate = function(제출대상구분코드)
{
	var formData = new FormData();
	formData.append('계약ID', yearContractId );
	formData.append('제출대상구분코드', 제출대상구분코드 );
	
	var cfrmMessage = "초기 데이타를 생성하시겠습니까?";
	if(제출대상구분코드 == '1'){
		cfrmMessage = "상반기 " + cfrmMessage;
	}else if(제출대상구분코드 == '2'){
		cfrmMessage = "하반기 " + cfrmMessage;
	}
	
	jConfirm(cfrmMessage, '', function(cfrm){
		if(cfrm){
			
			var url = rootPath + '/febhometax/insertYP800.do';
			
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
					
					if(result.success){
						jAlert("생성 되었습니다.",'',function(){
							$.unblockUI();
						});
					}else{
						jAlert("생성에 실패하였습니다.",'',function(){
							$.unblockUI();
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