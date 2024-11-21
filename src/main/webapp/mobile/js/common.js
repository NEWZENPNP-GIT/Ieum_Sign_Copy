
    //var rootPath = "https://ieumsign.newzenpnp.co.kr/"; // Angular 개발
    var rootPath = "/";
    var fullPath = "https://ieumsign.newzenpnp.co.kr/";
    var certUrl = "https://dev.mobile-ok.com/popup/common/mobile_ncp.jsp";  //개발
    //var certUrl = "http://dev.mobile-ok.com/popup/common/hscert.jsp";  //개발PC
    // var certUrl = "https://www.mobile-ok.com/popup/common/hscert.jsp";   //운영

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

    function setCookie (name, value) {
        var today = new Date();
        var argv = setCookie.arguments;
        var argc = setCookie.arguments.length;
        var expires = (2 < argc) ? argv[2] : new Date(today.getTime() + (24 * 60 * 60 * 1000));
        var path = (3 < argc) ? argv[3] : null;
        var domain = (4 < argc) ? argv[4] : null;
        var secure = (5 < argc) ? argv[5] : false;

        document.cookie = name + "=" + encodeURIComponent(value) + ((expires == null) ? "" : ("; expires=" + expires.toGMTString())) + ((path == null) ? "" : ("; path=" + path)) + ((domain == null) ? "" : ("; domain=" + domain)) + ((secure === true) ? "; secure" : "");
    }

    function openWin (url,name,width,height) {
        if (typeof width === "undefined"  || width === 0)  { width  = screen.width - 20;   window.screenX = 0; }
        if (typeof height === "undefined" || height === 0) { height = screen.height - 100; window.screenY = 0; }
        window.open(url, name, "width="+width+", height="+height+", location=no, menubar=no, status=no, scrollbars=no, toolbar=no");
    }

    var getParams = function() {
        var urlString = location.search;

        var parse = function(params, pairs) {
            var pair = pairs[0];
            var parts = pair.split('=');
            var key = decodeURIComponent(parts[0]);
            var value = decodeURIComponent(parts.slice(1).join('='));

            if (typeof params[key] === "undefined") params[key] = value;
            else                                    params[key] = [].concat(params[key], value);

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

    //특수 문자가 있나 없나 체크
    function checkSpecial(str) {
        var regExp = new RegExp(/[\{\}\[\]\/?,;:|\)*~`!^\+<>@\#$%&\\\=\(\'\"]/gi);    // 정규식 형태 생성
        return regExp.test(str);
    }

    // 파일확장자 체크
    function checkFileExt(file, ext) {
        var fileExt = file.slice(file.indexOf(".") + 1).toLowerCase();
        return fileExt === ext;
    }

    function cutStr(str,limit) {
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
                if(i+1 !== len) dot = "...";
                tmpStr = str.substring(0,i+1);
                break;
            }
        }
        return tmpStr+dot;
    }

    function chr_byte(chr){
        if (encodeURIComponent(chr).length > 4) return 2;
        else 					   			    return 1;
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
