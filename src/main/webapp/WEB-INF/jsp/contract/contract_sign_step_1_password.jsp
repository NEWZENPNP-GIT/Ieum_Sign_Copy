    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <!DOCTYPE html>
    <html>
    <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, user-scalable=no">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Pragma" content="no-cache">
    <link rel="shortcut icon" href="https://www.kfhi.or.kr/assets/web/img/common/favicon.ico">
    <link rel="stylesheet" type="text/css" href="/css/font.css">
    <link rel="stylesheet" type="text/css" href="/css/material-icons.min.css">
    <link rel="stylesheet" type="text/css" href="/css/electornic_contract.css">
    <link rel="stylesheet" type="text/css" href="/css/electronic_layout.css">

    <script type="text/javascript" src="/js/jquery.min.js"></script>
    <script type="text/javascript" src="/js/loader.js"></script>
    <script type='text/javascript' src="/js/jquery.bpopup.js"></script>
    <script type="text/javascript" src="/js/jquery.blockUI.js"></script>
    <script type="text/javascript" src="/js/common.js"></script>
    <style>
        .popupstylediv .pop_title{border-bottom:0px;}
        #electronic_layout .popupdiv02{padding-top:5px;margin-top:5px;}
    </style>

    <script>

        //***************					윈도우 로딩					***************//


        $(document).ready(function() {
            $('#pwd').keypress(function(event) {
                var keycode = (event.keyCode ? event.keyCode : event.which);
                if (keycode === '13') fn_checkAuthCode(1);
                event.stopPropagation();
            });
            $("#pwd").focus();
        });


        //***************					거래처 인증코드 확인					***************//


        function fn_checkAuthCode(){

            var id       = getURLParameters('id').split("_")[0];
            var url      = "/contract/checkAuthCode.do";
            var authCode = $("#pwd").val();

            if (isNull(authCode)) { alert("비밀번호를 입력해주세요."); return false; }

            $.ajax({
                url:url,
                crossDomain : true,
                dataType:"json",
                type:"POST",
                data: {
                    authCode:authCode,
                    digitNonce:id
                },
                success:function(result) {
                    if (result.success) { opener.location.href = "/contract/contractPersonEtcView.do?id=" + getURLParameters('id'); window.close(); }
                    else                { alert("비밀번호가 올바르지 않습니다."); }
                },
                error:function(request,status,error) { goInvalidPage(request, error); }
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
    <div id="electronic_layout">
        <div class="popupstylediv">
            <div class="pop_title"><h3>비밀번호를 입력해주세요</h3></div>
            <div class="popupdiv02"><input id="pwd" type="password" name="" class="passwordinput"></div>
            <div class="footbtnareadiv footcenterdiv"><a class="electronic_btn02" onclick="fn_checkAuthCode()">확인</a></div>
        </div>
    </div>
    </body>
    </html>