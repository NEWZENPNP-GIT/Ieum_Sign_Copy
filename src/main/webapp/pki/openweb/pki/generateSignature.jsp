<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>
    <%@include file="./common.jsp" %>
    <title>서명 생성/검증</title>
</head>
<body>

<div class="container">
    <%@include file="header.jsp" %>
    <div class="col-md-9">
        <div><h3>서명 생성/검증</h3></div>
        <div class="panel panel-primary">
            <div class="panel-heading">서명 데이터 생성</div>
            <div class="panel-body">
                <form onsubmit="return false;" class="form-horizontal">
                    <div class="form-group">
                        <label for="dataType" class="control-label col-sm-3">서명값 생성 타입</label>
                        <div class="col-sm-4">
                            <select class="form-control" id="dataType" name="dataType">
                                <option value="0">(구)데이터 서명 생성</option>
                                <option value="1">(구)파일 서명 생성</option>
                                <option value="101">데이터 서명 생성</option>
                                <option value="103">파일 서명 생성</option>
                                <option value="102">Base64Encoding 된 데이터 서명 생성</option>
                                <option value="104">Hash 된 데이터 서명 생성</option>
							</select>
                        </div>
                    </div>
                    <div class="form-group" id="dataFileGroup">
                        <label class="control-label col-sm-3" for="dataFile">파일 경로</label>
                        <div class="col-sm-8">
                            <input class="form-control" name="dataFile" id="dataFile" placeholder="서명값을 생성할 파일의 전체 경로">
                        </div>
                    </div>
                    <div class="form-group" id="dataGroup">
                        <label class="control-label col-sm-3" for="data">데이터</label>
                        <div class="col-sm-8">
                            <textarea cols="80" rows="3" class="form-control" name="data" id="data" placeholder="서명값을 생성할 데이터 입력.">서명생성용 샘플 데이터</textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-3" for="ssn">신원확인 정보</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="ssn" placeholder="인증서 신원확인 정보">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-3"></label>
                        <div class="col-sm-3">
                           <button onclick="generateSignatureTest();"> 서명 데이터 생성</button>
                        </div>
                    </div>
                </form>

            </div>
        </div>

        <div class="panel panel-success">
            <div class="panel-heading">서명 생성 결과</div>
            <div class="panel-body">
                <b>서명값</b>
                <textarea id="sigData" rows="5" ></textarea>
                <b>인증서 정보</b>
                <div id="sigCertInfo" class="scrollable-data"></div>

                <div id="sigCert" ></div>
                <div>
                    <button onclick="verifySigDataTest();">서명 데이터 검증.</button>
                </div>
            </div>
        </div>

        <div class="panel panel-success">
            <div class="panel-heading">서명 데이터 검증결과.</div>
            <div class="panel-body">
                <textarea id="verifyResult" rows="1" style="width:100%;" ></textarea>
            </div>
            <h5>인증서 정보</h5>
            <div class="scrollable-data" id="verifyCertInfo"></div>
        </div>

        <hr>
        <%@include file="footer.jsp" %>

    </div>
</div>



<script>
    function verify_complete_callback(res) {
        if (res.code == 0) {
            $("#verifyResult").val("서명검증 성공");
            //$("#verifyCertInfo").append(objectToTable("",res.data.certInfo));
        }
        else {
            alert("error code = " + res.code + ", message = " + res.errorMessage);
        }
    }
    function generate_signature_complete_callback(res) {
        if (res.code == 0) {
            $("#sigData").val(res.data.signature);
            $("#sigCert").text(res.data.certInfo.cert);
            $("#sigCert").hide();
            $("#sigCertInfo").append(objectToTable("",res.data.certInfo));
        }
        else {
            alert("error code = " + res.code + ", message = " + res.errorMessage);
        }
    }

    function generateSignatureTest() {
        $("#sigCertInfo").empty();
        $("#sigData").val("");
        $("#sigCert").text("");


        var dataType = $("#dataType").val();
        var data = "";
        if(dataType == 1 || dataType == 103)
            data = $("#dataFile").val();
        else
            data = $("#data").val();

        var options = {};
        var ssn = $("#ssn").val();
        if(ssn != "")
            options.ssn = ssn;

        saveCookie("ssn",ssn);


        nxTSPKI.generateSignature(dataType,data, options, generate_signature_complete_callback);
    }

    function verifySigDataTest() {
        $("#verifyCertInfo").empty();
        var dataType = $("#dataType").val();
        var data = "";
        if(dataType == 1 || dataType == 103)
            data = $("#dataFile").val();
        else
            data = $("#data").val();

        var sigData = $("#sigData").val();
        var signerCert = $("#sigCert").text();

        $("#verifyResult").val("");


        nxTSPKI.verifySignature(dataType,data,sigData,signerCert, verify_complete_callback);
    }

    $(document).ready(function(){
        $("#dataFileGroup").hide();
        $("#dataType").change(function() {
            var dataType = $("#dataType").val();
            if(dataType == 1 || dataType == 103) {
                $("#dataFileGroup").show();
                $("#dataGroup").hide();
            }
			else {
                $("#dataFileGroup").hide();
                $("#dataGroup").show();
			}
        });
    });
	
	//초기화 함수 필수
    $(document).ready(function(){
        nxTSPKI.onInit(function(){ 
			//nxTSPKI.init 함수 완료 후 실행해야 하는 함수나 기능 작성 위치
			//alert("Init 완료");
		});

		nxTSPKI.init(true);
    });
	
</script>


</body>
</html>
