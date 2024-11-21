<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="./common.jsp" %>
    <title>Envelop / Develop File</title>
</head>
<body>
<div class="container">
    <%@include file="header.jsp" %>
    <div class="col-md-9">
        <h2>Envelop / Develop File</h2>
        <div class="panel panel-primary">
            <div class="panel-heading">Envelop File</div>
            <div class="panel-body">
                <form class="form-horizontal" onsubmit="return false;">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="testCert">사용할 인증서</label>
                        <div class="col-sm-9">
                            <textarea cols="80" rows="3" id="testCert" name="testCert"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="dataFile">대상파일</label>
                        <div class="col-sm-6">
                            <input class="form-control" name="dataFile" id="dataFile" type="text" value="" placeholder="Envelop할 파일의 전체 경로"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="envelopedDataFile">저장할 파일</label>
                        <div class="col-sm-6">
                            <input class="form-control" name="envelopedDataFile" id="envelopedDataFile" type="text" value="" placeholder="Envelop한 파일을 저장할 전체 경로"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" ></label>
                        <div>
                            <button onclick="envelopFileTest();">Envelop </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="panel panel-primary">
            <div class="panel-heading">Develop File </div>
            <div class="panel-body">
                <form class="form-horizontal" onsubmit="return false;">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="vEnvelopedDataFile">대상 파일</label>
                        <div class="col-sm-9">
                            <input class="form-control" name="vEnvelopedDataFile" id="vEnvelopedDataFile" type="text" value="" placeholder="Envelop한 파일의 전체경로"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="vDataFile">원본 저장 파일</label>
                        <div class="col-sm-9">
                            <input class="form-control" name="vDataFile" id="vDataFile" type="text" value="" placeholder="Develop후 추출한 원문 데이터를 저장할 파일"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" ></label>
                        <div>
                            <button onclick="developFileTest();">Develop</button>
                        </div>

                    </div>
                </form>
            </div>
        </div>
        <div class="panel panel-success">
            <div class="panel-heading">Develop Result</div>
            <div class="panel-body">
                <textarea id="developResult" rows="2" cols="80"></textarea>
                <h5>키정보</h5>
                <div id="developKeyInfo" class="scrollable-data"></div>
            </div>
        </div>

        <hr/>

        <%@include file="footer.jsp"%>
    </div>
</div>


