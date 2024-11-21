<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="./common.jsp" %>
    <title>파일 추가 서명/검증</title>
</head>
<body>

<div class="container">
    <%@include file="header.jsp" %>
    <div class="col-md-9">
        <div><h3>파일 추가 서명/검증</h3></div>

        <div class="panel panel-primary">
            <div class="panel-heading">파일 정보</div>
            <div class="panel-body">
                <form onsubmit="return false;" class="form-horizontal">
                    <div class="form-group">
                        <label for="inputFile" class="control-label col-sm-2">입력 파일</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="text" id="inputFile" placeholder="서명할 원본 파일 전체 경로">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="signedFile" class="control-label col-sm-2">서명 결과 파일</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="text" id="signedFile" placeholder="서명된 파일을 저장할 전체 경로">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="ssn">신원확인 정보</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="ssn" placeholder="인증서 신원확인 정보">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="signedFile" class="control-label col-sm-2"></label>
                        <div class="col-sm-6">
                            <button onclick="signFileTest();">파일 서명</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <div class="panel panel-success">
            <div class="panel-heading">서명결과</div>
            <div class="panel-body">
                <form onsubmit="return false;" class="form-horizontal">
                    <div class="form-group">
                        <label for="aInputFile" class="control-label col-sm-2">서명된 파일</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="text" id="aInputFile" placeholder="추가 서명할 원본 파일 전체 경로">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="aSignedFile" class="control-label col-sm-2">추가 서명된 결과 파일</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="text" id="aSignedFile" placeholder="추가 서명된 파일을 저장할 전체 경로">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="aSignedFile" class="control-label col-sm-2">서명정보</label>
                        <div class="col-sm-10">
                            <div id="signCertInfo" class="scrollable-data"></div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="signedFile" class="control-label col-sm-2"></label>
                        <div class="col-sm-6">
                            <button onclick="addSignInSignedFileTest();">추가 파일 서명</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <div class="panel panel-primary">
            <div class="panel-heading">검증</div>
            <div class="panel-body">
                <form onsubmit="return false;" class="form-horizontal">
                    <div class="form-group">
                        <label for="vInputFile" class="control-label col-sm-2">서명된 파일</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="text" id="vInputFile" placeholder="추가 서명할 원본 파일 전체 경로">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="vResultFile" class="control-label col-sm-2">검증후 원본파일</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="text" id="vResultFile" placeholder="파일 서명검증후 원본파일을 저장할 전체 경로.">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="signedFile" class="control-label col-sm-2"></label>
                        <div class="col-sm-6">
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" id="certValidation"/>결과에 인증서 검증 포함
                                </label>
                            </div>
                            <button onclick="veirfySignedFileTest();">서명 검증하기</button>
                        </div>
                    </div>
                </form>


            </div>
        </div>


        <div class="panel panel-success">
            <div class="panel-heading">서명 검증 결과</div>
            원문 데이터
            <div class="panel-body">
                <form class="form-horizontal" onsubmit="return false;">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="verifyData">원문 데이터 경로</label>
                        <div class="col-sm-10">
                            <input readonly id="verifyData" class="form-control" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="verifyData">서명 정보</label>
                        <div class="col-sm-10">
                            <div id="signerInfo" class="scrollable-data" ></div>
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
        $("#signCertInfo").empty();
        if (res.code == 0) {
            $("#aInputFile").val(res.data.signedFile);
            $("#vInputFile").val(res.data.signedFile);
            $("#signCertInfo").append(objectToTable("",res.data.certInfo));
        }
        else {
            alert("error code = " + res.code + ", message = " + res.errorMessage);
        }
    }
    function add_sign_complete_callback(res) {
        $("#aSignedFile").val("");
        $("#signCertInfo").empty();

        if (res.code == 0) {
            $("#aInputFile").val(res.data.signedFile);
            $("#vInputFile").val(res.data.signedFile);
            $("#signCertInfo").append(objectToTable("",res.data.certInfo));
        }
        else {
            alert("error code = " + res.code + ", message = " + res.errorMessage);
        }
    }
    function addSignInSignedFileTest() {
        var inputFile = $("#aInputFile").val();
        var signedFile = $("#aSignedFile").val();
        if(signedFile == "") {
            signedFile = inputFile + ".sig";
            $("#aSignedFile").val(signedFile);
        }

        var options = {};
        var ssn = $("#ssn").val();
        if(ssn != "")
            options.ssn = ssn;

        saveCookie("ssn",ssn);

        nxTSPKI.addSignInSignedFile(inputFile,signedFile,options,add_sign_complete_callback);
    }
    function veirfySignedFileTest() {
        var inputFile = $("#vInputFile").val();
        var resultFile = $("#vResultFile").val();
        if(resultFile == "") {
            resultFile = inputFile + ".org";
            $("#vResultFile").val(resultFile);
        }

        var certValidation = $("#certValidation").is(":checked");
        nxTSPKI.verifySignedFile(inputFile,resultFile,"",{certValidation:certValidation},verify_complete_callback);
    }
    function signFileTest() {
        var inputFile = $("#inputFile").val();
        var signedFile = $("#signedFile").val();
        if(signedFile == "") {
            signedFile = inputFile + ".nx.sig";
            $("#signedFile").val(signedFile);
        }

        var options = {};
        var ssn = $("#ssn").val();
        if(ssn != "")
            options.ssn = ssn;

        saveCookie("ssn",ssn);

        nxTSPKI.signFile(inputFile,signedFile,options,sign_complete_callback);
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
