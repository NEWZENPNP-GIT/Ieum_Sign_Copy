
	//var rootPath = "https://ieumsign.newzenpnp.co.kr/"; // Angular 개발
	var rootPath = window.location.protocol + '//' + window.location.host + '/';
	var fileRootPath = window.location.protocol + '//' + window.location.host + ':9443' + '/';

	//var fullPath = "https://ieumsign.newzenpnp.co.kr/"; // 개발
	var fullPath = "https://ieumsign.co.kr/"; // 운영

	//var certUrl = "https://dev.mobile-ok.com/popup/common/mobile_ncp.jsp";  //개발
	//var certUrl = "http://dev.mobile-ok.com/popup/common/hscert.jsp";  //개발PC
	var certUrl = "https://www.mobile-ok.com/popup/common/hscert.jsp";   //운영

	var pgUrl = "https://npg.settlebank.co.kr";    //운영
	//var pgUrl = "https://tbnpg.settlebank.co.kr";  //개발 (mid_test)

	var mobileType = "";

	var curPage = 0;
	var totPage = 0;
	var viewPage = 10;
	var dataList;

	var date = new Date();

	var getMobile = function() {
	  var mobileKeyWords = new Array('iPad', 'iPhone', 'iPod', 'BlackBerry', 'Android', 'Windows CE', 'LG', 'MOT', 'SAMSUNG', 'SonyEricsson','Mobile','Nokia','webOS','Opera Mini','opera mobi','SonyEricsson','Windows Phone','IEMobile','POLARIS');
	  for (var word in mobileKeyWords) {
		  if (navigator.userAgent.match(mobileKeyWords[word]) != null) {
			  mobileType=mobileKeyWords[word];
			  return true;
		  }
	  }
	  return false;
	}

	function getCookie(name) {
		var nameOfCookie = name + "=";
		var x = 0;
		while (x <= document.cookie.length) {
			var y = (x + nameOfCookie.length);
			if (document.cookie.substring(x, y) === nameOfCookie) {
				var endOfCookie = document.cookie.indexOf(";", y);
				if (endOfCookie === -1) endOfCookie = document.cookie.length;
				return decodeURIComponent(document.cookie.substring(y, endOfCookie));
			}
			x = document.cookie.indexOf(" ", x) + 1;
			if (x === 0) break;
		}
		return "";
	}

	function setCookie(name, value) {
		var today = new Date();
		var argv = setCookie.arguments;
		var argc = setCookie.arguments.length;
		var expires = (2 < argc) ? argv[2] : new Date(today.getTime() + (24 * 60 * 60 * 1000)); // 기본 만료 시간: 24시간 후
		var path = (3 < argc) ? argv[3] : null;
		var domain = (4 < argc) ? argv[4] : null;
		var secure = (5 < argc) ? argv[5] : false;

		document.cookie = name + "=" + encodeURIComponent(value) + ((expires == null) ? "" : ("; expires=" + expires.toGMTString())) + ((path == null) ? "" : ("; path=" + path)) + ((domain == null) ? "" : ("; domain=" + domain)) + ((secure === true) ? "; secure" : "");
	}


	function openWin (url,name,width,height,left) {
		var left;
		if (typeof width === "undefined"  || width === 0)  { width  = screen.width - 20;   window.screenX = 0; }
		if (typeof height === "undefined" || height === 0) { height = screen.height - 100; window.screenY = 0; }

		var top = parseInt((screen.height/2) - (height/2));

		if (typeof left === "undefined" || left === 0 ) left = parseInt((screen.width / 2) - (width / 2));

		var winPopup = window.open(url, name, "width="+width+"px, height="+height+"px,left="+left+"px,top="+top+"px, location=no, menubar=no, status=no, scrollbars=no, toolbar=no, resizable=no");
		if (winPopup !== undefined) { winPopup.focus(); return winPopup; }
	}

	var getParams = function() {
		var urlString = location.search;

		var parse = function(params, pairs) {
			var pair = pairs[0];
			var parts = pair.split('=');
			var key = decodeURIComponent(parts[0]);
			var value = decodeURIComponent(parts.slice(1).join('='));

			if (typeof params[key] === "undefined") params[key] = value;
			else 									params[key] = [].concat(params[key], value);

			return pairs.length === 1 ? params : parse(params, pairs.slice(1));
		};

		return urlString.length === 0 ? {} : parse({}, urlString.substr(1).split('&'));
	};

	var getURLParameters = function(paramName) {
		var sURL = window.document.URL.toString();
		var url = new URL(sURL);
		var params = new URLSearchParams(url.search);
		return params.has(paramName) ? params.get(paramName) : null;
	};

	var isNull = function(obj) { return (!(typeof obj !== "undefined" && obj != null && obj !== "")); }

	var isSetNull = function(obj,value) { return (typeof obj !== "undefined" && obj != null && obj !== "")?obj:value; }

	var isNotNull = function(obj) { return !isNull(obj); }


	Date.prototype.format = function(f) {
		if (!this.valueOf()) return " ";

		var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
		var d = this;

		return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
			switch ($1) {
				case "yyyy": return d.getFullYear();
				case "yy": return (d.getFullYear() % 1000).zf(2);
				case "MM": return (d.getMonth() + 1).zf(2);
				case "dd": return d.getDate().zf(2);
				case "E": return weekName[d.getDay()];
				case "HH": return d.getHours().zf(2);
				case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
				case "mm": return d.getMinutes().zf(2);
				case "ss": return d.getSeconds().zf(2);
				case "a/p": return d.getHours() < 12 ? "오전" : "오후";
				default: return $1;
			}
		});
	};

	String.prototype.trim = function() { return this.replace(/(^\s*)|(\s*$)/gi, ""); }
	String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
	String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
	Number.prototype.zf = function(len){return this.toString().zf(len);};

	//date변환
	var convertDateTime = function(st) {
		if (isNull(st)) return null;

		var year = st.substr(0,4);
		var month = st.substr(4,2);
		var day = st.substr(6,2);
		var hour = st.substr(8,2);
		var minute = st.substr(10,2);
		var second = st.substr(12,2);
		var dt = new Date(year, month-1, day, hour, minute, second);
		return dt;
	}

	var convertDate = function(st) {
		if (isNull(st)) return null;
		var year = st.substr(0,4);
		var month = st.substr(4,2);
		var day = st.substr(6,2);
		var dt = new Date(year, month-1, day);
		return dt;
	}

	//번호
	var hyphenRemove = function(st) {
		st = st.replace(/-/gi, "");
		return st;
	}

	var removeSpecialChars = function(st) {
		var specialChars = /[~!#$^&*=+|:;?"<,.>']/;
		return st.split(specialChars).join("");
	}

	function AddComma(data_value) {

		var txtNumber = '' + data_value;    // 입력된 값을 문자열 변수에 저장합니다.

		var rxSplit = new RegExp('([0-9])([0-9][0-9][0-9][,.])');    // 정규식 형태 생성
		var arrNumber = txtNumber.split('.');    // 입력받은 숫자를 . 기준으로 나눔. (정수부와 소수부분으로 분리)
		arrNumber[0] += '.'; // 정수부 끝에 소수점 추가

		do {
			arrNumber[0] = arrNumber[0].replace(rxSplit, '$1,$2'); // 정수부에서 rxSplit 패턴과 일치하는 부분을 찾아 replace 처리
		} while (rxSplit.test(arrNumber[0])); // 정규식 패턴 rxSplit 가 정수부 내에 있는지 확인하고 있다면 true 반환. 루프 반복.

		if (arrNumber.length > 1) { // txtNumber를 마침표(.)로 분리한 부분이 2개 이상이라면 (즉 소수점 부분도 있다면)
			return arrNumber.join(''); // 배열을 그대로 합칩. (join 함수에 인자가 있으면 인자를 구분값으로 두고 합침)
		} else { // txtNumber 길이가 1이라면 정수부만 있다는 의미.
			return arrNumber[0].split('.')[0]; // 위에서 정수부 끝에 붙여준 마침표(.)를 그대로 제거함.
		}
	}

	function downloadDataUrlFromJavascript(dataUrl) {

		var link = document.createElement("a");
		link.target = "_blank";

		link.href = dataUrl;
		document.body.appendChild(link);
		link.click();

		document.body.removeChild(link);
		delete link;
	}

	function checkNumber(str) {
		if (str == null || str.length === 0) return false;

		var len = str.length;

		for (var i=0; i<len; i++) {
			var c = str.charAt(i);
			if (c < '0' || c > '9') return false;
		}
		return true;
	}

	function checkPassword(str){

		var pw  = str;
		var num = pw.search(/[0-9]/g);
		var eng = pw.search(/[a-z]/ig);
		var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

		if (pw.length < 5 || pw.length > 15) { alert("비밀번호는 5자리 ~ 15자리 이내로 입력해주세요."); return false; }

		if (pw.search(/₩s/) !== -1) { alert("비밀번호는 공백없이 입력해주세요."); return false; }

		if (num < 0 || eng < 0 ) { alert("영문,숫자를 혼합하여 입력해주세요."); return false; }

		if (spe>0) { alert("특수문자를 입력하실 수 없습니다."); return false; }

		return true;
	}

	//특수 문자가 있나 없나 체크
	function checkSpecial(str) {
		var regExp = new RegExp(/[\{\}\[\]\/?,;:|\)*~`!^\+<>@\#$%&\\\=\(\'\"]/gi);    // 정규식 형태 생성
		return regExp.test(str);
	}

	// 파일확장자 체크
	function checkFileExt(file, ext) {
		var fileExt = file.split(".").pop().toLowerCase();
		return fileExt === ext;
	}

	function checkDate(date) {
		var regExp = new RegExp(/^(19|20)\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$/);
		return regExp.test(date);
	}

	function checkDateNoHypen(date) {
		var regExp = new RegExp(/^(19|20)\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[0-1])$/);
		return regExp.test(date);
	}

	function checkPhoneNum(num) {
		var regExp = new RegExp(/^01([0|1|6|7|8|9]?)?([0-9]{3,4})?([0-9]{4})$/);
		return regExp.test(num);
	}

	function checkPhoneNumNoHypen(num) {
		var regExp = new RegExp(/^01([0|1|6|7|8|9]?)([0-9]{3,4})([0-9]{4})$/);
		return regExp.test(num);
	}

	function checkEmail(email) {
		var regExp = new RegExp(/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i);
		return regExp.test(email);
	}

	function cutStr(str,limit){
		var tmpStr = str;
		var byte_count = 0;
		var len = str.length;
		var dot = "";
		for (i=0; i<len; i++) {
			byte_count += chr_byte(str.charAt(i));
			if (byte_count === limit-1) {
				if (chr_byte(str.charAt(i+1)) === 2) {
					tmpStr = str.substring(0,i+1);
					dot = "...";
				} else {
					if (i+2 !== len) dot = "...";
					tmpStr = str.substring(0,i+2);
				}
				break;
			} else if (byte_count === limit) {
				if (i+1 !== len) dot = "...";
				tmpStr = str.substring(0,i+1);
				break;
			}
		}
		return tmpStr+dot;
	}

	function chr_byte(chr){
	   if (encodeURIComponent(chr).length > 4) return 2;
	   else 					   			   return 1;
	}

	function getDateCount(obj){
		var second = $(obj).attr("sec");

		$(obj).attr("timer", setInterval(function() {
			setTimeSaleTimer($(obj), second);
			second -= 1;
		}, 1000));
	}

	function setTimeSaleTimer(obj, objSec) {

		var days = parseInt( objSec / 86400 );
		var hours = parseInt( objSec / 3600 ) % 24;
		var minutes = parseInt( objSec / 60 ) % 60;
		var seconds = objSec % 60;

		if (days === 0 && hours === 0 && minutes === 0 && seconds === 0) {
			clearInterval($(obj).attr("timer"));
			return false;
		}
		if (hours === 0 && days > 0) {
			days --;
			hours = 23;
		}
		if (minutes === 0 && hours > 0) {
			hours --;
			minutes = 59;
		}
		if (seconds === 0 && minutes > 0) {
			minutes --;
			seconds = 59;
		} else seconds --;

		$(obj).find('#min').html(minutes);
		$(obj).find('#sec').html(seconds);
	}

	//재외국인 번호 체크
	function check_fgnno(fgnno) {
		var sum= 0;
		var odd= 0;
		buf = new Array(13);
		for (i=0; i<13; i++) buf[i]=parseInt(fgnno.charAt(i));
		odd = buf[7]*10 + buf[8];

		if (odd%2 !== 0) return false;
		if ((buf[11]!==6) && (buf[11]!==7) && (buf[11]!==8) && (buf[11]!==9)) return false;

		multipliers = [2,3,4,5,6,7,8,9,2,3,4,5];

		for (i=0, sum=0; i<12; i++) sum += (buf[i] *= multipliers[i]);

		sum = 11 - (sum%11);
		if (sum >= 10) sum -= 10;

		sum += 2;
		if (sum >= 10) sum -= 10;

		return sum === buf[12];

	}

	//사업자등록번호 체크
	function check_busino(vencod) {
		var sum = 0;
		var getlist =new Array(10);
		var chkvalue =new Array("1","3","7","1","3","7","1","3","5");
		for (var i=0; i<10; i++) { getlist[i] = vencod.substring(i, i+1); }
		for (var i=0; i<9; i++)  { sum += getlist[i]*chkvalue[i]; }
		sum = sum + parseInt((getlist[8]*5)/10);

		sidliy = sum % 10;
		sidchk = 0;

		if (sidliy !== 0) sidchk = 10-sidliy;
		else 			  sidchk = 0;

		return sidchk === getlist[9];
	}

	/** @param txt<br/>
	 *  @param len : 생략시 기본값 20<br/>
	 *  @param lastTxt : 생략시 기본값 "..."<br/>
	 *  @returns 결과값
	 * <br/>
	 * <br/>
	 * 특정 글자수가 넘어가면 넘어가는 글자는 자르고 마지막에 대체문자 처리<br/>
	 *  ex) 가나다라마바사 -> textLengthOverCut('가나다라마바사', '5', '...') : 가나다라마...<br/>
	 */
	function textLengthOverCut(txt, len, lastTxt) {

		// 기본값
		if (len === "" || len == null) len = 20;

		// 기본값
		if (lastTxt === "" || lastTxt == null) lastTxt = "...";

		if (txt.length > len) txt = txt.substr(0, len) + lastTxt;

		return txt;
	}

	// 모바일여부
	var isMobile = getMobile();

	/** 스크롤 관련 이벤트 **/
	window.onresize = function(){ left_menu_css(); };

	function left_menu_css(){
		var _winH;

		if (isNull(document.getElementById("main_contents"))) return;

		if (isNull($("#contents_wrap"))) return;

		if (document.getElementById("main_contents").length > 0 && $("#contents_wrap").length > 0) {
			if (document.getElementById("main_contents").clientHeight < $("#contents_wrap").eq(0)[0].clientHeight) {
				_winH=$("#contents_wrap").eq(0)[0].clientHeight+'px';
			} else {
				_winH=document.getElementById("main_contents").clientHeight+'px';
			}

			//좌측 메뉴 배경 window맞게 조정
			document.getElementById("left_nav").style.height=_winH;
		}
	}

	/* ++++++++++++++++++++  page link +++++++++++++++++++++++++ */
	function openPage(url) {
		$("#contents_wrap").eq(0).empty();
		$(window).off("resize");
		$.ajax({
			url : url,
			success : function(result) { $("#contents_wrap").eq(0).html(result); }
		});
	}

	$(".ser_menage").click(function(){
		var url = 'myPage_service_management.html';
		openPage(url);
	})

	$(".btn_contract").click(function(){
		var url = 'contract_write_step01.html';
		openPage(url);
		$(this).parent().addClass("active");
	});

	$(".company_manage").click(function(){
		var url = 'company_management.html';
		openPage(url);
		$(this).addClass("active");
	});


	function date_mask(obj) {

		var textlength = obj.value.length;

		if (textlength === 4) {
			obj.value = obj.value + "-";
		} else if (textlength === 7) {
			obj.value = obj.value + "-";
		} else if (textlength > 9) {
			//날짜 수동 입력 Validation 체크
			var chk_date = checkDate(obj);

			if (chk_date === false) return;
		}
	}

	function exportDataToCSVFile(header, keys, body) {

		var csv = ''
		csv = header.join(',');
		csv += '\n';

		$.each(body, function (index, rows) {

			if (rows) {
				var tmp = [];
				$.each(keys, function (index, key) {

					key && tmp.push(rows[key])
				})
				csv += tmp.join(',');
				csv += '\n';
			}
		})
		var pom = document.createElement('a');
		var blob = new Blob(["\ufeff" + csv], {type: 'text/csv;charset=utf-8;'});
		var url = URL.createObjectURL(blob);
		pom.href = url;
		pom.setAttribute('download', '현황파일.csv');
		pom.click();
	}

	//Home 화면으로 이동
	function goHome() {
		window.location.href = rootPath + "menu/goMainMenu.do";
	}

	//3자리 단위마다 콤마 생성
	function addCommas(x) {
		if (isNull(x)) {
			return "0";
		} else {
			if (x.toString().indexOf(",") > -1) x = parseFloat(removeCommas(x));
			else 								x = parseFloat(x);

			return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}
	}

	//3자리 단위마다 콤마 생성 양수
	function addCommasABS(x) {
		if (isNull(x)) {
			return "0";
		} else {
			if (x.toString().indexOf(",") > -1) x = parseFloat(removeCommas(x));
			else 								x = parseFloat(x);

			if (x < 0) x = x * -1;

			return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		}
	}

	function removeCommas(x) {
		if (!x || x.length == 0) return "0";
		else return x.split(",").join("");
	}

	// 처리중 메세지 호출
	function fncOnBlockUI() {
		$.blockUI({
			css: {
				border: 'none',
				padding: '15px',
				backgroundColor: '#000',
				'-webkit-border-radius': '10px',
				'-moz-border-radius': '10px',
				opacity: .6,
				color: '#fff'
			}
		});
	}

	// 오늘날짜
	function getToday() {

		var year = date.getFullYear();
		var month = date.getMonth() + 1;
		var day = date.getDate();

		if (month < 10) month = "0" + month;
		if (day < 10) day = "0" + day;

		return year + "-" + month + "-" + day;
	}

	//오늘날짜 +N일 후 날짜
	function getNextDay(day) {

		var nextDay = new Date();
		nextDay.setDate(Number(nextDay.getDate()) + Number(day));

		var year = nextDay.getFullYear();
		var month = nextDay.getMonth() + 1;
		var day = nextDay.getDate();

		if (month < 10) month = "0" + month;
		if (day < 10) day = "0" + day;

		return year + "-" + month + "-" + day;
	}

	//전화번호, 휴대폰 번호에 자동 하이픈 추가
	function phoneNumberHyphen(val) {
		if (isNull(val)) return "";
		else 			 return val.replace(/(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/, "$1-$2-$3");
	}

	//사업자번호에 자동 하이픈 추가
	function bizNumberHyphen(val) {
		if (isNull(val)) {
			return "";
		} else {
			if (val.length === 10) return val.replace(/(\d{3})(\d{2})(\d{5})/, '$1-$2-$3');
			else 				   return val;
		}
	}

	//주민등록번호에 자동 하이픈 추가
	function juminNumberHyphen(val) {
		if 		(isNull(val)) 		return "";
		else if (val.length === 13) return val.replace(/(\d{6})(\d{7})/, '$1-$2');
		else						return val;
	}

	//날짜포맷 설정
	function formatYYYYMMDD(strDate) {
		if (isNull(strDate)) return "";
		else 				 return moment(strDate, 'YYYYMMDD').format('YYYY-MM-DD');
	}

	//사업자번호 변경
	function changeBizNumber(objId) {
		$("#" + objId).val(bizNumberHyphen($("#" + objId).val()));
	}


	//전화번호 변경
	function changePhoneNumber(objId) {
		$("#" + objId).val(phoneNumberHyphen($("#" + objId).val()));
	}

	//특수문자 인코딩
	function encodingSpecialCharacter(str) {
		return str.replace(/[\!\(\)\*\+\,\/\:\;\=\?\@\[\]\$\<\>\"\']/g, function (match) {
			return '%' + match.charCodeAt(0).toString(16).toUpperCase();
		});
	}


	// 날자차이
	function dateDiff(_date1, _date2) {
		var diffDate_1 = _date1 instanceof Date ? _date1 : new Date(_date1);
		var diffDate_2 = _date2 instanceof Date ? _date2 : new Date(_date2);

		diffDate_1 = new Date(diffDate_1.getFullYear(), diffDate_1.getMonth() + 1, diffDate_1.getDate());
		diffDate_2 = new Date(diffDate_2.getFullYear(), diffDate_2.getMonth() + 1, diffDate_2.getDate());

		var diff = diffDate_2.getTime() - diffDate_1.getTime();

		diff = Math.ceil(diff / (1000 * 3600 * 24));

		return diff;
	}

	function openHelpWindow(url) {
		var helpurl = rootPath + "module/pdfjs/web/pdfviewer.html?file=/help/";
		var helpname = "전자계약서비스 도움말";
		var helppage = "document/전자계약이용가이드.pdf";

		if 		(url === "imagemanager") 		   helppage = encodeURIComponent("document/도장관리.pdf");
		else if (url === "detailwrite")			   helppage = encodeURIComponent("document/전자계약작성.pdf");
		else if (url === "detailedit") 			   helppage = encodeURIComponent("document/전자계약수정.pdf");
		else if (url === "detailreset")			   helppage = encodeURIComponent("document/전자계약수정.pdf");
		else if (url === "detailview") 			   helppage = encodeURIComponent("document/전자계약조회.pdf");
		else if (url === "sendlist") 			   helppage = encodeURIComponent("document/전자계약발송함.pdf");
		else if (url === "recvlist") 			   helppage = encodeURIComponent("document/전자계약수신함.pdf");
		else if (url === "completelist") 		   helppage = encodeURIComponent("document/전자계약완료함.pdf");
		else if (url === "contractformmanagement") helppage = encodeURIComponent("person/양식관리.pdf");
		else if (url === "contractwritestep01")    helppage = encodeURIComponent("person/전자근로계약작성.pdf");
		else if (url === "contractlist") 		   helppage = encodeURIComponent("person/전자근로계약목록.pdf");
		else if (url === "contractlistcomplete")   helppage = encodeURIComponent("person/전자근로계약완료함.pdf");
		else if (url === "contactlist") 		   helppage = encodeURIComponent("document/주소록관리.pdf");

		openWin(helpurl + helppage, helpname, 800, 600);
	}

	function lpad(val, padLength, padString) {
		while (val.length < padLength) val = padString + val;
		return val;
	}

	function rpad(val, padLength, padString) {
		while (val.length < padLength) val += padString;
		return val;
	}

	function goInvalidPage(request, error) {
		if (request.status === "401" && getCookie("systemType") === "009") {
			alert("사용자 권한이 존재하지 않습니다. 다시 접속하여 주시기 바랍니다.");
		} else  {
			alert("입력하신 정보를 다시 확인해주시기 바랍니다." + error);
		}
	}

	/**
	 * ex)
	 *  consoleLog("message");
	 *  consoleLog("message","debug");
	 *  consoleLog("message","info");
	 * 	consoleLog("message","error");
	 */
	function consoleLog(message, level){

		// Chrome 1 - 71
		var isChrome = !!window.chrome && (!!window.chrome.webstore || !!window.chrome.runtime);

		if (isChrome) {
			if 		(level === "debug") console.log("%c" + "DEBUG : " + message, "color:Green");
			else if (level === "info")  console.log("%c" + "INFO : " + message, "color:DodgerBlue");
			else if (level === "warn")  console.warn("WARN : " + message);
			else if (level === "error") console.error("ERROR : " + message);
			else						console.log(message);

		} else {
			if 		(level === "debug") console.log("DEBUG : " + message);
			else if (level === "info") 	console.info("INFO : " + message);
			else if (level === "warn") 	console.warn("WARN : " + message);
			else if (level === "error") console.error("ERROR : " + message);
			else 						console.log(message);

		}
	}

	function logD(message){
		consoleLog(message,"debug");
	}
	//console.log Info
	function logI(message){
		consoleLog(message,"info");
	}
	//console.log Warn
	function logW(message){
		consoleLog(message,"warn");
	}
	//console.log Error
	function logE(message){
		consoleLog(message,"error");
	}

	function reload() {
		$.ajax({
			url:rootPath+"logout.do",
			crossDomain : true,
			dataType:"json",
			type:"POST",
			success:function(result) {  location.href = rootPath; },
			error:function(request,status,error) { }
		});
	}
