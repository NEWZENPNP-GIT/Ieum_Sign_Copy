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
    <script src="/js/jquery.blockUI.js"></script>


    <script src="/js/JSLINQ.js"></script>
    <script src="/js/history.js"></script>
    <script type="text/javascript" src="/js/webGL/gl-matrix.js"></script>
    <script type="text/javascript" src="/js/webGL/webgl-utils.js"></script>
    <script type="text/javascript" src="/js/webGL/Point.js"></script>
    <script type="text/javascript" src="/js/webGL/Color.js"></script>
    <script type="text/javascript" src="/js/webGL/DrawUtils.js"></script>
    <script type="text/javascript" src="/js/vector.js"></script>

    <script id="shader-fs" type="x-shader/x-fragment">
        precision mediump float;
        uniform vec4 uColor;
        void main(void) { gl_FragColor = uColor; }
    </script>

    <script id="shader-vs" type="x-shader/x-vertex">
        attribute vec3 aVertexPosition;
        uniform mat4 uPMatrix;
        void main(void) { gl_Position = uPMatrix * vec4(aVertexPosition, 1.0); }
    </script>

    <script>

        //***************					윈도우 로딩					***************//


        $(document).ready(function () {

            initCanvas();

            param = getParams();
            $("#id").val(param.id);
            $("#key").val(param.key);
            $("#num").val(param.num);
        });


        //***************					지우기					***************//


        function fn_reset() { initCanvas(); History.removeAllItem(); actionData = null; }


        //***************					서명 저장					***************//


        function fn_save() {

            var url       = rootPath + "contract/signContract.do";
            var linedata  = History.getItems();
            var initScale = 0.5;
            var id        = $("#id").val();
            var key       = $("#key").val();
            var num       = $("#num").val();
            var line      = "";
            var count     = 0;

            if (actionData != null) { History.addAction(actionData); actionData = null; }

            if (linedata.length === 0) { alert("서명을 하신 후 진행해주시기 바랍니다."); return; }

            for (var j = 0; j < linedata.length; j++) {
                var linehead = linedata[j].id + "-" + linedata[j].linewidth + "-";
                for (var k = 0; k < linedata[j].data.length; k++) {
                    var point = linedata[j].data[k];
                    var lineX = (point.x * initScale);
                    var lineY = (point.y * initScale);
                    line += linehead + lineX + "-" + lineY + "|";
                    count++;
                }
            }

            if (count < 5) { alert("서명에 대한 정보가 부족합니다."); return; }

            $.blockUI();

            $.ajax({
                url: url,
                crossDomain: true,
                dataType: "json",
                type: "POST",
                data: {
                    id: id,
                    key: key,
                    num: num,
                    line: line
                },
                success: function (result) {
                    $.unblockUI();
                    if (result.success === true) {
                    	if (result.data !== undefined) { //양식이 존재할 경우
                    		if (result.data.signBizType === "1" && result.data.signUserType === "1") {
                    			alert("전자서명이 완료되었습니다.\r\n잠시 후 서명하신 문서가 배부됩니다.\r\n배부된 전자문서를 다운로드 받아 보관하시기 바랍니다.\r\n문서 분실 시 담당자에게 문의 부탁 드립니다.");
                    		}

							if (result.data.signBizType === "0" && result.data.signUserType === "1") {
								alert("전자서명이 완료되었습니다.\r\n서명한 전자문서의 다운로드는 담당자에게 문의 부탁 드립니다.");
                    		}
	                        window.opener.moveContractComplete(result.data.signBizType, result.data.signUserType);
	                        window.close();
	                        window.shouldClose = true;
                    	} else {
                    		alert("전자서명이 완료되었습니다.\r\n잠시 후 서명하신 문서가 배부됩니다.\r\n배부된 전자문서를 다운로드 받아 보관하시기 바랍니다.\r\n문서 분실 시 담당자에게 문의 부탁 드립니다.");
                            window.opener.moveContractComplete();
                            window.close();
                            window.shouldClose = true;
                    	}
                    } else if (!isNull(result.message)) alert(result.message);
                },
                error: function (request, status, error) {
                    $.unblockUI();
                    alert("전자서명 정보를 전송 중 장애가 발생하였습니다. 잠시 후 다시 진행해주시기 바랍니다.");
                }
            });
        }


        //***************					닫기					***************//


        $(".close").click(function () { window.close(); window.shouldClose = true; })

    </script>
    </head>

    <body>
    <input type="hidden" id="id" value=""/>
    <input type="hidden" id="key" value=""/>
    <input type="hidden" id="num" value=""/>
    <div id="signature" class="">
    <div class="popup_bg">
        <ul class="sig">
            <li class="poptit"><span class="text NanumGothic">서명 입력란</span><span
                    class="btn_type close Material_icons">close</span></li>
            <li class="popcon">
                <canvas id="drawCanvas" width="320px" height="170px" style="background-color:powderblue;"></canvas>
            </li>
            <li class="popbtn-group NanumGothic">
                <a class="btn_type btn_del" onclick="fn_reset()">지우기</a>
                <a class="btn_type btn_ok" onclick="fn_save()">확인</a>
                <a class="btn_type btn_close close">닫기</a>
            </li>
        </ul>
    </div>
</div>
</body>
</html>