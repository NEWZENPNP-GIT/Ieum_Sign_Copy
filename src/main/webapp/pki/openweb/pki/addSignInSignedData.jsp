<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="./common.jsp" %>
    <title>추가 서명/검증</title>
</head>
<body>

<div class="container">
    <%@include file="header.jsp" %>
    <div class="col-md-9">
        <div><h3>추가 서명/검증</h3></div>

        <div class="panel panel-primary">
            <div class="panel-heading">서명 데이터</div>
            <div class="panel-body">
                <form onsubmit="return false;" class="form-horizontal">
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="inputData">데이터</label>
                        <div class="col-sm-10">
                            <textarea class="form-control" rows="3" id="inputData">test data</textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="ssn">신원확인 정보</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="ssn" placeholder="인증서 신원확인 정보">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="ssn"></label>
                        <div class="col-sm-10">
                            <button class="btn-primary" id="signButton" onclick="signDataTest();">서명하기</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <div class="panel panel-info">
            <div class="panel-heading">서명 결과</div>
            <div class="panel-body">
                <form onsubmit="return false;" class="form-horizontal">
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="signedData">서명결과</label>
                        <div class="col-sm-10">
                            <textarea readonly class="form-control" id="signedData" rows="5" ></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="signedData">서명자 정보</label>
                        <div class="col-sm-10">
                            <div class="scrollable-data" id="signSignerInfo"></div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2" ></label>
                        <div class="col-sm-10">
                            <button class="btn-primary" id="_signButton" onclick="addSignInSignedDataTest();">추가서명하기</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="panel panel-primary">
            <div class="panel-heading">검증</div>
            <div class="panel-body">
                <button class="btn-info" id="verifyResult" onclick="veirfySignedDataTest();">서명 검증하기</button>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" id="certValidation"/>결과에 인증서 검증 포함
                    </label>
                </div>
            </div>
        </div>

        <div class="panel panel-success">
            <div class="panel-heading">서명 검증 결과</div>
            <div class="panel-body">
                <form onsubmit="return false;" class="form-horizontal">
                    <div class="form-group">
                        <label for="verifyData" class="col-sm-2 control-label">원문 데이터</label>
                        <div class="col-sm-10">
                            <textarea readonly id="verifyData" rows="3" class="form-control" ></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="verifyData" class="col-sm-2 control-label">서명 정보</label>
                        <div class="col-sm-10">
                            <div id="signerInfo" class="scrollable-data " ></div>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <hr>
        <%@include file="footer.jsp" %>

    </div>
</div>



<script>
    function verify_complete_callback(res) {
        if (res.code == 0) {
            $("#verifyData").val(res.data.content);
            for(var i=0;i<res.data.signerInfo.length;i++) {
                var caption = (i+1) + '번째 서명정보';
                $("#signerInfo").append(objectToTable(caption,res.data.signerInfo[i]));
            }
        }
        else {
            alert("error code = " + res.code + ", message = " + res.errorMessage);
        }
    }
    function sign_complete_callback(res) {
        if (res.code == 0) {
            $("#signedData").val(res.data.signedData);
            $("#signSignerInfo").empty().append(objectToTable("",res.data.certInfo));
        }
        else {
            alert("error code = " + res.code + ", message = " + res.errorMessage);
        }
    }

    function veirfySignedDataTest() {
        var signedData = $("#signedData").val();
        var certValidation = $("#certValidation").is(":checked");

        $("#signerInfo").html("");
        nxTSPKI.verifySignedData(signedData,{"certValidation": certValidation},verify_complete_callback);
    }

    function addSignInSignedDataTest() {
        var signedData = $("#signedData").val();

        var options = {};
        var ssn = $("#ssn").val();
        if(ssn != "")
            options.ssn = ssn;

        saveCookie("ssn",ssn);

        nxTSPKI.addSignInSignedData(signedData,options, sign_complete_callback);
    }

    function signDataTest() {
        var data = $("#inputData").val();

        var options = {};
        var ssn = $("#ssn").val();
        if(ssn != "")
            options.ssn = ssn;

        saveCookie("ssn",ssn);

        nxTSPKI.signData(data,options,sign_complete_callback);
    }

    $(document).ready(function(){
        var algs = ["MD5","RIPEMD160","SHA1","HAS160","SHA256","SHA384","SHA512"];

        for(var a=0;a<algs.length;a++) {
                $("#algorithm").append('<option value="'+algs[a]+'">'+algs[a]+'</option>');
        }
        $("#algorithm > option[value=SHA256]").attr("selected","true");
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
