<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset= UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=no">
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=yes, target-densitydpi=medium-dpi" />
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">

<meta name="google-signin-scope" content="">
<meta name="google-signin-client_id" content="59382001111-j6qe2a2jmv7dmhc34tjku0j3l34hpeb3.apps.googleusercontent.com">
<script src="https://apis.google.com/js/platform.js" async defer></script>
<title>인적자원관리 전문IT기업 - 뉴젠P&amp;P</title>
<link rel="stylesheet" type="text/css" href="/css/font.css">
<link rel="stylesheet" type="text/css" href="/css/login.css">
<link rel="stylesheet" type="text/css" href="/css/bootstrap_candy.css">
<script type="text/javascript" src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<!-- bootPag -->
<script type="text/javascript" src="/js/jquery.bootpag_candy.js"></script>
<script src="/js/common.js"></script>
<style>
</style>
<script type="text/javascript">
	$(document).ready(function(){
		displaySize();
		
		$(window).resize(function() {
	        displaySize();
	    });
		
		fn_memberList(1);
		
		$("#searchValue").keyup(function(event){
		    if(event.keyCode == 13){
		    	fn_memberList(1);
		    }
		});
	});
	
	/** id/pw 찾기 **/
    $("#btn_info > .btn_type").click(function(){
    	var target=$(this).attr("id").substring(4,6);
    	
    	if(target=="id"){
    		$("#popup_wrap.search_id").removeClass("hidden");
    	}else{
    		$("#popup_wrap.search_pw").removeClass("hidden");
    	}
    });
    
    $("#popup_close.btn_type").click(function(){
    	$(this).parent().parent().addClass("hidden");
    });
    /* <!-- id/pw찾기 --> */

	function displaySize(){
		//if($(document).width()>360){
		$("#member_wrap").css("height", $(window).height()-144+"px")
		$("#member_content").css("margin-left",($(document).width()-$("#member_wrap").width())/2+"px");
        $("#member_box").css("margin-left",($("#member_content").width()-$("#member_box").width())/2+"px");
        $("#member_box").css("height", $("#member_wrap").height()-64+"px");
        
        $("#login_bg").css("height", $(window).height()-1+"px");
		$("#bg_bottom").css("top", ($("#login_bg").height()-$("#bg_bottom").height())+"px");
		$(".list").css("height", $("#member_box").height()-265+"px")
    }
    
    function fn_memberList(page) {
		var url = rootPath+"welmoney/getMemberList.do";
		
		var viewPage = 10;
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
		
		var searchKey = $("#searchKey").val();
		var searchValue = $("#searchValue").val();
		var startPage = (curPage*viewPage);
		var endPage = viewPage;

		$.blockUI();
	    
		$.ajax({
			url:url,
		    crossDomain : true,
			dataType:"json",
			type:"GET",
			data: {
				searchKey:searchKey,
				searchValue:searchValue,
				startPage:startPage,
				endPage:endPage,
			},
			success:function(result)
			{
				var htmlData = "";
				console.log("contract_list:" + result);
				if (result.success==true) {
					totPage = result.total;
					console.log(totPage);
					console.log(viewPage);
					var totalPage = Math.ceil(totPage/viewPage);
					
					$.each(result.data, function(i, row) {
						
						var num = (curPage * viewPage) + i + 1;
						htmlData += '<tr class="databox"> ';
						htmlData += ' <td>'+num+'</td> ';
						htmlData += ' <td>'+row.userId+'</td> ';
						htmlData += ' <td class="name">'+row.userName+'</td> ';
						if(!isNull(row.startDate) && row.startDate!="0") {
							htmlData += ' <td>'+convertDate(row.startDate).format("yyyy-MM-dd")+'</td> ';
						} else {
							htmlData += ' <td></td>';
						}
						if(!isNull(row.endDate) && row.endDate!="0") {
							htmlData += ' <td>'+convertDate(row.endDate).format("yyyy-MM-dd")+'</td> ';
						} else {
							htmlData += ' <td></td>';
						}
						htmlData += ' <td>'+row.phoneNum+'</td> ';
						htmlData += ' <td>'+row.EMail+'</td> ';
						htmlData += ' <td><span style="cursor:pointer;" class="glyphicon glyphicon-edit" onclick="fn_update(\''+row.userId+'\')"></span></td> ';
						
						htmlData += '</tr> ';
					});
					
					if(page==1) {
						$('#page-selection').empty();
						$('#page-selection').removeData("twbs-pagination");
						$('#page-selection').unbind("page");
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
							fn_memberList(num);
						}); 
					}
				} else {
					htmlData += '<tr> <td colspan="8">요청하신 자료가 존재하지 않습니다.</td></tr>';
					totPage = result.total;
					
					$('#page-selection').empty();
					$('#page-selection').removeData("twbs-pagination");
					$('#page-selection').unbind("page");
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
    
    function fn_save(){
	    openWin (rootPath+"welmoney/memberDetail.do","사용자정보", "500", "370");
    }
    
    function fn_update(userId){
    	openWin (rootPath+"welmoney/memberDetail.do?userId="+userId,"사용자정보", "500", "370");
    }
</script>
</head>

<body oncontextmenu="return false">
	<div id="login_bg">
		<div id="bg_gradient"></div>
	</div>
	<div id="member_wrap">
		<div id="member_content">
			<div id="tit_box" class="NanumGothic"><span>WelMoney Admin Page.</span></div>
			<div id="member_box">			
				<div id="box_tit" class="NanumGothic">Member</div>
				<div id="input_box">
					<div class="field_area">
						<div class="selBox NanumGothic">
							<select id="searchKey" class="lt">
									<option value="empName">사용자명</option>
									<option value="userId">사용자ID</option>
									<option value="eMail">이메일</option>
									<option value="phoneNum">휴대폰</option>
							</select>
						</div>
						<div class="inputBox">
							<fieldset>
								<input type="text" id="searchValue" class="word_input NanumGothic">
								<a class="btn_search btn_type Material_icons" onclick="fn_memberList(1)">search</a>
							</fieldset>
						</div>
					</div>
					<div class="table_contents">
						<div class="list">
							<table class="NanumGothic">
								<colgroup>
									<col width="5%">
									<col width="15%">
									<col width="15%">
									<col width="15%">
									<col width="15%">
									<col width="15%">
									<col width="15%">
									<col width="5%">
								</colgroup>
								<thead>
									<tr>
										<th>순번</th>
										<th>ID</th>
										<th>사용자명</th>
										<th>시작일</th>
										<th>종료일</th>
										<th>휴대폰번호</th>
										<th>이메일</th>
										<th class="border-right">수정</th>
									</tr>
								</thead>
								<tbody id="dataList">
									<tr class="databox"> 
										<td colspan="8">요청하신 자료가 존재하지 않습니다.</td>
									</tr>
								</tbody>
							</table>
						</div>
						<div id="page-selection" class="pagingnav_wrap"></div>
						<div class="btn_footGroup NanumGothic">
							<div id="btn_save" class="btn_type Material_icons"  onClick="fn_save();"><a>등록</a></div>
						</div>
					</div>
				</div> <!--  member_box -->
			</div> <!-- member_box -->
		</div> <!--  member_content -->
	</div> <!--  member_wrap -->
</body>
</html>