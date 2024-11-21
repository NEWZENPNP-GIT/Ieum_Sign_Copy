
    <!--****************************************************************************************************************
    * 상기 프로그램에 대한 저작권을 포함한 직접재산권은 NewZen P&P에 있으며 NewZen P&P가 명시적으로 허용하지 않은
    * 사용, 복사, 변경, 제3자에 의한 공개 배포는 엄격히 금지되며 NewZen P&P의 지적재산권 침해에 해당합니다.
    ********************************************************************************************************************
    --------------------------------------------------------------------------------------------------------------------
    * 회사       : 뉴젠피앤피
    * 화면명     : 전자서명 서명 팝업(거래처)
    * 화면설명   : 전자문서 > 서명
    * 작성자     : 이정훈
    * 생성일     : 2023.11.08
    --------------------------------------------------------------------------------------------------------------------
    * 수정자     :
    * 수정일     :
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
    <link rel="stylesheet" type="text/css" href="/css/contract_write.css">

    <script type="text/javascript" src="/js/jquery.min.js"></script>
    <script type="text/javascript" src="/js/common.js"></script>
    <script src="/js/jquery.blockUI.js"></script>

    <!-- 공인인증서 관련 -->
    <script type="text/javascript" src="/js/nxts/nxts.min.js"></script>
    <script type="text/javascript" src="/js/nxts/nxtspki_config.js"></script>
    <script type="text/javascript" src="/js/nxts/nxtspki.js"></script>
    <script>

        //***************					윈도우 로딩					***************//


        $(document).ready(function () {

            param = getParams();
            $("#id").val(param.id);
            $("#key").val(param.key);
            $("#num").val(param.num);

            $("#attachSign").hide();

            $("#ck1_1").change(function () {
                if($(this).prop('checked')) {
                    $("#signFile").val("");
                    $("#preview").attr('src', '/images/sign/digitsign_1.png');
                    $("#fileName").text("");
                }
            });
        });


        //***************					파일 체크					***************//


        function fileCheck(obj) {

            var pathpoint = obj.value.lastIndexOf('.');
            var filepoint = obj.value.substring(pathpoint+1, obj.length);
            var filetype  = filepoint.toLowerCase();

            if (filetype === 'png') {
                var file     = obj.files[0];
                var fileName = obj.files[0].name;
                var reader   = new FileReader();

                $("#fileName").text(fileName);
                reader.onload = function(e) { $("#preview").attr("src", e.target.result); }
                reader.readAsDataURL(file);
            } else { alert("파일확장자는 png파일만 가능합니다."); return false; }
        }


        //***************					서명 등록					***************//


        function fn_save() {

            var formData = new FormData();
            var maxSize  = 5 * 1024 * 1024; // 5MB
            var fileSize = 0;

            // 이미지 업로드
            var imageFile = document.getElementsByName('signFile');
            if ($("#ck1_2").prop('checked')) {
                for (var i=0; i<imageFile.length; i++) {
                    if (imageFile[i]['files'][0] !== undefined && imageFile[i]['files'][0] != null) {
                        if (!checkFileExt(imageFile[i]['files'][0].name, 'png')) {
                            alert('파일확장자는 png파일만 가능합니다.');
                            return;
                        }
                        fileSize += imageFile[i]['files'][0].size;

                        if (checkSpecial(imageFile[i]['files'][0].name)) {
                            alert('파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.');
                            return;
                        }
                        formData.append('imageStamp' + i, imageFile[i]['files'][0]);
                    } else { alert('서명을 첨부하여 주시기 바랍니다.'); return; }
                }

                if (fileSize > maxSize) { alert('파일은 ' + (maxSize / 1024 / 1024) + 'MB 이하의 파일만 첨부 가능합니다'); return false; }

                if (!confirm("서명 하시겠습니까?")) return;

                opener.fn_customer_sign(formData);
                window.close();
            } else {
                if (!confirm("공인인증 후 서명이 완료됩니다.\n서명 하시겠습니까?")) return;
                opener.callSignData();
                window.close();
            }
        }


        //***************					전자서명 체크 (기본 직인사용 )					***************//


        function fn_checkSignType_Y() {
            if ($("#ck1_1").prop('checked')) $("#ck1_2").prop('checked',false);

            $("#attachSign").hide();
        }


        //***************					서명첨부 체크 ( 자사서명 업로드 후 사용)					***************//


        function fn_checkSignType_N(){
            if ($("#ck1_2").prop('checked')) $("#ck1_1").prop('checked',false);
            $("#attachSign").show();
        }


        //***************					자사서명 업로드					***************//


        function fn_fileUpload() { if($("#ck1_2").prop('checked')) $("#signFile").click(); }


        //***************					창 닫기					***************//


        $(".close").click(function () { window.close(); window.shouldClose = true; })

    </script>
    </head>
    <body>

    <!-- *********************************************  전자 근로 계약  *********************************************  -->

    <div id="contract_popup" style="height: 300px;">
        <div class="container">
            <div id="popup_tit">
                <div class="tit_icon"></div>
                <div class="tit_text col_skyblue NanumGothic">전자서명</div>
            </div>
            <div class="contents NanumGothic" style="height: 250px;">
                <div class="input_wrap">
                    <dl class="boxRow ckStyle NanumGothic">
                        <dt><label class="label_txt NanumGothic">서명종류 선택</label></dt>
                        <dd>
                            <div class="chBox">
                                <input type="checkbox" id="ck1_1" name="signType" onclick = "fn_checkSignType_Y()" checked>
                                <label for="ck1_1">전자서명</label>
                            </div>
                            <div class="chBox">
                                <input type="checkbox" id="ck1_2" name="signType" onclick = "fn_checkSignType_N()">
                                <label for="ck1_2">서명첨부</label>
                            </div>
                        </dd>
                    </dl>
                    <dl id="attachSign" class="boxRow default_bx NanumGothic">
                        <input type="file" id="signFile" name="signFile" style="display:none;" accept="image/png" onchange="fileCheck(this)"/>
                        <dt>
                            <label class="label_txt NanumGothic">자사서명 업로드</label>
                        </dt>
                        <dd class="column2">
                            <a class="btn_type type1" href="javascript:fn_fileUpload();">파일선택</a>
                            <div class="default_box down_file">
                                <span class="file_icon"></span> <!-- icon_excel -->
                                <span class="file_name" id="fileName"></span>
                            </div>
                        </dd>
                    </dl>
                    <dl class="boxRow ckStyle NanumGothic">
                        <dt>
                            <label class="label_txt NanumGothic">서명이미지</label>
                        </dt>
                        <dd>
                            <div style="width: calc(100% - 2px); height: 150px; border: 1px solid #bfbfbf; text-align: center;">
                                <img id="preview" src="/images/sign/digitsign_1.png" style="width: 150px; height: 150px;">
                            </div>
                        </dd>
                    </dl>
                </div>
            </div>
            <div class="btn_group">
                <a class="btn_type type1 NanumGothic" onclick="fn_save();">확인</a>
                <a class="btn_type type3 NanumGothic" onclick="window.close();">취소하기</a>
            </div>
        </div>
    </div>
    </body>
    </html>