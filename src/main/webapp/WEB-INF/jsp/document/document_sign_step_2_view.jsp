
    <!DOCTYPE html>
    <html>
    <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />

    <meta name="viewport" content="user-scalable=yes, width=device-width, initial-scale=1, maximum-scale=10">
    <meta name="apple-mobile-web-app-capable" content="yes" >
    <meta name="HandheldFriendly" content="true">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Pragma" content="no-cache">

    <link rel="stylesheet" type="text/css" href="/css/font.css">
    <link rel="stylesheet" type="text/css" href="/css/material-icons.min.css">
    <link rel="stylesheet" type="text/css" href="/css/electornic_contract.css">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.4.5.2.min.css">
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>

    <!-- 1. jQuery UI -->
    <script type="text/javascript" src="/js/jquery.min.js"></script>
    <script type="text/javascript" src="/js/common.js"></script>
    <script type="text/javascript" src="/js/ui.biz.core.js"></script>
    <script type="text/javascript" src="/js/jquery.blockUI.js"></script>

    <script type="text/javascript" src="/js/bootstrap.4.5.2.min.js"></script>

    <link rel="resource" type="application/l10n" href="/module/pdfjs/web/locale/locale.properties">
    <script type="text/javascript" src="/module/pdfjs/build/pdf.js"></script>
    <script type="text/javascript"  src="/module/pdfjs/web/viewer.js"></script>

    <!-- 공인인증서 관련 -->
    <script type="text/javascript" src="/js/nxts/nxts.min.js"></script>
    <script type="text/javascript" src="/js/nxts/nxtspki_config.js"></script>
    <script type="text/javascript" src="/js/nxts/nxtspki.js"></script>

    <script type="text/javascript" src="/js/zoomEvent.js"></script>
    <script type="text/javascript" src="/js/page_swiper.js"></script>

    <!-- 첨부파일 css -->
    <style>
        .btn_adress {display: inline-block;vertical-align: middle;padding: 8px 20px;font-size: 13px;font-weight: bold;border-radius: 0px;border: none;color: #ffffff;background-color: #00a9ea;cursor: pointer}
        .inpstyle02 {border: 0px solid #d5d5d5;padding: 8px 10px;color: #666;font-weight: bold;font-size: 12px;background-color: white;cursor: pointer;}

        #eleContract_wrap .container .contit .btn_confirm_group .btn_type{min-width:0% !important;}
        #eleContract_wrap .container .contit .btn_confirm_group{max-width:100% !important;}
        #eleContract_wrap .container .contit .pageNav{width:10%;}
        #eleContract_wrap .container .contit .btn_confirm_group{width: 50%; max-width:350px; }
        #eleContract_wrap .container .contit .btn_confirm_group .btn_type{padding-left: 0px; box-sizing: border-box; float: right; }
        #eleContract_wrap .container .contit .btn_confirm_group .btn_type{width: calc(50% - 70px); max-width:171px; height: 46px; color:#fff; border-radius: 8px; font-size:20px; box-sizing: border-box;}

        #eleContract_wrap .container .contit .btn_confirm_group .btn_type{width: 25%; max-width:171px; height: 40px; color:#fff; border-radius: 8px; font-size:15px; box-sizing: border-box; word-break: keep-all; display: flex; display: -ms-flex; display: -webkit-flex; justify-content: center; -ms-justify-content: center; -webkit-justify-content: center; align-items: center; -ms-align-items: center; -webkit-align-items: center;}
        #eleContract_wrap .container .contit .btn_confirm_group .btn_type:first-child{background-color: #a94442; margin-right:8px;}
        #eleContract_wrap .container .contit .btn_confirm_group .btn_type:nth-child(2){background-color: #7c9dbc; margin-right:8px !important;}
        #eleContract_wrap .container .contit .btn_confirm_group .btn_type:nth-child(3){background-color: #1383d5 !important;margin-right:8px}
        #eleContract_wrap .container .contit .btn_confirm_group .btn_type:nth-child(4){background-color: #fe7d7d !important;}

        #eleContract_wrap .container .contit .btn_confirm_group .btn_type{padding-left:3px !important; box-sizing: border-box;}
    </style>
    <script>

        //***************					윈도우 로딩					***************//


        var url        = "";
        var result     = '${result}';
        var jsonData   = JSON.parse(result);
        var docuStatus = "1";
        var recvStatus = "1";
        var _orient    = "";
        var _zoom;

        $(document).ready(function() {

            if (isNotNull(jsonData.master.reqComment)) alert("[작성자 메모]\r\n"+jsonData.master.reqComment.replace(/<br>/g, "\r\n"));

            if (jsonData.data.length === 0) location.href="/document/goDocumentStepNone.do";

            url = "/" + jsonData.data[0].filePath;

            $("#docuId").val(jsonData.master.docuId);
            $("#bizId").val(jsonData.master.bizId);
            $("#recvUserId").val(jsonData.detail.recvUserId);
            $("#userId").val(jsonData.master.userId);
            $("#recvType").val(jsonData.detail.recvType);
            $("#contactType").val(jsonData.detail.contactType);

            //참조자는 버튼이 보이지 않음
            //검토단계일 경우 검토완료 / 정정요청
            //서명단계일 경우 서명 / 정정요청

            //첨부파일
            $.each(jsonData.file, function(i, row) {
                htmlFile = '	<a class="dropdown-item" href="#" onclick="fn_fileDocumentDownload(\''+row.fileId+'\');">'+row.fileName+'</a>';
                $("#fileList").append(htmlFile);
            });

            if (jsonData.file.length === 0) $("#dropdownMenuButton").addClass("hidden");

            //참조자는 첨부파일을 선택할 수 없음
            $("#attachFileList").addClass("hidden");
            $(".attach_btns").addClass('hidden');

            var pageList = "";
            $.each(jsonData.data, function(i, row) {
                var page = fullPath+row.filePath+row.fileName;
                pageList += "<div class=\"swiper-slide\"><img class=\"pinch-zoom-image\" src=\""+page+"\"></div>";
            });

            $("#pageList").html(pageList);

            var _targetY=$(window).height()/2;
            $("[class *='"+"swiper-button"+"']").css("top",_targetY);

            var _swiper=new swiperPage();
            _swiper.init();
        });

        $(window).load(function() { _orient="portrait";  });

        $(window).on("orientationchange", function(event) { _orient="landscape"; });

    </script>
    </head>

    <body>
    <input type="hidden" id="docuId">
    <input type="hidden" id="bizId">
    <input type="hidden" id="recvUserId">
    <input type="hidden" id="userId">
    <input type="hidden" id="recvType">
    <input type="hidden" id="contactType">
    <input type="file" id="attachFile" name="attachFile" style="visibility: hidden; position: absolute; left: -9999px; top: -9999px;">
    <div id="eleContract_wrap">
        <div class="header">
            <div class="logo"></div>
            <div class="pagetit">
                <span class="NanumGothic">전자문서</span>
            </div>
        </div>
        <div class="container">
            <div class="contit">
                <div class="dropdown">
                    <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">파일</button>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuButton" id="fileList"></div>
                </div>
                <div class="pageNav">
                    <div class="btn_prev btn_type Material_icons">이전보기</div>
                    <div class="swiper-pagination NanumGothic"></div>
                    <div class="btn_next btn_type Material_icons">다움보기</div>
                </div>
                <div class="NameDivTd" id="fileView"></div>
                <div class="btn_confirm_group" id="btnView"></div>
            </div>
            <div class="swiper-container">
                <div class="swiper-wrapper" id="pageList"></div>
            </div>
            <div class="btn_next swiper-button-next"></div>
            <div class="btn_prev swiper-button-prev"></div>
        </div>
    </div>
    </body>
    </html>