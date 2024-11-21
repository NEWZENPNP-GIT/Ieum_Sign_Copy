
    <!--****************************************************************************************************************
    * 상기 프로그램에 대한 저작권을 포함한 직접재산권은 NewZen P&P에 있으며 NewZen P&P가 명시적으로 허용하지 않은
    * 사용, 복사, 변경, 제3자에 의한 공개 배포는 엄격히 금지되며 NewZen P&P의 지적재산권 침해에 해당합니다.
    ********************************************************************************************************************
    --------------------------------------------------------------------------------------------------------------------
    * 회사       : 뉴젠피앤피
    * 화면명     : 전자문서
    * 화면설명   : 전자문서 서명 팝업
    * 작성자     :
    * 생성일     :
    --------------------------------------------------------------------------------------------------------------------
    * 수정자     : 이정훈
    * 수정일     : 2023.10.31
    * 수정내용   :
    ------------------------------------------------------------------------------------------------------------------->


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
    <script type="text/javascript" src="/js/jquery.blockUI.js"></script>

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


        function fn_reset() {
            initCanvas();
            History.removeAllItem();
            actionData = null;
        }


        //***************					저장					***************//


        function fn_save() {

            $.blockUI();

            var initScale = 0.5;
            var line      = "";
            var count     = 0;
            var linedata  = History.getItems();

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

            $.unblockUI();

            if (count < 5) { alert("서명에 대한 정보가 부족합니다."); return; }

            opener.fn_sign(line);
            window.close();
        }


        //***************					닫기					***************//


        $(".close").click(function () { window.close(); window.shouldClose = true; })

    </script>
    </head>

    <body>
    <input type="hidden" id="id" value=""/>
    <input type="hidden" id="key" value=""/>
    <input type="hidden" id="num" value=""/>
    <input type="file" id="attachFile" name="attachFile" style="visibility: hidden; position: absolute; left: -9999px; top: -9999px;">
    <div id="signature" class="">
        <div class="popup_bg">
            <ul class="sig">
                <li class="poptit">
                    <span class="text NanumGothic">서명 입력란</span>
                    <span class="btn_type close Material_icons">close</span>
                </li>
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