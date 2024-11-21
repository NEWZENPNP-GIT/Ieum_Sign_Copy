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

    <!-- 1. jQuery UI -->
    <script type="text/javascript" src="/js/jquery.min.js"></script>
    <script type="text/javascript" src="/js/common.js"></script>
    <script type="text/javascript" src="/js/jquery.blockUI.js"></script>

    <link rel="resource" type="application/l10n" href="/module/pdfjs/web/locale/locale.properties">
    <script type="text/javascript" src="/module/pdfjs/build/pdf.js"></script>
    <script type="text/javascript"  src="/module/pdfjs/web/viewer.js"></script>

    <script type="text/javascript" src="/js/zoomEvent.js"></script>
    <script type="text/javascript" src="/js/page_swiper.js"></script>

    <script>

        //***************					윈도우 로딩					***************//


        var success             = ${success};
        var result              = '${result}';
        var total               = ${total};
        var jsonData            = JSON.parse(result);
        var signBizType         = jsonData.signBizType;
        var signUserType        = jsonData.signUserType;
        var signCustomerType    = jsonData.signCustomerType;
        var correctionType      = jsonData.correctionType;
        var contractId          = jsonData.contractId;
        var isCandycashContract = false;
        var url                 = "";
        var _orient             = "";
        var _zoom;

        $(document).ready(function() {

            url             = "/" + jsonData.data[0].filePath;
            var htmlBtnView = "";
            var pageList    = "";

            if (contractId === "10000000000000000000000000000010") isCandycashContract = true;

            if (isNotNull(jsonData.contract.comment)) alert("[담당자 메모]\r\n"+jsonData.contract.comment.replace(/<br>/g, "\r\n"));


            //완료된 계약서가 아닐경우 -> 완료된 계약서에는 서명을 받지 않음.
            if (signUserType === "1") { //사용자의 서명이 필요한 경우
                htmlBtnView += '<a class="btn_type btn_signature" onclick="popupSign()" >서명</a>';
                if (correctionType === "Y") {
                    htmlBtnView += '<a class="btn_type btn_request" onclick="popupReject()" >정정 요청</a>';
                }
            } else if (signCustomerType === "1") { //거래처의 서명이 필요한 경우
                htmlBtnView += '<a class="btn_type btn_signature" onclick="popupCustomerSign()" >서명</a>';
                if (correctionType === "Y") {
                    htmlBtnView += '<a class="btn_type btn_request" onclick="popupReject()" >정정 요청</a>';
                }
            } else {
                htmlBtnView += '<a class="btn_type btn_signature" onclick="goComplete()" >완료</a>';
                if (correctionType === "Y") {
                    htmlBtnView += '<a class="btn_type btn_request" onclick="popupReject()" >정정 요청</a>';
                }
            }
            $("#btnView").html(htmlBtnView);

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


        //***************					수기 전자서명 팝업					***************//


        function popupSign() {
            var param = getParams();
            var url   = rootPath + "contract/goSignPad.do?id="+param.id+"&key="+jsonData.id+"&num=1";
            openWin(url, "contract_signpad", 520, 466);
        }


        //***************					직인 팝업					***************//


        function popupCustomerSign() {
            var param = getParams();
            var url   = rootPath + "contract/goSignCustomer.do?id="+param.id+"&key="+jsonData.id+"&num=1";
            openWin(url, "contract_signCustomer", 530, 400);
        }


        //***************					정정요청 팝업					***************//


        function popupReject() {
            var param = getParams();
            var url   = rootPath + "contract/goRejectPad.do?id="+param.id+"&key="+jsonData.id+"&num=1";
            openWin(url, "contract_signpad", 520, 466);
        }


        //***************					서명완료 페이지 이동					***************//


        function moveContractComplete(signBizType, signUserType, signCustomerType) {
            if (signBizType !== undefined && signBizType != null && signUserType !== undefined && signUserType != null && signCustomerType != null) {
                location.href=rootPath+"contract/goContractComplete.do?signBizType="+signBizType+"&signUserType="+signUserType+"&signCustomerType="+signCustomerType;
            } else {
                location.href=rootPath+"contract/goContractComplete.do?signBizType=&signUserType=&signCustomer=";
            }
        }


        //***************					정정완료 페이지 이동					***************//


        function moveContractRejectComplete() { location.href=rootPath+"contract/goContractRejectComplete.do"; }


        //***************					근로자서명없이 완료처리					***************//


        function goComplete() {

            var param = getParams();
            var url   = rootPath + "contract/signContract.do";

            if (!confirm("전자문서의 내용을 확인하신 후\r\n정정할 내용이 없으면\r\n확인을 클릭해주세요.")) return;

            $.blockUI();

            $.ajax({
                url: url,
                crossDomain: true,
                dataType: "json",
                type: "POST",
                data: {
                    id: param.id,
                    key: "signdata",
                    num: "1",
                    line: "1-1-1-1|"
                },
                success: function (result) {
                    $.unblockUI();
                    if (result.success === true) {
                        if (signBizType === "0" && signUserType === "1") alert("전자서명이 완료되었습니다.\r\n서명한 전자문서의 다운로드는 담당자에게 문의 부탁 드립니다.");
                        else                                             alert("전자서명이 완료되었습니다.\r\n잠시 후 서명하신 문서가 배부됩니다.\r\n배부된 전자문서를 다운로드 받아 보관하시기 바랍니다.\r\n문서 분실 시 담당자에게 문의 부탁 드립니다.");
                        moveContractComplete(signBizType, signUserType, );
                    } else {
                        if (!isNull(result.message)) alert(result.message);
                    }
                },
                error: function (request, status, error) {
                    $.unblockUI();
                    alert("전자문서 정보 전송 중 장애가 발생하였습니다. 잠시 후 다시 진행해주시기 바랍니다.");
                }
            });

        }

        $(window).load(function() {
            if (window.orientation === 0) _orient="portrait";
            else                          _orient="landscape";
        });

        $(window).on("orientationchange", function(event) {
            if (window.orientation == 0) _orient="portrait";
            else                         _orient="landscape";
        });

    </script>
    </head>

    <body>
    <div id="eleContract_wrap" class="">
        <div class="header">
            <div class="logo"></div>
            <div class="pagetit"><span class="NanumGothic">전자문서</span></div>
        </div>
        <div class="container">
            <div class="contit">
                <span class="tit NanumGothic">문서 확인</span>
                <div class="pageNav">
                    <div class="btn_prev btn_type Material_icons">play_arrow</div>
                    <div class="swiper-pagination NanumGothic"></div>
                    <div class="btn_next btn_type Material_icons">play_arrow</div>
                </div>
                <div class="btn_group" id="btnView"></div>
            </div>
            <div class="swiper-container">
                <div class="swiper-wrapper" id="pageList"></div>
            </div>
        </div>
    </div>
    </body>
    </html>