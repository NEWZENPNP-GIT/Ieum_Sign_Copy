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
                if ($(this).prop('checked')) {
                    $("#signFile").val("");
                    $("#preview").attr('src', '/images/sign/digitsign_1.png');
                    $("#fileName").text("");
                }
            });
        });

        function fileCheck(obj) {
            var pathpoint = obj.value.lastIndexOf('.');
            var filepoint = obj.value.substring(pathpoint+1, obj.length);
            var filetype  = filepoint.toLowerCase();

            if (filetype === 'png') {
                var file     = obj.files[0];
                var fileName = obj.files[0].name;
                $("#fileName").text(fileName);

                var reader    = new FileReader();
                reader.onload = function(e) { $("#preview").attr("src", e.target.result); }
                reader.readAsDataURL(file);
            } else { alert("파일확장자는 png파일만 가능합니다."); return false; }
        }

        function fn_save() {

            var formData = new FormData();

            if ($("#ck1_2").prop('checked')) {

                var maxSize   = 5 * 1024 * 1024; // 5MB
                var fileSize  = 0;
                var imageFile = document.getElementsByName('signFile');
                var url       = rootPath + "contract/signContractCustomer.do";

                for (var i = 0; i < imageFile.length; i++) {
                    if (imageFile[i]['files'][0] !== undefined && imageFile[i]['files'][0] != null) {

                        if (!checkFileExt(imageFile[i]['files'][0].name, 'png')) { alert('파일확장자는 png파일만 가능합니다.'); return; }

                        fileSize += imageFile[i]['files'][0].size;

                        if (checkSpecial(imageFile[i]['files'][0].name)) { alert('파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.'); return; }
                        formData.append('imageStamp' + i, imageFile[i]['files'][0]);
                    } else { alert('서명을 첨부하여 주시기 바랍니다.'); return; }
                }
                if (fileSize > maxSize) { alert('파일은 ' + (maxSize / 1024 / 1024) + 'MB 이하의 파일만 첨부 가능합니다'); return false; }

                if (!confirm("서명 하시겠습니까?")) return;

                formData.append('id', $("#id").val());
                formData.append('key', $("#key").val());
                formData.append('num', '1');

                $.blockUI();

                $.ajax({
                    url: url,
                    data: formData,
                    processData: false,
                    contentType: false,
                    type: 'POST',
                    success: function (result) {
                        $.unblockUI();
                        if (result.success === true) {
                            if (result.data !== undefined) { //양식이 존재할 경우
                                alert("전자서명이 완료되었습니다.\r\n잠시 후 서명하신 문서가 배부됩니다.\r\n배부된 전자문서를 다운로드 받아 보관하시기 바랍니다.\r\n문서 분실 시 담당자에게 문의 부탁 드립니다.");
                                window.close();
                                window.opener.moveContractComplete(result.data.signBizType, result.data.signUserType);
                            } else {
                                alert("전자서명이 완료되었습니다.\r\n잠시 후 서명하신 문서가 배부됩니다.\r\n배부된 전자문서를 다운로드 받아 보관하시기 바랍니다.\r\n문서 분실 시 담당자에게 문의 부탁 드립니다.");
                                window.close();
                                window.opener.moveContractComplete();
                            }
                        } else { if (!isNull(result.message)) alert(result.message); }
                    },
                    error: function (request, status, error) {
                        $.unblockUI();
                        alert("전자서명 정보를 전송 중 장애가 발생하였습니다. 잠시 후 다시 진행해주시기 바랍니다.");
                    }
                });
            } else {
                var options  = {};
                var signdata = [];
                if (!confirm("공인인증 후 서명이 완료됩니다.\n서명 하시겠습니까?")) return;

                signdata.push($("#id").val());
                nxTSPKI.multiSignData(signdata, options, sign_complete_callback);
            }
        }

        function fn_checkSignType_Y() {
            if ($("#ck1_1").prop('checked')) $("#ck1_2").prop('checked',false);
            $("#attachSign").hide();
        }

        function fn_checkSignType_N() {
            if ($("#ck1_2").prop('checked')) $("#ck1_1").prop('checked',false);
            $("#attachSign").show();
        }

        function fn_fileUpload() { if($("#ck1_2").prop('checked')) $("#signFile").click(); }

        function sign_complete_callback(res) {

            if (res.code === 0) {

                var url      = rootPath + "contract/signContractCustomer.do";
                var formData = new FormData();

                formData.append('id', $("#id").val());
                formData.append('key', $("#key").val());
                formData.append('num', '1');

                $.blockUI();

                $.ajax({
                    url: url,
                    data: formData,
                    processData: false,
                    contentType: false,
                    type: 'POST',
                    success: function (result) {
                        $.unblockUI();
                        if (result.success === true) {
                            if (result.data !== undefined) { //양식이 존재할 경우
                                alert("전자서명이 완료되었습니다.\r\n잠시 후 서명하신 문서가 배부됩니다.\r\n배부된 전자문서를 다운로드 받아 보관하시기 바랍니다.\r\n문서 분실 시 담당자에게 문의 부탁 드립니다.");
                                window.close();
                                window.opener.moveContractComplete(result.data.signBizType, result.data.signUserType);
                            } else {
                                alert("전자서명이 완료되었습니다.\r\n잠시 후 서명하신 문서가 배부됩니다.\r\n배부된 전자문서를 다운로드 받아 보관하시기 바랍니다.\r\n문서 분실 시 담당자에게 문의 부탁 드립니다.");
                                window.close();
                                window.opener.moveContractComplete();
                            }
                        } else if (!isNull(result.message)) alert(result.message);
                    },
                    error: function (request, status, error) {
                        $.unblockUI();
                        alert("전자서명 정보를 전송 중 장애가 발생하였습니다. 잠시 후 다시 진행해주시기 바랍니다.");
                    }
                });
            } else { alert(res.errorMessage); }
        }

    </script>
    </head>

    <body>
    <input type="hidden" id="id" value=""/>
    <input type="hidden" id="key" value=""/>
    <input type="hidden" id="num" value=""/>
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
                        <div class="chBox"><input type="checkbox" id="ck1_1" name="signType" onclick = "fn_checkSignType_Y()" checked><label for="ck1_1">전자서명</label></div>
                        <div class="chBox"><input type="checkbox" id="ck1_2" name="signType" onclick = "fn_checkSignType_N()"><label for="ck1_2">서명첨부</label></div>
                    </dd>
                </dl>
                <dl id="attachSign" class="boxRow default_bx NanumGothic">
                    <input type="file" id="signFile" name="signFile" style="display:none;" accept="image/png" onchange="fileCheck(this)"/>
                    <dt><label class="label_txt NanumGothic">자사서명 업로드</label></dt>
                    <dd class="column2">
                        <a class="btn_type type1" href="javascript:fn_fileUpload();">파일선택</a>
                        <div class="default_box down_file">
                            <span class="file_icon"></span>
                            <span class="file_name" id="fileName"></span>
                        </div>
                    </dd>
                </dl>
                <dl class="boxRow ckStyle NanumGothic">
                    <dt><label class="label_txt NanumGothic">서명이미지</label></dt>
                    <dd>
                        <div style="width: calc(100% - 2px); height: 150px; border: 1px solid #bfbfbf; text-align: center;"><img id="preview" src="/images/sign/digitsign_1.png" style="width: 150px; height: 150px;"></div>
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