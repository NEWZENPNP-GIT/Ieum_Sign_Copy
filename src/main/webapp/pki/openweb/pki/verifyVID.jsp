<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="./common.jsp" %>
    <title>VID 검사</title>
</head>
<body>
<div class="container" >
    <%@include file="header.jsp" %>
    <div class="col-md-9">
        <h2>VID 검사</h2>

        <div class="panel panel-primary">
            <div class="panel-heading"> 데이터입력 </div>
            <div class="panel-body">
                <form class="form-horizontal" onsubmit="return false;">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" >SSN</label>
                        <div class="col-sm-8 ">
                            <input class="form-control" id="ssn" placeholder="주민등록 번호">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" >Cert</label>
                        <div class="col-sm-8 ">
                            <textarea class="form-control" rows="5" id="cert" > </textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" >Random</label>
                        <div class="col-sm-8 ">
                            <input class="form-control" id="r" placeholder="랜덤">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" ></label>
                        <div>
                            <button class="" onclick="verifyVIDTest();">verifyVID</button>
                            <button class="" onclick="verifyVID2Test();">verifyVID2</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="panel panel-info">
            <div class="panel-heading"> 검증 결과</div>
            <div class="panel-body">
                <h5>검증결과</h5>
                <input id="verifyResult">
                <h5>인증서정보</h5>
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
            $("#verifyResult").val((res.data.result == true) ? "성공" : "실패");
            $("#verifyCertInfo").append(objectToTable("",res.data.certInfo));
        }
        else {
            alert(res.errorMessage);
        }
    }

    function verifyVIDTest() {
        $("#verifyCertInfo").empty();
        var ssn = $("#ssn").val();
        nxTSPKI.verifyVID(ssn,{},verify_complete_callback);
    }
    function verifyVID2Test() {
        $("#verifyCertInfo").empty();
        var ssn = $("#ssn").val();
        var cert = $("#cert").val();
        var r = $("#r").val();
        nxTSPKI.verifyVID2(cert,ssn,r,{},verify_complete_callback);
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
