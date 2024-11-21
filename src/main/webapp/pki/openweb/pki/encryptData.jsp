<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="./common.jsp" %>
    <title>암호화 테스트</title>
</head>
<body>

<div class="container">
    <%@include file="header.jsp" %>
    <div class="col-md-9">
        <h3>데이터 암호화/복호화</h3>
        <div class="panel panel-primary">
            <div class="panel-heading">키 정보</div>
            <div class="panel-body">
                <form class="form-horizontal " onsubmit="return false;">
                    <div class="form-group">
                        <label for="key" class="col-sm-4 control-label">키 타입</label>
                        <div class="col-sm-6">
                            <input type="radio" name="keyType" id="keyTypeGen" value="0" checked> 키생성 (문자열)
                            <input type="radio" name="keyType" id="keyTypeSet" value="1">키입력 (B64인코딩된 값)
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="algorithm" class="col-sm-4 control-label">알고리즘</label>
                        <div class="col-sm-6">
                            <select id="algorithm" class="form-control" name="algorithm"></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="key" class="col-sm-4 control-label">암호화 Key</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="text" id="key" name="key" placeholder="암호화 키 입력" value="sample-key">
                        </div>
                    </div>
                    <div class="form-group" id="ivArea">
                        <label for="iv" class="col-sm-4 control-label">암호화 IV</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="text" id="iv" name="iv" placeholder="암호화 IV 입력">
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="panel panel-primary">
            <div class="panel-heading">입력데이터</div>
            <div class="panel-body">
                <textarea id="inputData" rows="5" cols="80">sample data</textarea>
                <button onclick="encryptDataTest();"> 암호화 </button>
            </div>
        </div>
        <div class="panel panel-success">
            <div class="panel-heading">encrypted 데이터</div>
            <div class="panel-body">
                <textarea id="encryptedData" rows="5" cols="80"></textarea>
                <button onclick="decryptDataTest();"> 복호화 </button>
            </div>
        </div>
        <div class="panel panel-success">
            <div class="panel-heading">decrypted 데이터</div>
            <div class="panel-body">
                <textarea id="decryptedData" rows="5" cols="80"></textarea>
            </div>
        </div>

        <hr>
        <%@include file="footer.jsp"%>
    </div>
</div>
<script>
    function encrypt_complete_callback(res) {
        if(res.code == 0) {
            $("#encryptedData").val(res.data);
        }
        else {
            alert(res.errorMessage);
        }
    }
    function decrypt_complete_callback(res){
        if(res.code == 0)
            $("#decryptedData").val(res.data);
        else
            alert(res.errorMessage);
    }

    function decryptDataTest() {
        var key = $("#key").val();
        var iv = $("#iv").val();
        var data = $("#encryptedData").val();
        var alg = $("#algorithm").val();

        if(iv != "")
            key = {key:key,iv:iv};
        nxTSPKI.decryptData(data,key,{encrpytionAlgorithm:alg},decrypt_complete_callback);
    }

    function encryptDataTest() {
        var key = $("#key").val();
        var iv = $("#iv").val();
        var data = $("#inputData").val();

        var alg = $("#algorithm").val();

        if(iv != "")
            key = {key:key,iv:iv};
        nxTSPKI.encryptData(data,key,{encrpytionAlgorithm:alg},encrypt_complete_callback);
    }

    $(document).ready(function(){
        var algs = ["des","3des","seed"];
        var modes = ["ecb","cbc","cfb","ofb"];

        for(var a=0;a<algs.length;a++) {
            for(var m=0;m<modes.length;m++) {
                var alg = algs[a] + "-" + modes[m];
                $("#algorithm").append('<option value="'+alg+'">'+alg+'</option>');
            }
        }
        $("#algorithm > option[value=3des-cbc]").attr("selected","true");

        $("#ivArea").hide();

        $('input[type=radio][name=keyType]').change(function(){
            $("#key").val("");
            $("#iv").val("");
            if(this.value == "0") {
                $("#ivArea").hide();
            }
            else {
                $("#ivArea").show();
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
