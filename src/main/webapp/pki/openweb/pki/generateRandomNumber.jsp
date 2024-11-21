<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="./common.jsp" %>
    <title>랜덤값 생성</title>
</head>
<body>

<div class="container">
    <%@include file="header.jsp" %>
    <div class="col-md-9">
        <div><h3>랜덤값 생성</h3></div>

        <div class="panel panel-primary">
            <div class="panel-heading">랜덤값 생성</div>
            <div class="panel-body">
                <form onsubmit="return false;" class="form-horizontal">
                    <div class="form-group">
                        <label for="randomLength" class="col-sm-2 control-label">랜덤값 길이</label>
                        <div class="col-sm-3">
                            <input class="form-control" type="number" name="randomLength" id="randomLength" value="10">
                        </div>
                        <div class="col-sm-3">
                            <button onclick="genRandomNumberTest();" >랜덤값 생성</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <div class="panel panel-success">
            <div class="panel-heading">랜덤값</div>
            <div class="panel-body">
                <textarea id="randomNumber" rows="5" ></textarea>
            </div>
        </div>

        <hr>
        <%@include file="footer.jsp" %>

    </div>
</div>



<script>
    function generate_complete_callback(res) {
        if (res.code == 0) {
            $("#randomNumber").val(res.data);
        }
        else {
            alert("error code = " + res.code + ", message = " + res.errorMessage);
        }
    }

    function genRandomNumberTest() {
        var randomLength = $("#randomLength").val();
        $("#randomNumber").val("");
        nxTSPKI.generateRandomNumber(randomLength, generate_complete_callback);
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
