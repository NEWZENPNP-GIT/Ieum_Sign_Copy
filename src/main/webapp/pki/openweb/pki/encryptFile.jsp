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
        <h3>파일 암호화/복호화</h3>
        <div class="panel panel-primary">
            <div class="panel-heading">암/복호화 정보</div>
            <div class="panel-body">
                <form class="form-horizontal " onsubmit="return false;">
                    <div class="form-group">
                        <label for="key" class="col-sm-2 control-label">암/복호화 Key</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="text" id="key" name="key" placeholder="암호화 키 입력" value="sample-key">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="iv" class="col-sm-2 control-label">알고리즘</label>
                        <div class="col-sm-6">
                            <select id="algorithm" class="form-control" name="algorighm"></select>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="panel panel-info">
            <div class="panel-heading">암호화 파일 정보</div>
            <div class="panel-body">
                <form class="form-horizontal" onsubmit="return false;">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="dataFile">암호화할 파일</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" placeholder="암호화할 파일의 전체경로" name="dataFile" id="dataFile">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="encDataFile">저장할 파일</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" placeholder="암호화한 파일을 저장할 전체 경로" name="encDataFile" id="encDataFile">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="encDataFile"></label>
                        <div class="col-sm-9">
                            <button type="text" onclick="encryptFileTest();" >파일 암호화</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="panel panel-success">
            <div class="panel-heading">암호화 결과</div>
            <div class="panel-body">
                <textarea id="encryptedFileResult" rows="2" cols="80"></textarea>
            </div>
        </div>

        <!--          ------------------------------------------------------------------  -->


        <div class="panel panel-info">
            <div class="panel-heading">복호화 파일 정보</div>
            <div class="panel-body">
                <form class="form-horizontal" onsubmit="return false;">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="vEncDataFile">복호화할 파일</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" placeholder="복호화할 파일의 전체경로" name="vEncDataFile" id="vEncDataFile">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="vDataFile">저장할 파일</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" placeholder="복호화한 파일을 저장할 전체 경로" name="vDataFile" id="vDataFile">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for=""></label>
                        <div class="col-sm-9">
                            <button type="text" onclick="decryptFileTest();" >파일 복호화</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <div class="panel panel-success">
            <div class="panel-heading">복호화 결과</div>
            <div class="panel-body">
                <textarea id="decryptedFileResult" rows="2" cols="80"></textarea>
            </div>
        </div>
        <hr>
        <%@include file="footer.jsp"%>
    </div>
</div>
<script>
    function encrypt_complete_callback(res) {
        if(res.code == 0) {
            $("#encryptedFileResult").val(res.data);
            $("#vEncDataFile").val(res.data);
            $("#vDataFile").val(res.data+".org");
        }
        else {
            alert(res.errorMessage);
        }
    }
    function decrypt_complete_callback(res){
        if(res.code == 0)
            $("#decryptedFileResult").val(res.data);
        else
            alert(res.errorMessage);
    }

    function decryptFileTest() {
        var key = $("#key").val();
        var dataFile = $("#vDataFile").val();
        var encDataFile = $("#vEncDataFile").val();

        var alg = $("#algorithm").val();
        $("#decryptedFileResult").val("");

        nxTSPKI.decryptFile(encDataFile,dataFile,key,{encrpytionAlgorithm:alg},decrypt_complete_callback);
    }

    function encryptFileTest() {
        var key = $("#key").val();
        var dataFile = $("#dataFile").val();
        var encDataFile = $("#encDataFile").val();

        var alg = $("#algorithm").val();
        $("#encryptedFileResult").val("");
        if(encDataFile =="") {
            encDataFile = dataFile + ".nx.enc";
            $("#encDataFile").val(encDataFile);
        }



        nxTSPKI.encryptFile(dataFile,encDataFile,key,{encrpytionAlgorithm:alg},encrypt_complete_callback);
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
