<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="./common.jsp" %>
    <title>LDAP에서 데이터 가져오기</title>
</head>
<body>

<div class="container">
    <%@include file="header.jsp" %>
    <div class="col-md-9">
        <div><h3>LDAP에서 데이터 가져오기.</h3></div>

        <div class="panel panel-primary">
            <div class="panel-heading">DN</div>
            <div class="panel-body">

                <form class="form-horizontal " onsubmit="return false;">
                    <div class="form-group">
                        <label for="dn" class="col-sm-2 control-label">DN</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="text" id="dn" name="dn" value="cn=테스트(KTNET),ou=KTNET,ou=LicensedCA,o=TradeSign,c=KR">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="dataType" class="col-sm-2 control-label">데이터 타입</label>
                        <div class="col-sm-6">
                            <select id="dataType" class="form-control" name="dataType">
                                <option value="1">첫번째 인증서</option>
                                <option value="2" selected >서명용 인증서</option>
                                <option value="3">암호용 인증서</option>
                                <option value="4">CRL</option>
                                <option value="5">ARL</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label  class="col-sm-2 control-label"></label>
                        <div class="col-sm-6">
                            <button onclick="getDataFromLDAPTest();"> LDAP에서 인증서 가져오기</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <div class="panel panel-success">
            <div class="panel-heading">결과</div>
            <div class="panel-body">
                <textarea id="result" rows="15" ></textarea>
            </div>
        </div>

        <hr>
        <%@include file="footer.jsp" %>

    </div>
</div>



<script>
    function get_data_complete_callback(res) {
        if (res.code == 0) {
            $("#result").val(res.data);
        }
        else {
            alert("error code = " + res.code + ", message = " + res.errorMessage);
        }
    }

    function getDataFromLDAPTest() {
        var url = $("#dn").val();
        var dataType = $("#dataType").val();
        $("#result").val("");

        nxTSPKI.getDataFromLDAP(url,dataType,{}, get_data_complete_callback);
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
