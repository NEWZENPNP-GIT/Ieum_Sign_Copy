    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <!DOCTYPE html>
    <html>
    <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, user-scalable=no">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Pragma" content="no-cache">

    <link rel="stylesheet" type="text/css" href="/css/font.css">
    <link rel="stylesheet" type="text/css" href="/css/material-icons.min.css">
    <link rel="stylesheet" type="text/css" href="/css/electornic_contract.css">

    <script type="text/javascript" src="/js/jquery.min.js"></script>
    <script type="text/javascript" src="/js/common.js"></script>
    <script>

        //***************					윈도우 로딩					***************//


        $(document).ready(function () {

            $("#logMessage").focus();

            param = getParams();
            $("#id").val(param.id);
            $("#key").val(param.key);
            $("#num").val(param.num);
        });


        //***************					수정사유 저장					***************//


        function fn_save() {

            var url         = rootPath + "contract/rejectContract.do";
            var logMessage  = $("#logMessage").val();
            var id          = $("#id").val();

            if (logMessage.length === 0) { alert("정정사유를 입력하세요."); return; }

            if (logMessage.length >= 201) { alert("정정사유는 200자 이내로 작성하시기 바랍니다."); return; }

            $.ajax({
                url: url,
                crossDomain: true,
                dataType: "json",
                type: "GET",
                data: {
                    id: id,
                    message: logMessage
                },
                success: function (result) {
                    if (result.success) {
                        alert("정정요청이 등록되었습니다.")
                        opener.moveContractRejectComplete();
                        window.close();
                        window.shouldClose = true;
                    } else { alert("정정요청중 문제가 발생하였습니다.\r\n담당자에게 확인 요청드립니다."); }
                },
                error: function (request, status, error) { alert("정정요청 등록이 실패하였습니다.잠시 후 다시 시도해주세요."); }
            });
        }


        //***************					메세지 수정					***************//


        function messageEdit() {

            var logMessage = $("#logMessage").val();

            if (logMessage.length >= 50) {
                alert("50자 이상 입력할수 없습니다.");
                $("#logMessage").focus();
            }
        }


        //***************					닫기					***************//


        $(".close").click(function () { window.close(); window.shouldClose = true; })

    </script>
    </head>
    <body>

    <input type="hidden" id="id" value=""/>
    <input type="hidden" id="key" value=""/>
    <input type="hidden" id="num" value=""/>
    <div id="request" class="">
    <div class="popup_bg">
        <ul class="req">
            <li class="poptit">
                <span class="text NanumGothic">정정 사유 입력</span>
                <span class="btn_type close Material_icons">close</span></li>
            <li class="popcon">
                <textarea class="NanumGothic" id="logMessage" onkeypress="messageEdit();"></textarea>
            </li>
            <li class="popbtn-group NanumGothic">
                <a class="btn_type btn_ok" onclick="fn_save()">확인</a>
                <a class="btn_type btn_close close">닫기</a>
            </li>
        </ul>
    </div>
</div>
</body>
</html>