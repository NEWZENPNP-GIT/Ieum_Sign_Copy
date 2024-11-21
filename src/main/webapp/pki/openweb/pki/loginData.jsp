<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="./common.jsp" %>
    <title>로그인 데이터</title>
</head>
<body>

<div class="container">
    <%@include file="header.jsp" %>
    <div class="col-md-9">
        <div><h3>로그인 데이터</h3></div>

        <div class="panel panel-primary">
            <div class="panel-heading">사용자 정보</div>
            <div class="panel-body">
            <form class="form-horizontal " onsubmit="return false;">
                <div class="form-group">
                    <label for="sessionId" class="col-sm-2 control-label">SessionID</label>
                    <div class="col-sm-4">
                        <input type="text" id="sessionId" class="form-control" name="sessionId"></input>
                    </div>
                </div>
                <div class="form-group">
                    <label for="ssn" class="col-sm-2 control-label">SSN</label>
                    <div class="col-sm-4">
                        <input type="text" id="ssn" class="form-control" name="ssn"></input>
                    </div>
                </div>
                <div class="form-group">
                    <label for="userInfo" class="col-sm-2 control-label">UserInfo</label>
                    <div class="col-sm-8">
                        <input type="text" id="userInfo" class="form-control" name="userInfo"></input>
                    </div>
                </div>
                <div class="form-group">
                    <label for="userInfo" class="col-sm-2 control-label"></label>
                    <div class="col-sm-6">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" id="verifyVID"/>SSN으로 verifyVID실행
                            </label>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2">
                        <button onclick="loginDataTest();">로그인 데이터 생성</button>
                    </div>
                </div>
            </form>
        </div>
        </div>

        <div class="panel panel-success">
            <div class="panel-heading">생성결과</div>
            <div class="panel-body">
                <form onsubmit="return false;" class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">로그인데이터</label>
                        <div class="col-sm-10">
                            <textarea class="form-control" id="loginData" rows="5" ></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">인증서 정보</label>
                        <div class="col-sm-10">
                            <div id="signCertInfo" class="scrollable-data"></div>
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
    function login_data_complete_callback(res) {
        if (res.code == 0) {
            $("#loginData").val(res.data.loginData);
            $("#signCertInfo").append(objectToTable("",res.data.certInfo));
         }
        else {
            alert("error code = " + res.code + ", message = " + res.errorMessage);
        }
    }

    function loginDataTest() {
        var verifyVID = $("#verifyVID").is(":checked");
        var options = {};

        var sessionId = $("#sessionId").val();
        var ssn = $("#ssn").val();
        var userInfo = $("#userInfo").val();
		
        if(verifyVID == true) {
			if(ssn != "0") {
				options.ssn = ssn;
			}
			else {
				alert("ssn에 입력된 값이 알맞지 않아 신원확인을 수행하지 않습니다.");
			}
        }
        nxTSPKI.loginData(sessionId, ssn, userInfo, options, login_data_complete_callback);
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
