<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="./common.jsp" %>
    <title>Base64 인코딩/디코딩</title>
</head>
<body>
<div class="container" >
    <%@include file="header.jsp" %>
    <div class="col-md-9">
        <h2>데이터 base64Encode / base64Decode</h2>

        <div class="panel panel-primary">
            <div class="panel-heading"> 인코딩할 데이터 </div>
            <div class="panel-body">
                <textarea rows="5" cols="80" id="plainData">sample encoding data</textarea>
                <button onclick="encTest();"> 인코딩 </button>
            </div>
        </div>
        <div class="panel panel-info">
            <div class="panel-heading"> 인코딩결과 데이터</div>
            <div class="panel-body">
                <textarea rows="5" cols="80" id="encodedData"></textarea><br/>
                <button onclick="decTest();"> 디코딩 </button>
            </div>
        </div>
        <div class="panel panel-success">
            <div class="panel-heading"> 디코딩결과 데이터</div>
            <div class="panel-body">
                <textarea rows="5" cols="80" id="decodedData"></textarea><br/>
            </div>
        </div>
        <hr/>
        <%@include file="footer.jsp"%>
    </div>

</div>
<script>
    function enc_complete_callback(res) {
        if(res.code == 0) {
            $("#encodedData").val(res.data);
        }
        else {
            alert(res.errorMessage);
        }
    }
    function dec_complete_callback(res){
        if(res.code == 0)
            $("#decodedData").val(res.data);
        else
            alert(res.errorMessage);
    }

    function encTest() {
        var data  = $("#plainData").val();
        nxTSPKI.base64EncodeData(data,enc_complete_callback);
    }
    function decTest() {
        var data  = $("#encodedData").val();
        nxTSPKI.base64DecodeData(data,dec_complete_callback);
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
