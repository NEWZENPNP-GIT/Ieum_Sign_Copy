<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="./common.jsp" %>
    <title>파일 서명/검증 테스트</title>
</head>
<body>
<div class="container">
    <%@include file="header.jsp" %>
    <div class="col-md-9">
        <h2>파일 전사서명</h2>
        <div class="panel panel-primary">
            <div class="panel-heading">파일 전자 서명 </div>
            <div class="panel-body">
                <form class="form-horizontal" onsubmit="return false;">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="dataFile">원본 파일</label>
                        <div class="col-sm-6">
                            <input class="form-control" name="dataFile" id="dataFile" type="text" value="" placeholder="서명할 파일의 전체 경로"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="signedDataFile">저장할 파일</label>
                        <div class="col-sm-6">
                            <input class="form-control" name="signedDataFile" id="signedDataFile" type="text" value="" placeholder="서명한 파일을 저장할 전체 경로"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="ssn">신원확인 정보</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="ssn" placeholder="인증서 신원확인 정보">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" ></label>
                        <div>
                            <button onclick="signFileTest();"> 파일 서명 </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="panel panel-success">
            <div class="panel-heading">서명결과</div>
            <div class="panel-body">
                <h5>서명결과</h5>
                <textarea id="signedData" rows="2" cols="80"></textarea>
                <h5>서명 인증서정보</h5>
                <div id="signCertInfo" class="scrollable-data"></div>
            </div>
        </div>
        <div class="panel panel-primary">
            <div class="panel-heading">파일 전자 서명검증 </div>
            <div class="panel-body">
                <form class="form-horizontal" onsubmit="return false;">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="vSignedDataFile">서명한 파일</label>
                        <div class="col-sm-9">
                            <input class="form-control" name="vSignedDataFile" id="vSignedDataFile" type="text" value="" placeholder="서명한 파일의 전체경로"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="vDataFile">원본 저장 파일</label>
                        <div class="col-sm-9">
                            <input class="form-control" name="vDataFile" id="vDataFile" type="text" value="" placeholder="서명한 파일에 원본데이터가 있는경우 원본 데이터를 저장할 파일"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="vContentFile">원본 파일</label>
                        <div class="col-sm-9">
                            <input class="form-control" name="vContentFile" id="vContentFile" type="text" value="" placeholder="서명파일에 원본데이터가 포함되지 않은경우 원본데이터 파일의 경로"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="checkbox">
                            <div class="col-sm-2"></div>
                            <label>
                                <input type="checkbox" id="certValidation"/>결과에 인증서 검증 포함
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" ></label>
                        <div>
                            <button onclick="verifySignedFileTest();">서명 파일 검증.</button>
                        </div>

                    </div>
                </form>
            </div>
        </div>
        <div class="panel panel-success">
            <div class="panel-heading">서명 검증 결과</div>
            <div class="panel-body">
                결과
                <textarea id="verifyResult" rows="3" cols="80"></textarea>
                서명 정보
                <div id="verifyCertInfo" class="scrollable-data"></div>
            </div>
        </div>

        <hr/>

        <%@include file="footer.jsp"%>
    </div>
</div>


<script>
    function verify_complete_callback(res) {
        if(res.code == 0) {
            $("#verifyResult").val(res.data.content);
            for(var i=0;i<res.data.signerInfo.length;i++) {
                var caption = (i+1) + ' 번째 서명정보';
                $("#verifyCertInfo").append(objectToTable(caption,res.data.signerInfo[i]));
            }
        }
        else {
            alert(res.errorMessage);
        }
    }
    function sign_complete_callback(res){
        if(res.code == 0) {
            $("#signedData").val(res.data.signedFile);
            $("#vSignedDataFile").val(res.data.signedFile);
            $("#vDataFile").val($("#dataFile").val()+".org");
            $("#signCertInfo").append(objectToTable("",res.data.certInfo));
        }
        else
            alert(res.errorMessage);
    }

    function signFileTest() {
        var options = {};
        $("#signCertInfo").empty();

        var dataFile = $("#dataFile").val();
        var signedDataFile = $("#signedDataFile").val();

        if(signedDataFile == "") {
            signedDataFile = dataFile + ".nx.sig";
            $("#signedDataFile").val(signedDataFile);
        }

        var ssn = $("#ssn").val();
        if(ssn != "") {
            options.ssn = ssn;
        }

        saveCookie("ssn",ssn);

        nxTSPKI.signFile(dataFile,signedDataFile,options,sign_complete_callback);
    }

    function verifySignedFileTest() {
        $("#verifyCertInfo").empty();
        var certValidation = $("#certValidation").is(":checked");
        var signedDataFile = $("#vSignedDataFile").val();
        var dataFile = $("#vDataFile").val();
        var contentFile = $("#vContentFile").val();
        nxTSPKI.verifySignedFile(signedDataFile,dataFile,contentFile,{"certValidation":certValidation},verify_complete_callback);
    }
	
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
