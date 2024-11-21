<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="./common.jsp" %>
    <title>해시 파일</title>
</head>
<body>

<div class="container">
    <%@include file="header.jsp" %>
    <div class="col-md-9">
        <div><h3>해시 데이터</h3></div>

        <div class="panel panel-primary">
            <div class="panel-heading">입력 데이터</div>
            <div class="panel-body">
            <form class="form-horizontal " onsubmit="return false;">
                <div class="form-group">
                    <label for="algorithm" class="col-sm-2 control-label">알고리즘</label>
                    <div class="col-sm-3">
                        <select id="algorithm" class="form-control" name="algorithm"></select>
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputFilePath" class="col-sm-2 control-label">해시할 파일의 경로</label>
                    <div class="col-sm-6">
                        <input class="form-control" id="inputFilePath" />
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2">
                        <button onclick="hashDataTest();">해시</button>
                    </div>
                </div>
            </form>
        </div>
        </div>

        <div class="panel panel-success">
            <div class="panel-heading">해시결과</div>
            <div class="panel-body">
                <textarea id="hashData" rows="5" cols="80"></textarea>
            </div>
        </div>

        <hr>
        <%@include file="footer.jsp" %>

    </div>
</div>



<script>
    function hash_complete_callback(res) {
        if (res.code == 0) {
            $("#hashData").val(res.data);
        }
        else {
            alert("error code = " + res.code + ", message = " + res.errorMessage);
        }
    }

    function hashDataTest() {
        $("#hashData").val("");

        var dataFile = $("#inputFilePath").val();
        var alg = $("#algorithm").val();

        nxTSPKI.hashFile(alg,dataFile, hash_complete_callback);
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