<script>
    function develop_complete_callback(res) {
        if(res.code == 0) {
            $("#developResult").val(res.data.dataFile);
            $("#developKeyInfo").append(objectToTable("",res.data.keyInfo));
        }
        else {
            alert(res.errorMessage);
        }
    }
    function envelop_complete_callback(res){
        if(res.code == 0) {
            $("#vEnvelopedDataFile").val(res.data);

        }
        else {
            alert(res.errorMessage);
        }
    }

    function envelopFileTest() {
        $("#developKeyInfo").empty();
        var dataFile = $("#dataFile").val();
        var enelopedDataFile = $("#envelopedDataFile").val();

        if(enelopedDataFile == "") {
            enelopedDataFile = dataFile + ".nx.env";
            $("#envelopedDataFile").val(enelopedDataFile);
        }

        var cert = $("#testCert").val();
        nxTSPKI.envelopFile(dataFile,enelopedDataFile,cert,{},envelop_complete_callback);
    }

    function developFileTest() {
        $("#developKeyInfo").empty();
        var envelopedDataFile = $("#vEnvelopedDataFile").val();
        var dataFile = $("#vDataFile").val();
        nxTSPKI.decryptEnvelopedFile(envelopedDataFile,dataFile,{},develop_complete_callback);
    }

    $(document).ready(function(){
        var kmCert        = "-----BEGIN CERTIFICATE-----MIIFnjCCBIagAwIBAgIEWZHJYTANBgkqhkiG9w0BAQsFADBPMQswCQYDVQQGEwJLUjESMBAGA1UECgwJVHJhZGVTaWduMRUwEwYDVQQLDAxBY2NyZWRpdGVkQ0ExFTATBgNVBAMMDFRyYWRlU2lnbkNBMjAeFw0xNTA1MTEwOTI1NDlaFw0xNjA2MDkwMjE4MzlaMG0xCzAJBgNVBAYTAktSMRIwEAYDVQQKDAlUcmFkZVNpZ24xFTATBgNVBAsMDEFjY3JlZGl0ZWRDQTENMAsGA1UECwwEVEVTVDEkMCIGA1UEAwwb7YWM7Iqk7Yq4MV9LVE5FVF8wMDAwMDY4NzI1MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxMlPX/bXTMChBEZmhZcCV3pTQ1hQXNYi4ccYDU5yOjLO0jVkH3RFhJqHuxHcrAi5wSn4K85TwGWNM9xDUGbRCS8x2vPgCvgnt32sBGhvASExJ3vUFfy2L/nP69g4pf0qNdl4wwbQ+6p9qSjpMCtNdLfgGhp/D/e7MVCYuJVVcgcs1r6MrG9/Pw2N5xt/X0aIwQV8gniWsZoE9vg+Dh97O7veieZluo0PbQDMBbTt2FEu2GEk1dbiGDVUF54UGGo2mN9MNT5QN0WpU0njIr7Qjh1t+iI91sMTGZT8DkTvMhXqqiUqz/UIrUg/GAomNo0YAV+l46marYJDsnEIK7MaCQIDAQABo4ICYjCCAl4wgY8GA1UdIwSBhzCBhIAUTV1WCgcD34PK89Vtjxn8EqyQooqhaKRmMGQxCzAJBgNVBAYTAktSMQ0wCwYDVQQKDARLSVNBMS4wLAYDVQQLDCVLb3JlYSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eSBDZW50cmFsMRYwFAYDVQQDDA1LSVNBIFJvb3RDQSA0ggIQCTAdBgNVHQ4EFgQUCtRrJK2rQLkqHCA1n/OcTKUeWKAwDgYDVR0PAQH/BAQDAgbAMHoGA1UdIAEB/wRwMG4wbAYJKoMajJpMAQEDMF8wLgYIKwYBBQUHAgIwIh4gx3QAIMd4yZ3BHLKUACCs9cd4x3jJncEcACDHhbLIsuQwLQYIKwYBBQUHAgEWIWh0dHA6Ly93d3cudHJhZGVzaWduLm5ldC9jcHMuaHRtbDBvBgNVHREEaDBmoGQGCSqDGoyaRAoBAaBXMFUMEO2FjOyKpO2KuDFfS1RORVQwQTA/BgoqgxqMmkQKAQEBMDEwCwYJYIZIAWUDBAIBoCIEICbtJ2uzRKnAoHO/WR3+tCkRJ/qIPdSjHRHTHrJ7pt2/MGYGA1UdHwRfMF0wW6BZoFeGVWxkYXA6Ly9sZGFwLnRyYWRlc2lnbi5uZXQ6Mzg5L2NuPWNybDFkcDU4MyxvdT1jcmxkcDIsb3U9QWNjcmVkaXRlZENBLG89VHJhZGVTaWduLGM9S1IwRgYIKwYBBQUHAQEEOjA4MDYGCCsGAQUFBzABhipodHRwOi8vb2NzcC50cmFkZXNpZ24ubmV0OjE4MDAwL09DU1BTZXJ2ZXIwDQYJKoZIhvcNAQELBQADggEBAIE6DplwEq9PqwW/2M3uEnPOt70LS7aRFVSM+uDO/OeUjRdQ//0CKWnldZ3/qbsT8dwUIRl9A1E/GwDsJJIQ7/QLcFA8swk5EiXkCDkZyPT6JFuS45ibS0xKwbp1xDYXyX2iYEXsrFw/2In2hG4QjGDunMuA7N1YSBTiEMbaDh5LxXrJPoXlo3BOJziVUFbFeneX/8GiusBkuvWwqOxSjkY82GJIDX51zF9l629iUxGVURpJN6cV3RBdDk3r6FXXGPGnfhswruL8uGoFIp4/BX6lE5ZTLc8hZ1GA1oLdrTC3yC9qNsPACnP+6Y5OQk2UomxZHzEMYlHQrar0lAd4/ls=-----END CERTIFICATE-----";
        $("#testCert").val(kmCert);
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
