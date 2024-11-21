
	<!--****************************************************************************************************************
	* 상기 프로그램에 대한 저작권을 포함한 직접재산권은 NewZen P&P에 있으며 NewZen P&P가 명시적으로 허용하지 않은
	* 사용, 복사, 변경, 제3자에 의한 공개 배포는 엄격히 금지되며 NewZen P&P의 지적재산권 침해에 해당합니다.
	********************************************************************************************************************
	--------------------------------------------------------------------------------------------------------------------
	* 회사       : 뉴젠피앤피
	* 화면명     : 메인 페이지
	* 화면설명   :
	* 작성자     :
	* 생성일     :
	--------------------------------------------------------------------------------------------------------------------
	* 수정자     : 이정훈
	* 수정일     : 2024.05.21
	* 수정내용   :
	------------------------------------------------------------------------------------------------------------------->

	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%@ page language="java" contentType="text/html; charset= UTF-8" pageEncoding="UTF-8"%>
	<!DOCTYPE html>
	<html>
	<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, user-scalable=no">

	<title>인적자원관리 전문IT기업 - 뉴젠P&amp;P</title>
	<link rel="stylesheet" type="text/css" href="/css/font_pc.css">
	<link rel="stylesheet" type="text/css" href="/css/main.css">
	<link rel="stylesheet" type="text/css" href="/css/main_calender.css">
	<link rel="stylesheet" type="text/css" href="/css/bootstrap_candy.css">

	<link rel="stylesheet" type="text/css" href="/css/main_calendar/tui-time-picker.css">
	<link rel="stylesheet" type="text/css" href="/css/main_calendar/tui-date-picker.css">
	<link rel="stylesheet" type="text/css" href="/css/main_calendar/tui-calendar.css" />
	<link rel="stylesheet" type="text/css" href="/css/main_calendar/default.css">
	<link rel="stylesheet" type="text/css" href="/css/main_calendar/icons.css"/>

	<script type='text/javascript' src="/js/jquery.min.js"></script>
	<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
	<script type='text/javascript' src="/js/common.js"></script>
	<script type='text/javascript' src="/js/ui.biz.core.js"></script>
	<script type='text/javascript' src="/js/jquery.bpopup.js"></script> <!-- layer popup library -->
	<script type="text/javascript" src="/js/loader.js"></script> <!--  Chart library -->
	<script>

		var contractMenu    = ["수신자관리", "전자문서", "게시판", "시스템관리"];
		var elecDocMenu     = ["수신자관리", "전자문서", "게시판", "시스템관리", "서비스소개"];
		var payStubMenu     = ["수신자관리", "임금명세관리", "연차관리", "근태관리", "게시판", "시스템관리", "서비스소개"];
		var companyLogoImg  = "";
		var defaultCssId    = "";
		var defaultMenuCode = "";

		//***************					윈도우 로딩					***************//


		$(document).ready(function() {
			fn_bizLogo();
			fn_pwdUpdDate();
			if (getCookie("loginType") !== "1") 	  $("#showPoint").removeClass("hidden")
			if (getCookie("useContractMenu") === "Y") $("#showPoint").addClass("hidden")
			fn_isDupleEmpNo();
		});


		//***************					로고 정보 조회					***************//


		function fn_bizLogo() {
			var formData = new FormData();
			formData.append('bizId', getCookie("bizId"));
			formData.append('startPage', 0);
			formData.append('endPage', 10);
			var url = rootPath + 'biz/getBizLogo.do';
			$.ajax({
				url:url,
				crossDomain : true,
				dataType:"json",
				async:false,
				type:"POST",
				processData: false,
				contentType: false,
				data:formData,
				success:function(res) {
					if (res.success) {
						if (isNotNull(res['companyLogo'])) companyLogoImg = res['companyLogo'];
						fn_menuTop();
					} else { alert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.'); }
				},
				error:function(request,status,error) {
					if (request.status === "401") { alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다."); location.href = rootPath; }
					else						  { alert("입력하신 정보를 다시 확인해주시기 바랍니다." + error ); }
				}
			});
		}


		//***************					메인 메뉴 리스트 조회					***************//


		function fn_menuTop() {
			var url = rootPath + "menu/getMenuMainList.do";
			$.ajax({
				url: url,
				corssDomain: true,
				dataType: "json",
				type: "GET",
				data: { menuLvl:"0" },
				success: function(result) {
					var htmlData = "";
					htmlData += '<li class="gnb_logo" onclick="goHome();" style="cursor:pointer;" id="top_menu_logo"></li>';
					if (result.success === true) {
						// 메뉴레벨 0
						$.each(result.data, function(i, row) {
							var shouldIncludeMenu = false;

							if (getCookie("useContractMenu") === "Y" || (getCookie("useElecDoc") === "Y" && getCookie("usePayStub") === "Y")) {
								shouldIncludeMenu = true;
							} else {
								if (getCookie("useElecDoc") === "Y" && $.inArray(row.menuName, elecDocMenu) !== -1) shouldIncludeMenu = true;
								if (getCookie("usePayStub") === "Y" && $.inArray(row.menuName, payStubMenu) !== -1) shouldIncludeMenu = true;
							}
							if (shouldIncludeMenu || $.inArray(row.menuName, contractMenu) !== -1) {
								htmlData += '<li class="' + row.cssId + '"><div class="menu btn_type ' + row.cssId + '" data-url="' + row.menuUrl + '" onclick="clickEvent(this,\'' + row.menuCode + '\',\'' + row.openType + '\',\'TOP\' )"><div class="tab"></div><span class="icon"></span>' + row.menuName + '</div></li>';
							}

							if (i === 0) {
								defaultCssId    = row.cssId;
								defaultMenuCode = row.menuCode;
							}
						});
					} else { alert('메뉴권한이 부여되지 않았습니다.'); }

					htmlData += '<li class="gnb_board none"><div class="menu btn_type btn_board middle"><div class="tab"></div><div class="icon"></div>문의 게시판</div></li>';
					htmlData += '<li class="gnb_serIntrodu none"><div class="menu btn_type btn_serIntrodu middle"><div class="tab"></div><span class="icon"></span>서비스 소개</div></li>';
					htmlData += '<li class="gnb_myArea">';
					htmlData += '	<div class="btn_inner my" onclick="fn_myPage();">MY</div>';
					htmlData += '	<div class="btn_inner logout" onclick="fn_logout();">로그아웃</div>';
					htmlData += '</li>';
					$('#top_gnb').html(htmlData);

					if (isNotNull(companyLogoImg)) {
						var imageFile    = new Image();
						imageFile.onload = function() {
							$("#top_menu_logo").css({"background":"url("+rootPath+"images/sign/"+companyLogoImg+")"});
							$("#top_menu_logo").css("background-repeat","no-repeat");
							$("#top_menu_logo").css("background-size","72%");
							$("#top_menu_logo").css("background-position","50% 50%");
							$("#top_menu_logo").css("justify-conten","center");
							$("#top_menu_logo").css("align-items","center");
						}
						imageFile.onerror = function() { logI("# 로고 이미지 파일이 없습니다. [ " +rootPath+"images/sign/"+companyLogoImg+ " ] "); }
						imageFile.src = rootPath+"images/sign/"+companyLogoImg;
					}
					getNoticeList();
				},
				error: function(request,status, error) {
					if (request.status === "401") { alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다."); location.href = rootPath; }
					else						  { alert("입력하신 정보를 다시 확인해주시기 바랍니다." + error ); }
				}
			});
		}


		function fn_menuLeft(_cssId, _menuCode) {
			var url = rootPath + "menu/getMenuMainList.do";

			$.ajax({
				url: url,
				corssDomain: true,
				dataType: "json",
				type: "GET",
				success: function(result) {
					if (result.success === true) {
						var htmlData = "";
						var _menuLv1 = _.filter(result.data, {"menuLvl":"1","pmenuCode":_menuCode});
						var _menuLv2 = _.filter(result.data, {"menuLvl":"2"});

						$.each(_menuLv1, function(i, row) {
							htmlData += '<li>';
							if (row.childCnt !== 0) {
								htmlData += '	<input id="group-' + (i+1) +'" type="checkbox" class="hidden" />';
								htmlData += '	<label for="group-' + (i+1) + '" class="btn_type" data-url="'+row.menuUrl+'" onclick="clickEvent(this,'+row.menuCode +')">';
								htmlData += '		<div class="fa fa-angle-right1 NanumGothic"></div>';
								htmlData += '		<span>' + row.menuName + '</span>';
								htmlData += '	</label>';
								htmlData += '	<ul class="group-list">';
							} else {
								htmlData += '	<label for="group-' + (i+1) + '">';
								htmlData += '		<a class="btn_type" data-url="'+row.menuUrl+'" onclick="clickEvent(this,'+row.menuCode +')">' + row.menuName + '</a>';
								htmlData += '	</label>';
							}

							$.each(_menuLv2, function(j, subRow) {
								if (row.menuCode === subRow.pmenuCode) htmlData += '<li><a data-url="'+subRow.menuUrl+'" onclick="clickEvent(this,'+subRow.menuCode +')">' + subRow.menuName + '</a></li>';
							});
							if (row.childCnt !== 0) htmlData += '	</ul>';
							htmlData += '</li>';
						});
						$('#nav_list').html(htmlData);
					}
					$("#top_gnb > li").removeClass("active");
					$("."+_cssId).addClass("active");
				},
				error: function(request,status, error) {
					if (request.status === "401") { alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다."); location.href = rootPath; }
					else 						  { alert("입력하신 정보를 다시 확인해주시기 바랍니다." + error ); }
				}
			});
		}

		function fn_menuCode(menuCode) {

			var url = rootPath + "menu/goMenu.do";
			$.ajax({
				url: url,
				corssDomain: true,
				dataType: "json",
				data:{ menuCode:menuCode },
				type: "GET",
				success: function(result) {
					if (result.success === true) {
						var _cssId    = result.data.cssId;
						var _menuCode = result.data.menuCode;

						if (isNotNull(_cssId)) {
							$("#top_gnb > li").removeClass("active");
							$("."+_cssId).addClass("active");
							fn_menuLeft(_cssId, _menuCode);
						}

						if (isNotNull(result.data.menuUrl)) {
							if (result.data.openType === '1') openWin(result['data']['menuUrl'], result['data']['menuCode'], result['data']['popupWidth'], result['data']['popupHeight']);
							else 							  fn_goMenuPage(_cssId, result.data.menuUrl);
						}
					} else { alert('요청하신 자료가 존재하지 않습니다.'); }
				},
				error: function(request,status, error) {
					if (request.status === "401") { alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다."); location.href = rootPath; }
					else 						  { alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error ); reload();}
				}
			});
		}

		function fn_goMenuPage(_cssId, url) {
			$("#contents_wrap").empty();
			$(window).off("resize");
			$.ajax({
				url : url,
				success : function(result) 			   { $("#contents_wrap").html(result); },
				error: function(request,status, error) { alert("다시 확인해주시기 바랍니다."+error); }
			});
		}

		function fn_myPage() {
			$("#contents_wrap").empty();
			$(window).off("resize");
			$.ajax({
				url : "/html/mypage/myPage_memberInfo.html",
				success : function(result)			   { $("#contents_wrap").html(result); },
				error: function(request,status, error) { alert("다시 확인해주시기 바랍니다."+error); }
			});
		}

		// 공지사항 조회
		function getNoticeList() {
			var formData = new FormData();
			formData.append('bbsId', '180706135108002');
			formData.append('startPage', '0');
			formData.append('endPage', '8');

			fncOnBlockUI();

			var url = rootPath + 'bbs/getBbsContentsList.do';

			$.ajax({
				url:url,
				crossDomain : true,
				dataType:"json",
				type:"POST",
				async:false,
				processData: false,
				contentType: false,
				data: formData,
				success:function(res) {
					if (res.success) {
						var tmpList = [];
						$.each(res['data'], function(i, v) {
							v.isNew = moment(v.insDate, 'YYYYMMDDHHmmss').add(7, 'days').isSameOrAfter(moment());
							v.contents = $('<div />').html(v.contents).text();
							tmpList.push(v);
						});
						noticeList = tmpList;

						var noticeListHtml = "<ul>";
						for (var idx=0; idx<noticeList.length; idx++) {
							var row = noticeList[idx];
							noticeListHtml += "<li>";
							noticeListHtml += "<div class='board_txt' tabindex='0' onclick='fncNoticeView("+row.bbsNo+")'>";
							noticeListHtml += "<span>"+row.subject+"</span></div>";
							noticeListHtml += "<div class='board_date' tabindex='0'>";
							noticeListHtml += "<span>"+moment(row.insDate, 'YYYYMMDD').format("MMDD")+"</span></div>";
							noticeListHtml += "</li>";
						}
						noticeListHtml += '</ul>';
						$("#noticeListUl").html(noticeListHtml);
						getFaqList();
					} else { $.unblockUI(); }
				},
				error:function(request,status,error) {
					$.unblockUI();
					if (request.status === "401") { alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다."); location.href = rootPath; }
					else 						  { alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error ); }
				}
			});
		}

		// faq 조회
		function getFaqList() {
			var formData = new FormData();
			formData.append('bbsId', '180709094128003');
			formData.append('startPage', '0');
			formData.append('endPage', '8');
			var url = rootPath + 'bbs/getBbsContentsList.do';

			$.ajax({
				url:url,
				crossDomain : true,
				dataType:"json",
				type:"POST",
				async:false,
				processData: false,
				contentType: false,
				data: formData,
				success:function(res) {
					if (res.success) {
						var tmpList = [];
						$.each(res['data'], function(i, v) {
							v.isNew    = moment(v.insDate, 'YYYYMMDDHHmmss').add(7, 'days').isSameOrAfter(moment());
							v.contents = $('<div />').html(v.contents).text();
							tmpList.push(v);
						});
						faqList = tmpList;

						var faqListHtml = "<ul>";
						for (var idx=0; idx<faqList.length; idx++) {
							var row = faqList[idx];
							faqListHtml += "<li>";
							faqListHtml += "<div class='board_txt' tabindex='0' onclick='fncFaqList(\""+row.workCode+"\")'>";
							faqListHtml += "<span>"+row.subject+"</span></div>";
							faqListHtml += "<div class='board_date' tabindex='0'>";
							faqListHtml += "<span>"+moment(row.insDate, 'YYYYMMDD').format("MMDD")+"</span></div>";
							faqListHtml += "</li>";
						}
						faqListHtml += '</tbody>';
						faqListHtml += '</table>';
						$("#faqListUl").html(faqListHtml);
						getMainNoticeList();
						$.unblockUI();

					} else { $.unblockUI(); }
				},
				error:function(request,status,error) {
					$.unblockUI();
					if (request.status === "401") { alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다."); location.href = rootPath; }
					else 						  { alert("입력하신 정보를 다시 확인해주시기 바랍니다." + error ); }
				}
			});
		}


		// 메인 공지사항 조회
		function getMainNoticeList() {

			var formData = new FormData();
			formData.append('bbsId', '202005130548001');
			formData.append('startPage', '0');
			formData.append('endPage', '9999');
			formData.append('expireDate', new Date().format("yyyyMMdd"));

			var url = rootPath + 'bbs/getMainBbsContentsList.do';

			$.ajax({
				url:url,
				crossDomain : true,
				dataType:"json",
				type:"POST",
				async:false,
				processData: false,
				contentType: false,
				data: formData,
				success:function(res) {
					if (res.success) {
						var left = 0;
						$.each(res['fileList'], function(i, v) {
							if (getCookie("todayNotOpen_"+i) !== "Y") {
								if (i > 0) {
									left = parseInt((screen.width / 2) - (475 / 2)) + (475 * i);
									openWin("/html/popup/popup_emergenWorking_notice.html?filePath=/temp/file/"+v.filePath+v.fileName+"&index="+i, "공지사항_"+i, 475, 594, left)
								} else {
									openWin("/html/popup/popup_emergenWorking_notice.html?filePath=/temp/file/" + v.filePath + v.fileName + "&index=" + i, "공지사항_" + i, 475, 594)
								}
							}
						});
						fn_initUiBizCore();
					}
				},
				error:function(request,status,error) {
					if (request.status === "401") { alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다."); location.href = rootPath; }
					else 						  { alert("입력하신 정보를 다시 확인해주시기 바랍니다." + error ); }
				}
			});
		}

		//공지사항 리스트 이동
		function fncNoticeList() {
			$("#contents_wrap").empty();
			$(window).off("resize");

			$.ajax({
				url: rootPath + "html/board/board_notice_list.html",
				success: function(result) 				{ $("#contents_wrap").html(result); },
				error: function(request, status, error) { alert("다시 확인해주시기 바랍니다."+error); }
			});
		}

		//공지사항 상세보기
		function fncNoticeView(bbsNo) {

			$("#contents_wrap").empty();
			$(window).off("resize");

			$.ajax({
				url: rootPath + "html/board/board_notice_view.html",
				success: function(result) 				{ setCookie("paramBbsNo", bbsNo); $("#contents_wrap").html(result); },
				error: function(request, status, error) { alert("다시 확인해주시기 바랍니다." + error); }
			});
		}

		//faq 리스트이동
		function fncFaqList(workCode) {
			$("#contents_wrap").empty();
			$(window).off("resize");

			$.ajax({
				url: rootPath + "html/board/board_faq_list.html",
				success: function(result) {
					if (isNotNull(workCode)) setCookie("paramWorkCode", workCode);
					$("#contents_wrap").html(result);
				},
				error: function(request, status, error) { alert("다시 확인해주시기 바랍니다."+error); }
			});
		}

		// 비밀번호 변경일자 조회 및 팝업 호출(21.10.06)
		function fn_pwdUpdDate() {

			var formData = new FormData();
			formData.append('userId', getCookie("loginId")); // user 테이블의 userId = emp 테이블의 loginId
			var url = rootPath + 'user/getPwdUpdDate.do';
			$.ajax({
				url:url,
				crossDomain : true,
				dataType:"json",
				type:"POST",
				async:false,
				processData: false,
				contentType: false,
				data: formData,
				success:function(result) {
					if (result.success) {
						if (result.total > 0) {
							var pwdUpdDate	= (''+result.data.pwdUpdDate).substr(0,8);
							var nowDate 	= new Date().format("yyyyMMdd");
							var intervalDay = betweenDay(pwdUpdDate,nowDate);

							if (intervalDay >= 90) {
								var askCheck = getCookie("PwdChangeAskExpires");
								if (!askCheck) openWin("/html/popup/popup_pwd_change_notice.html", "비밀번호 변경안내", 590, 450);
							}
						}
					}
				},
				error:function(request,status,error) {
					if (request.status === "401") { alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다."); location.href = rootPath; }
					else 						  { alert("입력하신 정보를 다시 확인해주시기 바랍니다." + error ); }
				}
			});
		}

		// 사번 중복 여부 체크
		function fn_isDupleEmpNo() {

			var formData = new FormData();
			formData.append('bizId', getCookie("bizId"));
			var url 	 = rootPath + 'emp/getDupleEmpNo.do';

			$.ajax({
				url:url,
				crossDomain : true,
				dataType:"json",
				type:"POST",
				async:false,
				processData: false,
				contentType: false,
				data: formData,
				success:function(result) {
					if (result.success) {
						if (result.isDupleCnt > 0) {
							var empNoList = [];

							for (var i=0; i<Math.min(result.isDupleCnt,5); i++) empNoList.push(result.empNoList[i].empNo);

							var message = "입력된 사원 중에 중복 사번이 존재합니다.\r\n[ " + empNoList.join(", ");

							if (result.isDupleCnt > 5) message += " 외 " + result.isDupleCnt - 5 + " 명";

							message += " ]\r\n인사관리에서 확인 하시길 바랍니다.";
							alert(message);
						}
					}
				},
				error:function(request,status,error) {
					if (request.status === "401") { alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다."); location.href = rootPath; }
					else 						  { alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error ); }
				}
			});
		}

		function betweenDay(firstDate, secondDate) {
			var firstDateObj  = new Date(firstDate.substring(0, 4), firstDate.substring(4, 6) - 1, firstDate.substring(6, 8));
			var secondDateObj = new Date(secondDate.substring(0, 4), secondDate.substring(4, 6) - 1, secondDate.substring(6, 8));
			var betweenTime   = Math.abs(secondDateObj.getTime() - firstDateObj.getTime());
			return Math.floor(betweenTime / (1000 * 60 * 60 * 24));
		}

		function fn_loadHtml(url) {
			$("#contents_wrap").empty();
			$(window).off("resize");

			$.ajax({
				url : url,
				async : true,
				success : function(result) 			   { $("#contents_wrap").html(result); },
				error: function(request,status, error) { alert("다시 확인해주시기 바랍니다."+error); }
			});
		}
	</script>
	</head>

	<body>
		<header id="header">
			<nav id="navbar">
				<ul id="top_gnb" class="NanumGothic"></ul>
			</nav>
		</header>
		<div id="main_wrap">
			<aside id="main_side" class="">
				<div class="left_nav" id="left_nav">
					<div class="box my_info">
						<div class="user_info">
							<div class="user_pic">
								<img src="/css/image/left_menu/userinfo_pic_default.jpg">
							</div>
							<div class="user_txt">
								<span class="company NanumGothic" id="bizName"></span>
								<span class="id NanumGothic" id="loginName"></span>
							</div>
						</div>
					</div>
					<div class="box point_area hidden" id="showPoint">
						<div class="tit">
							<span class="NanumGothic">잔여 포인트</span>
						</div>
						<div>
							<span class="point" id="curPoint"></span>
							<span class="point_dec NanumGothic" style="font-weight: bold;">P</span>
						</div>
						<div class="btn_charge" id="pointCharge">
							<a class="btn_inner NanumGothic" onclick="fn_pointAdd()">포인트 충전</a>
						</div>
					</div>
					<div class="left_menu">
						<nav class="nav" role="navigation">
							<ul class="nav_list NanumGothic" id ="nav_list"></ul>
						</nav>
					</div>
				</div>
			</aside>

			<section id="main_contents" class="">
				<!-- 계약서양식폼 -->
				<input type="hidden" id="contractFileId">
				<!-- 생성된 계약서 갯수 -->
				<input type="hidden" id="createPdfCnt">
				<div id="contents_wrap">
					<div id="content_main">
						<div class="tit NanumGothic"><p>Calender</p></div>
						<div class="calender_view">
							<div id="lnb">
								<div class="lnb-new-schedule">
									<button id="btn-new-schedule" type="button" class="btn btn-default btn-block lnb-new-schedule-btn" data-toggle="modal">일정 등록</button>
								</div>
								<div id="lnb-calendars" class="lnb-calendars">
									<div>
										<div class="lnb-calendars-item">
											<label>
												<input class="tui-full-calendar-checkbox-square" type="checkbox" value="all" checked>
												<span></span>
												<strong>전체 보기</strong>
											</label>
										</div>
									</div>
									<div id="calendarList" class="lnb-calendars-d1"></div>
								</div>
							</div>
							<div id="right">
								<div id="menu">
									<div class="dropdown">
										<button id="dropdownMenu-calendarType" class="btn btn-default btn-sm dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
											<i id="calendarTypeIcon" class="calendar-icon ic_view_month" style="margin-right: 4px;"></i>
											<span id="calendarTypeName">보기 구분</span>&nbsp;
											<i class="calendar-icon tui-full-calendar-dropdown-arrow"></i>
										</button>
										<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu-calendarType">
											<li role="presentation">
												<a class="dropdown-menu-title" role="menuitem" data-action="toggle-daily">
													<i class="calendar-icon ic_view_day"></i>일간보기
												</a>
											</li>
											<li role="presentation">
												<a class="dropdown-menu-title" role="menuitem" data-action="toggle-weekly">
													<i class="calendar-icon ic_view_week"></i>주간보기
												</a>
											</li>
											<li role="presentation">
												<a class="dropdown-menu-title" role="menuitem" data-action="toggle-monthly">
													<i class="calendar-icon ic_view_month"></i>월간보기
												</a>
											</li>
											<li role="presentation">
												<a class="dropdown-menu-title" role="menuitem" data-action="toggle-weeks2">
													<i class="calendar-icon ic_view_week"></i>2주보기
												</a>
											</li>
											<li role="presentation">
												<a class="dropdown-menu-title" role="menuitem" data-action="toggle-weeks3">
													<i class="calendar-icon ic_view_week"></i>3주보기
												</a>
											</li>
											<li role="presentation" class="dropdown-divider"></li>
											<li role="presentation">
												<a role="menuitem" data-action="toggle-workweek">
													<input type="checkbox" class="tui-full-calendar-checkbox-square" value="toggle-workweek" checked>
													<span class="checkbox-title"></span>Show weekends
												</a>
											</li>
											<li role="presentation">
												<a role="menuitem" data-action="toggle-start-day-1">
													<input type="checkbox" class="tui-full-calendar-checkbox-square" value="toggle-start-day-1">
													<span class="checkbox-title"></span>Start Week on Monday
												</a>
											</li>
											<li role="presentation">
												<a role="menuitem" data-action="toggle-narrow-weekend">
													<input type="checkbox" class="tui-full-calendar-checkbox-square" value="toggle-narrow-weekend">
													<span class="checkbox-title"></span>Narrower than weekdays
												</a>
											</li>
										</ul>
									</div>
									<div id="menu-navi">
										<button type="button" class="btn btn-default btn-sm move-today" data-action="move-today">오늘</button>
										<button type="button" class="btn btn-default btn-sm move-day" data-action="move-prev">
											<i class="calendar-icon ic-arrow-line-left" data-action="move-prev"></i>
										</button>
										<button type="button" class="btn btn-default btn-sm move-day" data-action="move-next">
											<i class="calendar-icon ic-arrow-line-right" data-action="move-next"></i>
										</button>
									</div>
									<span id="renderRange" class="render-range"></span>
								</div>
								<div id="calendar"></div>
							</div>
						</div>
					</div>
					<div id="content_footer">
						<div class="board_panel">
							<div class="notice">
								<div class="tit NanumGothic">
									<p>Notice</p>
									<a class="btn_type btn_more" onclick="fncNoticeList()">더보기</a>
								</div>
								<div class="board_view bg_whiteBoard" id="noticeListUl">
								</div>
							</div>
							<div class="faq">
								<div class="tit NanumGothic">
									<p>FAQ</p>
									<a class="btn_type btn_more" onclick="fncFaqList('')">더보기</a>
								</div>
								<div class="board_view bg_whiteBoard" id="faqListUl">
								</div>
							</div>
						</div>
						<div class="chart_wrap">
							<div class="tit NanumGothic">
								<p>Current status of use</p>
							</div>
							<div id="chart">
								<div id="barchart" class="bg_whiteBoard"></div>
							</div>
							<div id="chartlabel">
								<span class="label grap_Greasy">2017</span><span class="label grap_blue">2018</span>
							</div>
						</div>
					</div>
					<!-- content_footer -->
				</div>
			</section>
		</div><!-- main_wrap -->
		<div id="section_footer">
			<div class="left_blank"></div>
			<ul>
				<li class="foot_logo"></li>
				<li class="foot_email txt">대표 이메일 : help@newzenpnp.com</li>
				<li class="foot_tel txt">문의 : 02-6953-6750 (월~금, 오전 9시~ 11시 30분, 오후1시~6시)</li>
				<li class="foot_site">
					<select>
						<option value=""> 관련 사이트</option>
					</select>
				</li>
			</ul>
		</div>
	<!--  포인트 layer 팝업 창 -->
	<div id="bpopup_layer"></div>

	<script  type="text/javascript">
		/** 스크롤 관련 이벤트 **/
		window.onresize = function() { left_menu_css(); };

		function left_menu_css() {
			var _winH;

			if (document.getElementById("main_contents").clientHeight < document.getElementById("contents_wrap").clientHeight) _winH=document.getElementById("contents_wrap").clientHeight+'px';
			else 																											   _winH=document.getElementById("main_contents").clientHeight+'px';

			//좌측 메뉴 배경 window맞게 조정
			document.getElementById("left_nav").style.height=_winH;
		}
		document.addEventListener("DOMContentLoaded", function() { left_menu_css(); });

		document.getElementById("main_wrap").onscroll = function() {
			if (document.getElementById("left_nav").clientHeight !== document.getElementById("contents_wrap").clientHeight) left_menu_css();
		};
		/** --- end 스크롤 관련 이벤트 **/

		function clickEvent(obj,menuCode) {

			var url		= $(obj).attr('data-url');
			var tagName = $(obj).prop('tagName');

			//메뉴 active 비활성 예외 처리 (사용자가이드, 서비스소개)
			if (!$(obj).text().match('사용자', '가이드', '서비스소개')) {
				//펼침 목록 아이콘 클릭시
				if ($(obj).hasClass("fa-angle-right1")) return;

				if (tagName === "A") { //하위목록이 없는 버튼 클릭
					$(".nav_list > li").removeClass('active');
					$(".group-list > li > a").removeClass('active');

					var p_tagName=$(obj).parent().parent().prop('tagName');

					if (p_tagName === "UL") { // 하위목록의 lv2 메뉴 클릭
						$(obj).addClass("active");
					} else { //하위목록 없는 메뉴 펼침 목록 접기
						$(".nav_list").find('input[type="checkbox"]').attr("checked", false);
						$(obj).parent().parent().addClass("active");
					}
				} else {
					//LV1 하위목록 있고 url 없을때
					if (url !== "" ) {
						$(".nav_list > li").removeClass('active');
						$(".group-list > li > a").removeClass('active');
						$(".nav_list").find('input[type="checkbox"]').attr("checked", false);
						$(obj).parent().addClass("active");
					}
				}
			}
			// URL존재시 페이지 호출
			if (url.length > 0) fn_menuCode(menuCode);
		}

		/* ++++++++++++++++++++  page link +++++++++++++++++++++++++ */
		//포인트 충전
		$(".btn_charge").find(".btn_inner").click(function() {
			var targetX = ($(document).width()-862)/2;
			var targetY = ($(document).height()-586)/2;
			$('#bpopup_layer').bPopup({
				position: [targetX, targetY],
				opacity: 0.6,
				transitionClose  : true,
				follow: [false, false],
				contentContainer:'#bpopup_layer',
				loadUrl: 'myPage_pointCharging.html',
				onClose: function() { $("#bpopup_layer").empty(); }
			});
		});

		//mypage 링크
		$(".btn_inner.my").click(function() {
			$("#contents_wrap").empty();
			$(window).off("resize");
			$.ajax({
				url : "myPage.html",
				success : function(result) { $("#contents_wrap").html(result); }
			});
			$('script[src*="main_chart.js"]').remove();
		});

		$(".ser_menage").click(function() {
			$("#contents_wrap").empty();
			$(window).off("resize");
			$.ajax({
				url : "myPage_service_management.html",
				success : function(result) { $("#contents_wrap").html(result); }
			});
		})

		//게시판 (공지사항) 링크
		$(".board").click(function() {
			$("#contents_wrap").empty();
			$(window).off("resize");
			$.ajax({
				url : "board_notice.html",
				success : function(result) { $("#contents_wrap").html(result); }
			});
		});

		//문의게시판 링크
		$(".board_que").click(function() {
			$("#contents_wrap").empty();
			$(window).off("resize");
			$.ajax({
				url : "board_question.html",
				success : function(result) { $("#contents_wrap").html(result); }
			});
		});

		//문의게시판 링크
		$(".board_que_admin").click(function() {
			$("#contents_wrap").empty();
			$(window).off("resize");
			$.ajax({
				url : "board_question_admin.html",
				success : function(result) { $("#contents_wrap").html(result); }
			});
		});

		$(".btn_contract").click(function() {
			$(this).parent().addClass("active");
			$("#contents_wrap").empty();
			$(window).off("resize");
			$.ajax({
				url : "contract_write_step01.html",
				success : function(result) { $("#contents_wrap").html(result); }
			});
		});

		$(".company_manage").click(function() {
			$(this).addClass("active");
			$("#contents_wrap").empty();
			$(window).off("resize");
			$.ajax({
				url : "company_management.html",
				success : function(result) { $("#contents_wrap").html(result); }
			});
		})

	</script>

	<!-- calendar 관련 js plug-in -->
	<script type="text/javascript" src="/js/main_calendar/bootstrap.min.js"></script>
	<script type="text/javascript" src="/js/main_calendar/tui-code-snippet.min.js"></script>
	<script type="text/javascript" src="/js/main_calendar/tui-time-picker.min.js"></script>
	<script type="text/javascript" src="/js/main_calendar/tui-date-picker.min.js"></script>
	<script type="text/javascript" src="/js/main_calendar/moment.min.js"></script>
	<script type="text/javascript" src="/js/main_calendar/chance.min.js"></script>
	<script type="text/javascript" src="/js/main_calendar/tui-calendar.js"></script>
	<script type="text/javascript" src="/js/main_calendar/data/calendars.js"></script>
	<script type="text/javascript" src="/js/main_calendar/data/schedules.js"></script>
	<script type="text/javascript" src="/js/main_calendar/theme/dooray.js"></script>
	<script type="text/javascript" src="/js/main_calendar/default.js"></script>

	<!-- *********** Current status of use Chart ***********  -->
	<script type='text/javascript' src="/js/main_chart.js"></script>

	<!-- 데이터 추출을 위한 plug-in -->
	<script type='text/javascript' src="/js/underscore-min.js"></script>
	</body>
	</html>