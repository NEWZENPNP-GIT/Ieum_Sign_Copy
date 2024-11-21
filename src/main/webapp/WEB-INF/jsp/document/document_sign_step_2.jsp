
    <!--****************************************************************************************************************
    * 상기 프로그램에 대한 저작권을 포함한 직접재산권은 NewZen P&P에 있으며 NewZen P&P가 명시적으로 허용하지 않은
    * 사용, 복사, 변경, 제3자에 의한 공개 배포는 엄격히 금지되며 NewZen P&P의 지적재산권 침해에 해당합니다.
    ********************************************************************************************************************
    --------------------------------------------------------------------------------------------------------------------
    * 회사       : 뉴젠피앤피
    * 화면명     : 전자문서
    * 화면설명   : 전자계약 작성 > 수신자 에게 전달된 메일
    * 작성자     :
    * 생성일     :
    --------------------------------------------------------------------------------------------------------------------
    * 수정자     : 이정훈
    * 수정일     : 2023.10.31
    * 수정내용   :
    ------------------------------------------------------------------------------------------------------------------->

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
    <link rel="stylesheet" type="text/css" href="/css/font_awesome/css/all.min.css">
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
        #eleContract_wrap .container .contit {flex-wrap: wrap; justify-content: space-between; row-gap: 8px; position: relative; height: auto; padding: 12px 0;}
        .btnGroupBox {display: grid; grid-auto-flow: column; justify-content: space-between; align-items: center; column-gap: 8px;}
        .btnGroupBox .btn_type {display: inline-block; padding: 0 16px; border-radius: 6px; line-height: 40px; text-decoration: none; color: #fff; background-color: #1383d5;}
        .btnGroupBox .btn_type.btn_sky {background-color: #7c9dbc;}
        .drop_box {position: relative;}
        .drop_box .drop_item {display: inline-block; padding: 0 30px 0 16px; border: 0; border-radius: 6px; outline: none; line-height: 40px; color: #fff; background: url(/images/drop_arrow.png) no-repeat 94% center #707980; background-size: 14px;}
        .drop_box .drop_menu { display: none; position: absolute; left: 0; top: 42px; z-index: 100; width: max-content; min-width: 100%; max-height: 204px; padding: 8px 0; border: 1px solid rgba(0, 0, 0, .2); border-radius: 4px; background-color: #fff; overflow-y: auto;}
        .drop_box .drop_menu > li {position: relative;}
        .drop_box .drop_menu > li > a {display: block; padding: 5px 12px; font-size: 14px; text-decoration: none; color: #000}
        .drop_box .drop_menu > li > a:hover {background-color: #f8f9fa;}
        .drop_box.receiver .drop_item {background-color: #c84846;}
        .drop_box.receiver .drop_menu > li > a {padding-right: 28px;}
        .drop_box.receiver .drop_menu > li > input[type='file'] {display: none;}
        .drop_box.receiver .drop_menu > li .upload {display: block; padding: 4px 12px 6px; font-size: 14px; font-weight: bold; text-align: center; color: #1383d5; cursor: pointer;}
        .drop_box.receiver .drop_menu > li .upload:hover {text-decoration: underline; background-color: transparent;}
        .drop_box.receiver .drop_menu > li .del {display: block; position: absolute; right: 0; top: 0; width: 28px; height: 28px; background: url(/images/x.png) no-repeat center; background-size: 10px; cursor: pointer;}
    </style>
    <script>

        var url                 = "";
        var result              = '${result}';
        var jsonData            = JSON.parse(result);
        var docuStatus          = "1";
        var recvStatus          = "1";
        var signBizType         = jsonData.signBizType;
        var signUserType        = jsonData.signUserType;
        var signCustomerType    = jsonData.signCustomerType;
        var correctionType      = jsonData.correctionType;
        var contractId          = jsonData.contractId;
        var isCandycashContract = false;
        var userFileId          = 0;
        var userFileList        = [];
        var _orient             = "";
        var _zoom;

        //***************					윈도우 로딩					***************//


        $(document).ready(function() {

            $(document).on('click', '.drop_item', function() {
                if ($(this).hasClass('on')) {
                    $(this).removeClass('on');
                    $(this).siblings('.drop_menu').fadeOut(150);
                } else {
                    $('.drop_item').removeClass('on');
                    $(this).addClass('on');
                    $('.drop_menu').fadeOut(150);
                    $(this).siblings('.drop_menu').fadeIn(150);
                }
            });

            $(document).on('click', 'html', function(e) {
                if(!$(e.target).is('.drop_item')) {
                    $('.drop_item').removeClass('on');
                    $('.drop_menu').fadeOut(150);
                }
            });

            if (navigator.userAgent.match(/iPhone/i)) { $(".swiper-wrapper").css({ height: "auto"}); console.log("iphone"); }

            if (isNotNull(jsonData.master.reqComment)) alert("[작성자 메모]\r\n"+jsonData.master.reqComment.replace(/<br>/g, "\r\n"));

            if (jsonData.data.length === 0) location.href="/document/goDocumentStepNone.do";

            url = "/" + jsonData.data[0].filePath;

            $("#docuId").val(jsonData.master.docuId);
            $("#bizId").val(jsonData.master.bizId);
            $("#recvUserId").val(jsonData.detail.recvUserId);
            $("#userId").val(jsonData.master.userId);
            $("#recvType").val(jsonData.detail.recvType);
            $("#contactType").val(jsonData.detail.contactType);

            var htmlBtnView = "";

            //첨부파일
            $.each(jsonData.file, function(i, row) {
                htmlFile = '<li><a href="#" onclick="fn_fileDocumentDownload(\''+row.fileId+'\');">'+row.fileName+'</a></li>';
                $("#fileList").append(htmlFile);
            });

            if (jsonData.file.length === 0) $("#dropdownMenuButton").addClass("hidden");

            //참조자는 버튼이 보이지 않음
            if (jsonData.detail.recvType === "R") {
                docuStatus = jsonData.master.docuStatus;
                recvStatus = jsonData.detail.recvStatus;
                //  검토완료 버튼
                var htmlBtnReviewComment = '<a class="btn_type btn_signature btn_sky" onclick="popupReviewComment()">검토 완료</a>';
                //  정정요청 버튼(검토용)
                var htmlBtnReviewReject = '<a class="btn_type btn_request" onclick="popupReviewReject()">정정 요청</a>';
                //  수기 서명 버튼
                var htmlBtnSign = '<a class="btn_type btn_signature" onclick="popupSign()">서명</a>';
                // 	직인 버튼
                var htmlBtnCustom = '<a class="btn_type btn_signature" onclick="popupCustomerSign()">서명</a>';
                //  완료버튼
                var htmlBtnComplete = '<a class="btn_type btn_signature" onclick="goComplete()">완료</a>';
                //  정정요청 버튼
                var htmlBtnReject = '<a class="btn_type btn_request" onclick="popupReject()">정정 요청</a>';

                if (docuStatus === "1") {
                    // 발송
                    if (jsonData.master.reviewType === "Y" || jsonData.master.reviewType === "S") {
                        // 검토
                        htmlBtnView += htmlBtnReviewComment;
                        htmlBtnView += htmlBtnReviewReject;
                    } else {
                        if (jsonData.master.signType === "T") {
                            // 서명이 수기서명일 경우 (contatctType = P)
                            htmlBtnView += htmlBtnSign;
                        } else if(jsonData.master.signType === "C"){
                            // 서명이 직인일 경우 (contatctType = C)
                            htmlBtnView += htmlBtnCustom;
                        } else if (jsonData.master.signType === "TC"){
                            // 수기서명 + 직인 일 경우
                            if (jsonData.detail.contactType === "P"){
                                htmlBtnView += htmlBtnSign;
                            } else if (jsonData.detail.contactType === "C"){
                                htmlBtnView += htmlBtnCustom;
                            }
                        } else {
                            //서명이 없을 경우
                            htmlBtnView += htmlBtnComplete;
                        }
                        htmlBtnView += htmlBtnReject;
                    }
                } else if (docuStatus === "4") {
                    // 검토중
                    htmlBtnView += htmlBtnReviewComment;
                    htmlBtnView += htmlBtnReviewReject;
                } else if (docuStatus === "7") {
                    // 서명요청
                    if (jsonData.master.signType === "T"){
                        // 서명이 수기서명일 경우 (contatctType = P)
                        htmlBtnView += htmlBtnSign;
                    } else if (jsonData.master.signType === "C"){
                        // 서명이 직인일 경우 (contatctType = C)
                        htmlBtnView += htmlBtnCustom;
                    } else if (jsonData.master.signType === "TC"){
                        // 수기서명 + 직인 일 경우
                        if (jsonData.detail.contactType === "P"){
                            htmlBtnView += htmlBtnSign;
                        } else if (jsonData.detail.contactType === "C"){
                            htmlBtnView += htmlBtnCustom;
                        }
                    } else {
                        //서명이 없을 경우
                        htmlBtnView += htmlBtnComplete;
                    }
                    htmlBtnView += htmlBtnReject;
                } else if (docuStatus === "8") {
                    // 서명중
                    if (jsonData.master.signType === "T") {
                        // 서명이 수기서명일 경우 (contatctType = P)
                        htmlBtnView += htmlBtnSign;
                    } else if (jsonData.master.signType === "C") {
                        // 서명이 직인일 경우 (contatctType = C)
                        htmlBtnView += htmlBtnCustom;
                    } else if (jsonData.master.signType === "TC") {
                        // 수기서명 + 직인 일 경우
                        if (jsonData.detail.contactType === "P") {
                            htmlBtnView += htmlBtnSign;
                        } else if (jsonData.detail.contactType === "C") {
                            htmlBtnView += htmlBtnCustom;
                        }
                    } else {
                        //서명이 없을 경우
                        htmlBtnView += htmlBtnComplete;
                    }
                    htmlBtnView += htmlBtnReject;
                }
            } else {
                //참조자는 첨부파일을 선택할 수 없음
                $("#attachFileList").addClass("hidden");
                $(".attach_btns").addClass('hidden');
            }

            $("#btnView").append(htmlBtnView);

            var pageList = "";
            $.each(jsonData.data, function(i, row) {
                var page = fullPath+row.filePath+row.fileName;
                pageList += "<div class=\"swiper-slide\"><img class=\"pinch-zoom-image\" src=\""+page+"\"></div>";
            });
            $("#pageList").html(pageList);

            var _targetY=$(window).height()/2;
            $("[class *='"+"swiper-button"+"']").css("top",_targetY);


            //화면 크기에 따른 swiper 버튼 위치 지정
            var _swiper=new swiperPage();
            _swiper.init();

        });

        $(window).load(function() { _orient="portrait"; });

        $(window).on("orientationchange", function(event) { _orient="portrait"; });


        //***************					파일 첨부					***************//


        function openFileInput() { $('#userFileList').click(); }

        function attachFile() {

            var fileList  = document.getElementById("userFileList").files;
            var maxSize   = 200 * 1024 * 1024; // 200MB
            var totalSize = 0;
            var fileCnt   = 0;

            // 최대 5개 파일까지 허용
            for (var i = 0; i < fileList.length; i++) {

                var fileName = fileList[i].name;

                if(checkSpecial(fileName)) {
                    alert("파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.");
                    return;
                }

                if(fileList[i].value != undefined){ totalSize += fileList[i].value.size; }
                fileCnt++;
            }

            if( fileCnt >= 6){
                alert("첨부파일은 최대 5개 까지 등록 가능합니다.");
                return;
            }

            if (totalSize > maxSize) {
                alert('파일은 ' + (maxSize / 1024 / 1024) + 'MB 이하의 파일만 첨부 가능합니다','');
                return;
            }
            for (var i = 0; i < fileList.length; i++) { ParseFile(fileList[i]); }
        }

        function Output(msg) {
            var m = document.getElementById("userFileListDrop");
            m.innerHTML = msg + m.innerHTML;
        }

        function ParseFile(file) {

            if (userFileList.length >= 5) {
                alert("첨부파일은 최대 5개 까지 등록 가능합니다.");
                return;
            }

            userFileId++;

            var m = $("#userFileListDrop");
            if (!m.find(".nonfile").hide()) { m.find(".nonfile").show(); }

            Output('<li class="file" id="'+userFileId+'"><a href="javascript:void(0);" class="name">'+file.name+'<span class="del" onClick="attachFileDel('+userFileId+');"></span></a></li>');

            var oFile = { key: userFileId, value: file };
            userFileList.push(oFile);
        }

        function attachFileDel(userFileId) {

            $('*[name=attachFile]').val("");
            $("#"+userFileId).remove();

            if ($("#userFileList").children().length === 1) $(".nonfile").show();

            for (var i=0; i<userFileList.length; i++) {
                if (userFileList[i].key === userFileId) { userFileList.splice(i, 1); return; }
            }
        }

        //***************					수기 전자서명 팝업					***************//


        function popupSign() {

            var param = getParams();
            var url   = rootPath + "document/goDocumentSignPad.do?id="+param.id+"&key="+jsonData.id+"&num=1";
            openWin(url, "document_signpad", 520, 466);
        }


        //***************					직인 팝업					***************//


        function popupCustomerSign() {
            var param = getParams();
            var url   = rootPath + "document/goDocumentSignCustomer.do?id="+param.id+"&key="+jsonData.id+"&num=1";
            openWin(url, "document_signCustomer", 530, 400);
        }


        //***************					정정요청 팝업					***************//


        function popupReject() {
            var param = getParams();
            var url   = rootPath + "document/goDocumentRejectPad.do?id="+param.id+"&contactType="+jsonData.contactType;
            openWin(url, "document_signpad", 520, 466);
        }


        //***************					정정요청 팝업(검토용)					***************//


        function popupReviewReject() {
            var param = getParams();
            var url   = rootPath + "document/goDocumentReviewRejectPad.do?id="+param.id+"&contactType="+jsonData.contactType;
            openWin(url, "document_signpad", 520, 466);
        }


        //***************					검토완료 시 팝업					***************//


        function popupReviewComment(){
            var param = getParams();
            var url   = rootPath + "document/goDocumentReviewCommentPad.do?id="+param.id;
            openWin(url, "document_signpad", 520, 266);
        }


        //***************					서명완료 페이지 이동					***************//


        function moveDocumentComplete(docuStatus) {
            if(docuStatus == "4"){
                //검토를 완료했을 경우
                location.href=rootPath+"document/goDocumentReviewComplete.do";
            } else {
                //서명을 완료했을 경우
                location.href=rootPath+"document/goDocumentComplete.do";
            }
        }


        //***************					정정완료 페이지 이동					***************//


        function moveDocumentRejectComplete() { location.href=rootPath+"document/goDocumentRejectComplete.do"; }


        //***************					근로자서명없이 완료처리					***************//


        function goComplete() {

            if(!confirm("완료 하시겠습니까?")) return;

            var formData        = new FormData();
            var url             = rootPath + "document/forwardDocument.do";
            var docuId          = $("#docuId").val();
            var recvUserId      = $("#recvUserId").val();
            var bizId           = $("#bizId").val();
            var userId          = $("#userId").val();
            var recvType        = $("#recvType").val();
            var contactType     = $("#contactType").val();
            var attachFileList  = document.getElementsByName("attachFile");

            formData.append("docuId", docuId);
            formData.append("recvUserId", recvUserId);
            formData.append("bizId", bizId);
            formData.append("recvStatus", "9");
            formData.append("userId", userId);
            formData.append("recvType", recvType);
            formData.append("contactType", contactType);

            for(var i=0;i<attachFileList.length;i++) {
                if (attachFileList[i].files[0] != undefined) {
                    if(checkSpecial(attachFileList[i].files[0].name)) {
                        alert("파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.");
                        return;
                    }
                    formData.append("ATTACH_FILE["+i+"]",attachFileList[i].files[0]);
                }
            }

            for (var j = 0; j < userFileList.length; j++) {
                if (userFileList[j].value != undefined) {
                    if (checkSpecial(userFileList[j].value.name)) {
                        alert("파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.");
                        return;
                    }
                    formData.append("ATTACH_FILE[" + (attachFileList.length + j) + "]", userFileList[j].value);
                }
            }

            $.ajax({
                url:url,
                crossDomain : true,
                dataType:"json",
                async:false,
                type:"POST",
                processData: false,
                contentType: false,
                data:formData,
                success: function (result) {
                    if (result.success == true) { moveDocumentComplete("9"); }
                    else { if (!isNull(result.message)) alert(result.message); }
                },
                error: function (request, status, error) {
                    $.unblockUI();
                    alert("전자문서 정보 전송 중 장애가 발생하였습니다. 잠시 후 다시 진행해주시기 바랍니다.");
                }
            });
        }


        //***************					수기서명 등록					***************//


        function fn_sign(line) {

            var formData        = new FormData();
            var url             = rootPath + "document/forwardDocument.do";
            var docuId          = $("#docuId").val();
            var recvUserId      = $("#recvUserId").val();
            var bizId           = $("#bizId").val();
            var userId          = $("#userId").val();
            var recvType        = $("#recvType").val();
            var contactType     = $("#contactType").val();
            var attachFileList  = document.getElementsByName("attachFile");

            formData.append("docuId", docuId);
            formData.append("recvUserId", recvUserId);
            formData.append("bizId", bizId);
            formData.append("userId", userId);
            formData.append("recvType", recvType);
            formData.append("recvSign", line);
            formData.append("recvStatus", "9");
            formData.append("contactType", contactType);

            for(var i=0;i<attachFileList.length;i++) {
                if (attachFileList[i].files[0] != undefined) {
                    if(checkSpecial(attachFileList[i].files[0].name)) {
                        alert("파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.");
                        return;
                    }
                    formData.append("ATTACH_FILE["+i+"]",attachFileList[i].files[0]);
                }
            }

            for (var j = 0; j < userFileList.length; j++) {
                if (userFileList[j].value != undefined) {
                    if (checkSpecial(userFileList[j].value.name)) {
                        alert("파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.");
                        return;
                    }
                    formData.append("ATTACH_FILE[" + (attachFileList.length + j) + "]", userFileList[j].value);
                }
            }

            $.blockUI();

            $.ajax({
                url:url,
                crossDomain : true,
                dataType:"json",
                async:false,
                type:"POST",
                processData: false,
                contentType: false,
                data:formData,
                success: function (result) {
                    $.unblockUI();
                    if (result.success == true) { moveDocumentComplete("9"); }
                    else                        { alert(result.message); }
                },
                error: function (request, status, error) {
                    $.unblockUI();
                    alert("전자문서 정보 전송 중 장애가 발생하였습니다. 잠시 후 다시 진행해주시기 바랍니다.");
                }
            });
        }


        //***************					직인 등록					***************//


        function fn_customer_sign(param) {

            var formData        = param;
            var url             = rootPath + "document/forwardDocument.do";
            var docuId          = $("#docuId").val();
            var recvUserId      = $("#recvUserId").val();
            var bizId           = $("#bizId").val();
            var userId          = $("#userId").val();
            var recvType        = $("#recvType").val();
            var contactType     = $("#contactType").val();
            var attachFileList  = document.getElementsByName("attachFile");

            formData.append("docuId", docuId);				// 문서 ID
            formData.append("recvUserId", recvUserId);		// 수신/참조자ID
            formData.append("bizId", bizId);				// 기업 ID
            formData.append("userId", userId);				// 사용자 ID
            formData.append("recvType", recvType);
            formData.append("recvStatus", "9");
            formData.append("contactType", contactType);

            for(var i=0;i<attachFileList.length;i++) {
                if (attachFileList[i].files[0] != undefined) {
                    if(checkSpecial(attachFileList[i].files[0].name)) {
                        alert("파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.");
                        return;
                    }
                    formData.append("ATTACH_FILE["+i+"]",attachFileList[i].files[0]);
                }
            }

            for (var j = 0; j < userFileList.length; j++) {
                if (userFileList[j].value != undefined) {
                    if (checkSpecial(userFileList[j].value.name)) {
                        alert("파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.");
                        return;
                    }
                    formData.append("ATTACH_FILE[" + (attachFileList.length + j) + "]", userFileList[j].value);
                }
            }

            $.blockUI();

            $.ajax({
                url:url,
                crossDomain : true,
                dataType:"json",
                async:false,
                type:"POST",
                processData: false,
                contentType: false,
                data:formData,
                success: function (result) {
                    $.unblockUI();
                    if (result.success == true) { moveDocumentComplete("9"); }
                    else                        {  alert(result.message); }
                },
                error: function (request, status, error) {
                    $.unblockUI();
                    alert("전자문서 정보 전송 중 장애가 발생하였습니다. 잠시 후 다시 진행해주시기 바랍니다.");
                }
            });
        }


        //***************					공인인증서 본인인증					***************//


        function callSignData() {
            var options = {};
            var signdata = [];
            signdata.push($("#docuId").val());
            if(signdata.length>0) { nxTSPKI.multiSignData(signdata, options, sign_complete_callback); }
        }


        //***************					공인인증서 후 기본 직인 등록					***************//


        function sign_complete_callback(res) {
            if (res.code == 0) {
                if(res.data.length!=1) {
                    alert("전자서명한 문서의 건수가 맞지 않습니다.\r\n다시 전자서명을 진행해주시기 바랍니다.");
                    return;
                }
                var digitSign = "";
                for(var i=0;i<res.data.length;i++) { digitSign = res.data[i].signedData; }
                goCompleteCert(digitSign);
            }
        }


        //***************					근로자서명없이 완료처리					***************//


        function goCompleteCert(recvSign) {

            var formData        = new FormData();
            var url             = rootPath + "document/forwardDocument.do";
            var docuId          = $("#docuId").val();
            var recvUserId      = $("#recvUserId").val();
            var bizId           = $("#bizId").val();
            var userId          = $("#userId").val();
            var recvType        = $("#recvType").val();
            var contactType     = $("#contactType").val();
            var attachFileList  = document.getElementsByName("attachFile");

            formData.append("docuId", docuId);
            formData.append("recvUserId", recvUserId);
            formData.append("bizId", bizId);
            formData.append("recvStatus", "9");
            formData.append("userId", userId);
            formData.append("recvType", recvType);
            formData.append("contactType", contactType);

            for(var i=0;i<attachFileList.length;i++) {
                if (attachFileList[i].files[0] != undefined) {
                    if(checkSpecial(attachFileList[i].files[0].name)) {
                        alert("파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.");
                        return;
                    }
                    formData.append("ATTACH_FILE["+i+"]",attachFileList[i].files[0]);
                }
            }

            for (var j = 0; j < userFileList.length; j++) {
                if (userFileList[j].value != undefined) {
                    if (checkSpecial(userFileList[j].value.name)) {
                        alert("파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.");
                        return;
                    }
                    formData.append("ATTACH_FILE[" + (attachFileList.length + j) + "]", userFileList[j].value);
                }
            }

            $.ajax({
                url:url,
                crossDomain : true,
                dataType:"json",
                async:false,
                type:"POST",
                processData: false,
                contentType: false,
                data:formData,
                success: function (result) {
                    if (result.success == true) { moveDocumentComplete("9"); }
                    else { if (!isNull(result.message)) alert(result.message); }
                },
                error: function (request, status, error) {
                    $.unblockUI();
                    alert("전자문서 정보 전송 중 장애가 발생하였습니다. 잠시 후 다시 진행해주시기 바랍니다.");
                }
            });
        }

    </script>
    </head>
    <body>
    <input type="hidden" id="docuId">
    <input type="hidden" id="bizId">
    <input type="hidden" id="recvUserId">
    <input type="hidden" id="userId">
    <input type="hidden" id="recvType">
    <input type="hidden" id="contactType">
    <input type="hidden" id="reviewType">
    <input type="file" id="attachFile" name="attachFile" style="visibility: hidden; position: absolute; left: -9999px; top: -9999px;">
    <div id="eleContract_wrap" class="">
        <div class="header">
            <div class="logo"></div>
            <div class="pagetit"><span class="NanumGothic">전자문서</span></div>
        </div>

        <div class="container">
            <div class="contit">
                <div class="drop_box sender">
                    <button type="button" class="drop_item">발신자 첨부파일</button>
                    <ul class="drop_menu" id="fileList"></ul>
                </div>

                <div class="pageNav">
                    <div class="btn_prev btn_type Material_icons">이전보기</div>
                    <div class="swiper-pagination NanumGothic"></div>
                    <div class="btn_next btn_type Material_icons"><i class="fas fa-caret-right"></i>다움보기</div>
                </div>

                <div class="btnGroupBox" id="btnView">
                    <div class="drop_box receiver">
                        <button type="button" class="drop_item">수신자 첨부파일 <span></span></button>
                        <ul class="drop_menu" id="userFileListDrop">
                            <li>
                                <div class="upload" onclick="openFileInput()">파일 첨부</div>
                                <a href="javascript:void(0);" class="nonfile">첨부된 파일이 없습니다.<br>파일 최대 5개 첨부 가능 (최대 200메가)</a>
                                <input type="file" id="userFileList" onchange="attachFile()"  multiple style="display: none;">
                            </li>
                        </ul>
                    </div>
                </div>
            </div>

            <div class="swiper-container">
                <div class="swiper-wrapper" id="pageList"> </div>
            </div>
        </div>
    </div>
    </body>
    </html>